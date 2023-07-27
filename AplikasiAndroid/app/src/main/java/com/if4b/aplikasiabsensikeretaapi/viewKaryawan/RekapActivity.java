package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelKaryawan;
import com.if4b.aplikasiabsensikeretaapi.viewManager.RekapKaryawanActivity;

public class RekapActivity extends AppCompatActivity {
    TextView tvNama, tvJumlah, tvPersen, tvHariKerja;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private int totalAbsen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap);

        tvNama = findViewById(R.id.tv_nama_rekap);
        tvJumlah = findViewById(R.id.tv_jumlah_absen_rekap);
        tvPersen = findViewById(R.id.tv_persentase_rekap);
        tvHariKerja = findViewById(R.id.tv_hari_kerja);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelKaryawan modelKaryawan = snapshot.getValue(ModelKaryawan.class);
                    if (modelKaryawan != null) {
                        tvNama.setText("Nama: " + modelKaryawan.getUsername());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("TabelAbsensiMasukKaryawan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalAbsen = (int) snapshot.getChildrenCount();
                tvJumlah.setText("Jumlah Absensi: " + totalAbsen);
                int totalHariKerja = 20; // Jumlah hari kerja dalam sebulan
                double persentaseAbsen = (totalAbsen / (double) totalHariKerja) * 100;
                tvPersen.setText("Persentase Absen: " + persentaseAbsen + "%");
                tvHariKerja.setText("Total Hari Kerja: " + totalHariKerja + " Hari");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reference.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(valueEventListener);
    }

    private ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.exists()) {
                ModelKaryawan modelKaryawan = snapshot.getValue(ModelKaryawan.class);
                if (modelKaryawan != null) {
                    tvNama.setText("Nama: " + modelKaryawan.getUsername());
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };
}

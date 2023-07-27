package com.if4b.aplikasiabsensikeretaapi.viewManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.if4b.aplikasiabsensikeretaapi.model.ModelRekap;

import java.util.ArrayList;

public class RekapKaryawanActivity extends AppCompatActivity {
    TextView tvNama, tvJumlah, tvPersen, tvHariKerja;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private int totalAbsen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_karyawan);

        totalAbsen = 0;
        tvNama = findViewById(R.id.tv_nama_rekap_karyawan);
        tvJumlah = findViewById(R.id.tv_jumlah_absen_rekap_karyawan);
        tvPersen = findViewById(R.id.tv_persentase_rekap_karyawan);
        tvHariKerja = findViewById(R.id.tv_hari_kerja_karyawan);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dataList = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    String nama = data.child("username").getValue(String.class);


                    String dataItem = "Nama: " + nama;

                    dataList.add(dataItem);
                }

                StringBuilder stringBuilder = new StringBuilder();
                for (String data : dataList) {
                    stringBuilder.append(data);
                }

                tvNama.setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("TabelAbsensiMasukKaryawan").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                totalAbsen = (int) snapshot.getChildrenCount();
                tvJumlah.setText("Jumlah Absensi: " + totalAbsen );
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
}
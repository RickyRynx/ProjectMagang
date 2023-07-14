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

public class RekapManagerActivity extends AppCompatActivity {
    TextView tvNama, tvJumlah, tvPersen, tvHariKerja, tvNamaKaryawan, tvJumlahKaryawan, tvPersenKaryawan, tvHariKerjaKaryawan;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private int totalAbsen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_manager);

        totalAbsen = 0;
        tvNama = findViewById(R.id.tv_nama_rekap_manager);
        tvJumlah = findViewById(R.id.tv_jumlah_absen_rekap_manager);
        tvPersen = findViewById(R.id.tv_persentase_rekap_manager);
        tvHariKerja = findViewById(R.id.tv_hari_kerja_manager);

        reference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelManager").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelRekap modelRekap = snapshot.getValue(ModelRekap.class);
                    tvNama.setText("Nama: " + modelRekap.getUsername());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("TabelAbsensiMasukManager").addListenerForSingleValueEvent(new ValueEventListener() {
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
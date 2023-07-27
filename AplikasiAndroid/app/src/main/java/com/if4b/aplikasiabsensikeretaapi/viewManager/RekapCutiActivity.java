package com.if4b.aplikasiabsensikeretaapi.viewManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelCuti;
import com.if4b.aplikasiabsensikeretaapi.model.ModelProfil;
import com.if4b.aplikasiabsensikeretaapi.model.ModelRekap;
import com.if4b.aplikasiabsensikeretaapi.model.ModelRekapCuti;

import java.util.ArrayList;

public class RekapCutiActivity extends AppCompatActivity {

    ImageView ivProfil;
    TextView tvNamaCuti, tvJabatanCuti, tvKeterangan, tvMulai, tvSelesai;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap_cuti);

        tvNamaCuti = findViewById(R.id.tvNamaKaryawan);
        tvJabatanCuti = findViewById(R.id.tvJabatanKaryawan);
        tvKeterangan = findViewById(R.id.tvKeteranganKaryawan);
        tvMulai = findViewById(R.id.tvMulaiKaryawan);
        tvSelesai = findViewById(R.id.tvSelesaiKaryawan);
        ivProfil = findViewById(R.id.iv_profil_cuti_karyawan);


        reference = FirebaseDatabase.getInstance().getReference("TabelPengajuanCuti");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> dataList = new ArrayList<>();
                ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                String urlFotoProfil = modelProfil.getUrl_foto_profil();
                if (urlFotoProfil != null) {
                    Glide.with(RekapCutiActivity.this)
                            .load(urlFotoProfil)
                            .into(ivProfil);
                }

                for (DataSnapshot data : snapshot.getChildren()) {
                    // Ambil data dari setiap child dan tambahkan ke ArrayList
                    String nama = data.child("nama").getValue(String.class);
                    String jabatan = data.child("jabatan").getValue(String.class);
                    String keterangan = data.child("keterangan_cuti").getValue(String.class);
                    String tanggalMulai = data.child("tanggal_mulai_cuti").getValue(String.class);
                    String tanggalSelesai = data.child("tanggal_selesai_cuti").getValue(String.class);

                    // Buat string dari data yang diambil
                    String dataItem = "Nama: " + nama + "\n"
                            + "Jabatan: " + jabatan + "\n"
                            + "Keterangan: " + keterangan + "\n"
                            + "Mulai: " + tanggalMulai + "\n"
                            + "Selesai: " + tanggalSelesai + "\n\n";

                    // Tambahkan dataItem ke ArrayList
                    dataList.add(dataItem);
                }

                // Gabungkan semua data dari ArrayList menjadi satu string
                StringBuilder stringBuilder = new StringBuilder();
                for (String data : dataList) {
                    stringBuilder.append(data);
                }

                // Tampilkan data ke TextView
                tvNamaCuti.setText(stringBuilder.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
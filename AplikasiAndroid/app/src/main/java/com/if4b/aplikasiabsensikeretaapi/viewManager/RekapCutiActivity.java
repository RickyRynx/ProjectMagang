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
import com.if4b.aplikasiabsensikeretaapi.model.ModelCuti;
import com.if4b.aplikasiabsensikeretaapi.model.ModelProfil;
import com.if4b.aplikasiabsensikeretaapi.model.ModelRekap;
import com.if4b.aplikasiabsensikeretaapi.model.ModelRekapCuti;

public class RekapCutiActivity extends AppCompatActivity {

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


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelPengajuanCuti").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
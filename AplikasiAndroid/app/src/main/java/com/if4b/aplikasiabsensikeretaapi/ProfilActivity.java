package com.if4b.aplikasiabsensikeretaapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
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
import com.if4b.aplikasiabsensikeretaapi.model.ModelProfil;
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;

public class ProfilActivity extends AppCompatActivity {
    TextView tvNama;
    TextView tvEmail;
    TextView tvJabatan;
    TextView tvAlamat;
    TextView tvNo;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tvNama = findViewById(R.id.tvNamaProfil);
        tvEmail = findViewById(R.id.tvProfilEmail);
        tvJabatan = findViewById(R.id.tvProfilJabatan);
        tvAlamat = findViewById(R.id.tvProfilAlamat);
        tvNo = findViewById(R.id.tvProfilNo);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                    tvNama.setText("Nama: " + modelProfil.getUsername());
                    tvEmail.setText("Email: " + modelProfil.getEmail());
                    tvJabatan.setText("Jabatan: " + modelProfil.getJabatan());
                    tvAlamat.setText("Alamat: " + modelProfil.getAlamat());
                    tvNo.setText("No HP: " + modelProfil.getNoHp());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
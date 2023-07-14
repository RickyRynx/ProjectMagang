package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

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
    TextView tvNama, tvNipp, tvEmail, tvAlamat, tvJabatan, tvNo, tvTempat;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tvNama = findViewById(R.id.tvNamaProfil);
        tvNipp = findViewById(R.id.tvNipp);
        tvEmail = findViewById(R.id.tvProfilEmail);
        tvJabatan = findViewById(R.id.tvProfilJabatan);
        tvAlamat = findViewById(R.id.tvProfilAlamat);
        tvTempat = findViewById(R.id.tvProfilPenempatan);
        tvNo = findViewById(R.id.tvProfilNo);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                    tvNama.setText(modelProfil.getUsername());
                    tvNipp.setText(modelProfil.getNipp());
                    tvEmail.setText(modelProfil.getEmail());
                    tvJabatan.setText(modelProfil.getJabatan());
                    tvAlamat.setText(modelProfil.getAlamat());
                    tvTempat.setText(modelProfil.getPenempatan());
                    tvNo.setText(modelProfil.getNo_hp());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
package com.if4b.aplikasiabsensikeretaapi.viewManager;

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

public class ProfilManagerActivity extends AppCompatActivity {
    TextView tvNama, tvNipp, tvEmail, tvAlamat, tvJabatan, tvNo, tvTempat;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil_manager);

        tvNama = findViewById(R.id.tvNamaProfilManager);
        tvNipp = findViewById(R.id.tvNippManager);
        tvEmail = findViewById(R.id.tvProfilEmailManager);
        tvJabatan = findViewById(R.id.tvProfilJabatanManager);
        tvAlamat = findViewById(R.id.tvProfilAlamatManager);
        tvTempat = findViewById(R.id.tvProfilPenempatanManager);
        tvNo = findViewById(R.id.tvProfilNoManager);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelManager").child(firebaseUser.getUid());
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
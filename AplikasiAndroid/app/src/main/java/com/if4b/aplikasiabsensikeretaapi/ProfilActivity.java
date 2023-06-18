package com.if4b.aplikasiabsensikeretaapi;

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
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;

public class ProfilActivity extends AppCompatActivity {
    TextView tvNama, tvEmail, tvJabatan, tvAlamat, tvNo;
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
        reference = FirebaseDatabase.getInstance().getReference("modelUser").child("ModelUser").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelUser modelUser = snapshot.getValue(ModelUser.class);
                    tvNama.setText(modelUser.getUsername());
                    tvEmail.setText(modelUser.getEmail());
                    tvJabatan.setText(modelUser.getJabatan());
                    tvAlamat.setText(modelUser.getAlamat());
                    tvNo.setText(modelUser.getNomor());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
package com.if4b.aplikasiabsensikeretaapi.viewManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.if4b.aplikasiabsensikeretaapi.model.ModelManager;
import com.if4b.aplikasiabsensikeretaapi.model.ModelProfil;
import com.if4b.aplikasiabsensikeretaapi.view.LoginActivity;
import com.if4b.aplikasiabsensikeretaapi.view.PrivacyActivity;
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.ProfilActivity;

public class SettingManagerActivity extends AppCompatActivity {
    ImageView  ivPrivacy, ivLogout, ivBack, ivProfil;
    TextView tvNama;
    AppCompatButton btnProfile;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_manager);

        ivPrivacy = findViewById(R.id.iv_privacy_terms_manager);
        ivLogout = findViewById(R.id.iv_logout_manager);
        btnProfile = findViewById(R.id.btn_edit_profile_manager);
        tvNama = findViewById(R.id.tv_nama_setting_manager);
        ivBack = findViewById(R.id.iv_back_manager);
        ivProfil = findViewById(R.id.iv_profile_manager);


        ivPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingManagerActivity.this, PrivacyActivity.class);
                startActivity(intent);
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingManagerActivity.this, DashManagerActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingManagerActivity.this, ProfilManagerActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("TabelManager").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelManager modelManager = snapshot.getValue(ModelManager.class);
                    ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                    String urlFotoProfil = modelProfil.getUrl_foto_profil();
                    if (urlFotoProfil != null) {
                        Glide.with(SettingManagerActivity.this)
                                .load(urlFotoProfil)
                                .into(ivProfil);
                    }
                    tvNama.setText(modelManager.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logout() {
        // Hapus data sesi atau token
        SharedPreferences sharedPreferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Lanjutkan ke halaman login
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
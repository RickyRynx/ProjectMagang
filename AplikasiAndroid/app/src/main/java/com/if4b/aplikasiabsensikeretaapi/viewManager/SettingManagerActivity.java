package com.if4b.aplikasiabsensikeretaapi.viewManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.if4b.aplikasiabsensikeretaapi.ProfilActivity;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;
import com.if4b.aplikasiabsensikeretaapi.view.HistoryActivity;
import com.if4b.aplikasiabsensikeretaapi.view.LoginActivity;
import com.if4b.aplikasiabsensikeretaapi.view.PrivacyActivity;

public class SettingManagerActivity extends AppCompatActivity {
    ImageView ivHistory, ivPrivacy, ivLogout;
    TextView tvNama;
    AppCompatButton btnProfile;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_manager);

        ivHistory = findViewById(R.id.iv_history_manager);
        ivPrivacy = findViewById(R.id.iv_privacy_terms_manager);
        ivLogout = findViewById(R.id.iv_logout_manager);
        btnProfile = findViewById(R.id.btn_edit_profile_manager);
        tvNama = findViewById(R.id.tv_nama_setting_manager);

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingManagerActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

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
                Intent intent = new Intent(SettingManagerActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingManagerActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("modelUser").child("ModelUser").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelUser modelUser = snapshot.getValue(ModelUser.class);
                    tvNama.setText(modelUser.getUsername());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
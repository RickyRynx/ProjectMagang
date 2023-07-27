package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.if4b.aplikasiabsensikeretaapi.model.ModelKaryawan;
import com.if4b.aplikasiabsensikeretaapi.model.ModelProfil;
import com.if4b.aplikasiabsensikeretaapi.view.SettingActivity;
import com.if4b.aplikasiabsensikeretaapi.viewManager.SettingManagerActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashKaryawanActivity extends AppCompatActivity {
    ImageView ivIn, ivOut, ivSett, ivCuti, ivRekapan, ivProfil;
    TextView username, jabatan, tvHari, tvTanggal, tvJam;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_users);

        username = findViewById(R.id.tv_nama_user);
        jabatan = findViewById(R.id.tv_jbt_user);
        tvHari = findViewById(R.id.tvHari_user);
        tvJam = findViewById(R.id.tvJam_user);
        tvTanggal = findViewById(R.id.tvTanggal_user);
        ivIn = findViewById(R.id.iv_in_user);
        ivOut = findViewById(R.id.iv_out_user);
        ivSett = findViewById(R.id.iv_sett_user);
        ivCuti = findViewById(R.id.iv_cuti_user);
        ivProfil = findViewById(R.id.iv_profil_user);
        ivRekapan = findViewById(R.id.iv_rekap_user);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        ivIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashKaryawanActivity.this, AbsensiMasukActivity.class);
                startActivity(intent);
            }
        });

        ivOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashKaryawanActivity.this, AbsensiKeluarActivity.class);
                startActivity(intent);
            }
        });

        ivSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashKaryawanActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });


        ivCuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashKaryawanActivity.this, CutiActivity.class);
                startActivity(intent);
            }
        });

        ivRekapan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashKaryawanActivity.this, RekapActivity.class);
                startActivity(intent);
            }
        });

        ivProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashKaryawanActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });



        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
        SimpleDateFormat clockFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        tvHari.setText(dayFormat.format(date));
        tvTanggal.setText(dateFormat.format(date));
        tvJam.setText(clockFormat.format(date));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelKaryawan modelKaryawan = snapshot.getValue(ModelKaryawan.class);
                    ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                    String urlFotoProfil = modelProfil.getUrl_foto_profil();
                    if (urlFotoProfil != null) {
                        Glide.with(DashKaryawanActivity.this)
                                .load(urlFotoProfil)
                                .into(ivProfil);
                    }
                    username.setText(modelKaryawan.getUsername());
                    jabatan.setText(modelKaryawan.getJabatan());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}
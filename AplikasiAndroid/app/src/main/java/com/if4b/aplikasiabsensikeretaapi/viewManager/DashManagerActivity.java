package com.if4b.aplikasiabsensikeretaapi.viewManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.if4b.aplikasiabsensikeretaapi.model.ModelManager;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.AbsensiKeluarActivity;
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.AbsensiMasukActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashManagerActivity extends AppCompatActivity {
    ImageView ivIn, ivOut, ivHist, ivSett, ivCuti, ivRekap;
    TextView username, jabatan, tvHari, tvTanggal, tvJam;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_manager);

        username = findViewById(R.id.tv_nama_manager);
        jabatan = findViewById(R.id.tv_jbt_manager);
        tvHari = findViewById(R.id.tvHari_manager);
        tvJam = findViewById(R.id.tvJam_manager);
        tvTanggal = findViewById(R.id.tvTanggal_manager);
        ivIn = findViewById(R.id.iv_in_manager);
        ivOut = findViewById(R.id.iv_out_manager);
        ivSett = findViewById(R.id.iv_sett_manager);
        ivCuti = findViewById(R.id.iv_cuti_manager);
        ivRekap = findViewById(R.id.iv_rekap_manager);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();

        ivIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashManagerActivity.this, AbsensiMasukActivity.class);
                startActivity(intent);
            }
        });

        ivOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashManagerActivity.this, AbsensiKeluarActivity.class);
                startActivity(intent);
            }
        });

        ivSett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashManagerActivity.this, SettingManagerActivity.class);
                startActivity(intent);
            }
        });

        ivCuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashManagerActivity.this, CutiManagerActivity.class);
                startActivity(intent);
            }
        });

        ivRekap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashManagerActivity.this, IntentActivity.class);
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
        reference = FirebaseDatabase.getInstance().getReference().child("TabelManager").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelManager modelManager = snapshot.getValue(ModelManager.class);
                    username.setText(modelManager.getUsername());
                    jabatan.setText(modelManager.getJabatan());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
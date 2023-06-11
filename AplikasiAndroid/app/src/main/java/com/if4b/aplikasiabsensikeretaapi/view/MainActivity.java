package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.if4b.aplikasiabsensikeretaapi.AbsensiKeluarActivity;
import com.if4b.aplikasiabsensikeretaapi.AbsensiMasukActivity;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView username, jabatan, tvHari, tvTanggal, tvJam;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;


    Button btnMasuk, btnKeluar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username = findViewById(R.id.tv_nama);
        jabatan = findViewById(R.id.tv_jbt);
        tvHari = findViewById(R.id.tvHari);
        tvJam = findViewById(R.id.tvJam);
        tvTanggal = findViewById(R.id.tvTanggal);
        btnMasuk = findViewById(R.id.btn_in);
        btnKeluar = findViewById(R.id.btn_out);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference();


        btnMasuk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AbsensiMasukActivity.class));
            }
        });

        btnKeluar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AbsensiKeluarActivity.class));
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
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelUser modelUser = snapshot.getValue(ModelUser.class);
                    username.setText(modelUser.getUsername());
                    jabatan.setText(modelUser.getJabatan());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


}





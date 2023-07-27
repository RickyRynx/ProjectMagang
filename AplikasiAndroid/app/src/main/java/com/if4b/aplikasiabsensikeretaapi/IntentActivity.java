package com.if4b.aplikasiabsensikeretaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.RekapActivity;
import com.if4b.aplikasiabsensikeretaapi.viewManager.RekapCutiActivity;
import com.if4b.aplikasiabsensikeretaapi.viewManager.RekapKaryawanActivity;
import com.if4b.aplikasiabsensikeretaapi.viewManager.RekapManagerActivity;

public class IntentActivity extends AppCompatActivity {
    Button btnAbsen, btnCuti, btnAbsenKaryawan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        btnAbsen = findViewById(R.id.btn_data_absen);
        btnCuti = findViewById(R.id.btn_data_cuti);
        btnAbsenKaryawan = findViewById(R.id.btn_data_absen_karyawan);

        btnAbsen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntentActivity.this, RekapManagerActivity.class);
                startActivity(intent);
            }
        });

        btnCuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntentActivity.this, RekapCutiActivity.class);
                startActivity(intent);
            }
        });

        btnAbsenKaryawan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IntentActivity.this, RekapKaryawanActivity.class);
                startActivity(intent);
            }
        });

    }
}
package com.if4b.aplikasiabsensikeretaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AbsensiKeluarActivity extends AppCompatActivity {
    private EditText etNama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_keluar);

        etNama = findViewById(R.id.etNama);
    }

    public void absenOut(View view) {
        String nama = etNama.getText().toString();
        String jamKeluar = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String pesan = "Absensi Keluar Berhasil. Nama: " + nama + ", Jam Keluar: " + jamKeluar;
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
        etNama.setText("");
    }
}
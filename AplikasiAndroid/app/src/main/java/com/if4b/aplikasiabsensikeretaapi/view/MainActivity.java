package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.if4b.aplikasiabsensikeretaapi.AbsensiKeluarActivity;
import com.if4b.aplikasiabsensikeretaapi.AbsensiMasukActivity;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.DBApp;
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String user;
    TextView username, jabatan, tvHari, tvTanggal, tvJam;
     Button btnMasuk, btnKeluar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBApp helper;
        helper = new DBApp(this);

        username = findViewById(R.id.tv_nama);
        jabatan = findViewById(R.id.tv_jbt);
        tvHari = findViewById(R.id.tvHari);
        tvJam = findViewById(R.id.tvJam);
        tvTanggal = findViewById(R.id.tvTanggal);
        btnMasuk = findViewById(R.id.btn_in);
        btnKeluar = findViewById(R.id.btn_out);

        user = getIntent().getStringExtra("user");
        getUsers();

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

    }

    public void getUsers() {
        DBApp helper = new DBApp(this);
        ArrayList<ModelUser> modelUsers = helper.getUserDetails(user);
        ModelUser models = modelUsers.get(0);

        username.setText(models.getUsername());
        jabatan.setText(models.getJabatan());
    }


}





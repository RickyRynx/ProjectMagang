package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.database.DBApp;
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String user;
    TextView username, jabatan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBApp helper;
        helper = new DBApp(this);

        username = findViewById(R.id.tv_nama);
        jabatan = findViewById(R.id.tv_jbt);

        user = getIntent().getStringExtra("user");
        getUsers();

    }

    public void getUsers() {
        DBApp helper = new DBApp(this);
        ArrayList<ModelUser> modelUsers = helper.getUserDetails(user);
        ModelUser models = modelUsers.get(0);

        username.setText(models.getUsername());
        jabatan.setText(models.getJabatan());
    }
}
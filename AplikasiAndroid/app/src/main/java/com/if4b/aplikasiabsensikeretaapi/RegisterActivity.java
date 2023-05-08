package com.if4b.aplikasiabsensikeretaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etUsername, etPassword, etKonfirmasiPassword, etAlamat, etJabatan, etNoHp;
    private Button btnReg;
    private DBApp dbApp;
    private SQLiteDatabase dbricky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_passwordreg);
        etKonfirmasiPassword = findViewById(R.id.et_konfirmasi_password);
        etAlamat = findViewById(R.id.et_alamat);
        etJabatan = findViewById(R.id.et_jabatan);
        etNoHp = findViewById(R.id.et_nomor_hp);
        btnReg = findViewById(R.id.btn_register);
        DBApp dbApp = new DBApp(this);
        dbricky = dbApp.getWritableDatabase();



        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String konfirmasiPassword = etKonfirmasiPassword.getText().toString().trim();
                String alamat = etAlamat.getText().toString().trim();
                String jabatan = etJabatan.getText().toString().trim();
                String noHp = etNoHp.getText().toString().trim();

                ContentValues values = new ContentValues();

                if (username.isEmpty()) {
                    etUsername.setError("Username tidak boleh kosong");
                    etUsername.requestFocus();
                } else if (password.isEmpty()) {
                    etPassword.setError("Password tidak boleh kosong");
                    etPassword.requestFocus();
                } else if (konfirmasiPassword.isEmpty()) {
                    etKonfirmasiPassword.setError("Konfirmasi Password tidak boleh kosong");
                    etKonfirmasiPassword.requestFocus();
                } else if (alamat.isEmpty()) {
                    etAlamat.setError("Alamat tidak boleh kosong");
                    etAlamat.requestFocus();
                } else if (jabatan.isEmpty()) {
                    etJabatan.setError("Jabatan tidak boleh kosong");
                    etJabatan.requestFocus();
                } else if (noHp.isEmpty()) {
                    etNoHp.setError("Nomor HP tidak boleh kosong");
                    etNoHp.requestFocus();
                } else {
                        values.put(DBApp.COLUMN_USERNAME, username);
                        values.put(DBApp.COLUMN_PASSWORD, password);
                        values.put(DBApp.COLUMN_KONFIRMASI_PASSWORD, konfirmasiPassword);
                        values.put(DBApp.COLUMN_ALAMAT, alamat);
                        values.put(DBApp.COLUMN_JABATAN, jabatan);
                        values.put(DBApp.COLUMN_NOMOR_HP, noHp);
                        dbApp.insertData(values);
                        Toast.makeText(RegisterActivity.this, "Registrasi berhasil", Toast.LENGTH_SHORT).show();
                        finish();
                    }
            }
        });
    }

    @Override
    protected void onDestroy() {
        dbApp.close();
        dbricky.close();
        super.onDestroy();
    }

}

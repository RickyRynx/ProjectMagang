package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.database.DBApp;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etUsername, etPassword, etKonfirmasiPassword, etAlamat, etJabatan, etNoHp;
    private Button btnReg;
    DBApp helper;

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
        helper = new DBApp(this);



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
                        if (password.equals(konfirmasiPassword)) {
                            Boolean checkUserName = helper.checkUsername(username);

                            if (checkUserName == false) {
                                Boolean insert = helper.insertData(username, password, konfirmasiPassword, alamat, jabatan, noHp);

                                if (insert == true) {
                                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Registrasi Gagal!!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(RegisterActivity.this, "Username Telah Digunakan", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "Password Salah", Toast.LENGTH_SHORT).show();
                        }
                    }
            }
        });
    }


}

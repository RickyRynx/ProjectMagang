package com.if4b.aplikasiabsensikeretaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    Button btnLogin;
    EditText username, password, conf_password, alamat, jabatan, no_hp;
    Button btnReg;
    DBApp DB_ricky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_passwordreg);
        conf_password = findViewById(R.id.et_konfirmasi_password);
        alamat = findViewById(R.id.et_alamat);
        jabatan = findViewById(R.id.et_jabatan);
        no_hp = findViewById(R.id.et_nomor_hp);
        btnReg = findViewById(R.id.btn_register);
        DB_ricky = new DBApp(this);
        
        Register_user();

        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }


    private void Register_user() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String Conf_password = conf_password.getText().toString();
                String Alamat = alamat.getText().toString();
                String Jabatan = jabatan.getText().toString();
                String No_Hp = no_hp.getText().toString();

                if(!isUsernameValid(Username)) {
                    Toast.makeText(RegisterActivity.this, "Username Salah!! Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
                if(!isPasswordValid(Password)) {
                    Toast.makeText(RegisterActivity.this, "Password Salah!! Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
                if(!isConfPasswordValid(Conf_password)) {
                    Toast.makeText(RegisterActivity.this, "Konfirmasi Password Tidak Sesuai!! Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
                if(!isAlamatValid(Alamat)) {
                    Toast.makeText(RegisterActivity.this, "Alamat Salah!! Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
                if(!isJabatanValid(Jabatan)) {
                    Toast.makeText(RegisterActivity.this, "Jabatan Salah!! Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
                if(!isNomorHpValid(No_Hp)) {
                    Toast.makeText(RegisterActivity.this, "Nomor Hp Salah!! Silahkan Coba Lagi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isNomorHpValid(String no_hp) {
        return no_hp.isEmpty();
    }

    private boolean isJabatanValid(String jabatan) {
        return jabatan.isEmpty();
    }

    private boolean isAlamatValid(String alamat) {
        return alamat.isEmpty();
    }

    private boolean isConfPasswordValid(String conf_password) {
        return conf_password.length()>6;
    }

    private boolean isPasswordValid(String password) {
        return password.length()>6;
    }

    private boolean isUsernameValid(String username) {
        return username.isEmpty();
    }
}
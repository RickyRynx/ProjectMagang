package com.if4b.aplikasiabsensikeretaapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText username, password;
    Button btnLogin, btnRegister;
    DBApp DB_ricky;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.et_username);
        password = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);

        DB_ricky = new DBApp(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                lanjutLogin();
            }
        });


        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void lanjutLogin() {
        String Username = username.getText().toString();
        String Password = password.getText().toString();

        if(!isUsernameValid(Username)){
            Toast.makeText(this, "Username Salah", Toast.LENGTH_SHORT).show();
        }

        if(!isPasswordInvalid(Password)) {
            Toast.makeText(this, "Password Salah", Toast.LENGTH_SHORT).show();
        }

        Cursor res = DB_ricky.login_user(Username, Password);
        if(res.getCount()==1){
            final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Akun Tidak Diketahui", Toast.LENGTH_SHORT).show();
        }
    }


    private boolean isUsernameValid(String username) {
        return username.contains("text");
    }

    private boolean isPasswordInvalid(String password) {
        return password.length()>6;
    }
}
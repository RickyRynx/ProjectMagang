package com.if4b.aplikasiabsensikeretaapi.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.if4b.aplikasiabsensikeretaapi.DBApp;
import com.if4b.aplikasiabsensikeretaapi.R;


public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();


        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                loginAuth();
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

    private void loginAuth() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty()) {
            etPassword.setError("Password tidak boleh kosong");
            etPassword.requestFocus();
        } else if (password.isEmpty() || password.length()<6) {
            etPassword.setError("Password Minimum 6 Karakter");
            etPassword.requestFocus();
        } else {
            progressDialog.setMessage("Login...");
            progressDialog.setTitle("Login Berhasil!!");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        loginUser();
                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void loginUser() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
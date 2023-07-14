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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.if4b.aplikasiabsensikeretaapi.model.ModelKaryawan;
import com.if4b.aplikasiabsensikeretaapi.model.ModelManager;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.viewManager.DashManagerActivity;
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.DashKaryawanActivity;


public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;

    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseDatabase mDatabase;


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
                loginAuthKaryawan();
                loginAuthManager();
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



    private void loginAuthKaryawan() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();



        if (email.isEmpty()) {
            etEmail.setError("Password tidak boleh kosong");
            etEmail.requestFocus();
        } else if (password.isEmpty() || password.length()<6) {
            etPassword.setError("Password Minimum 6 Karakter");
            etPassword.requestFocus();
        }  else {

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ModelKaryawan modelKaryawan = snapshot.getValue(ModelKaryawan.class);
                                    if (modelKaryawan != null) {
                                        modelKaryawan.getJabatan();
                                        loginKaryawan();
                                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Login Gagal!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                }
            });

        }

    }

    private void loginAuthManager() {
        String nipp = etEmail.getText().toString();
        String password = etPassword.getText().toString();



        if (nipp.isEmpty()) {
            etEmail.setError("Password tidak boleh kosong");
            etEmail.requestFocus();
        } else if (password.isEmpty() || password.length()<6) {
            etPassword.setError("Password Minimum 6 Karakter");
            etPassword.requestFocus();
        }  else {

            mAuth.signInWithEmailAndPassword(nipp, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("TabelManager");
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    ModelManager modelManager = snapshot.getValue(ModelManager.class);
                                    if (modelManager != null) {
                                        modelManager.getJabatan();
                                        loginManager();
                                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                    }

                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(LoginActivity.this, "Login Gagal!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }

                }
            });

        }
    }


    private void loginKaryawan() {
        Intent intent = new Intent(LoginActivity.this, DashKaryawanActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loginManager() {
        Intent intent = new Intent(LoginActivity.this, DashManagerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
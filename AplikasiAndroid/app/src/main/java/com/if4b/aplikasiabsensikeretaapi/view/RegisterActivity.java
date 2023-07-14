package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.if4b.aplikasiabsensikeretaapi.R;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    private Button btnLogin;
    private DatabaseReference reference;
    FirebaseDatabase database;
    String[] jabatan = {"Karyawan", "Manager"};

    String[] penempatan = {"Kantor DIVRE III Palembang", "Kantor Sub DIVRE III Kertapati", "Kantor Depo LRT Palembang"};
    AutoCompleteTextView autoTextJabatan, autoTextPenempatan;
    ArrayAdapter<String> adapterJabatan, adapterPenempatan;


    private EditText etUsername, etNipp, etPassword, etKonfirmasiPassword, etAlamat, etNoHp, etEmail;
    private Button btnReg;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.et_username);
        etNipp = findViewById(R.id.et_nipp);
        etEmail = findViewById(R.id.et_email_register);
        etPassword = findViewById(R.id.et_passwordreg);
        etKonfirmasiPassword = findViewById(R.id.et_konfirmasi_password);
        etAlamat = findViewById(R.id.et_alamat);
        etNoHp = findViewById(R.id.et_nomor_hp);
        btnReg = findViewById(R.id.btn_register);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        autoTextJabatan= findViewById(R.id.auto_select_item);
        autoTextPenempatan = findViewById(R.id.auto_penempatan_item);
        adapterJabatan = new ArrayAdapter<String>(this, R.layout.list_select_dropdown, jabatan);
        adapterPenempatan = new ArrayAdapter<String>(this, R.layout.list_select_dropdown, penempatan);
        autoTextJabatan.setAdapter(adapterJabatan);
        autoTextPenempatan.setAdapter(adapterPenempatan);



        btnLogin = findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        autoTextJabatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Masuk Sebagai: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        autoTextPenempatan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Lokasi Penempatan: "+item, Toast.LENGTH_SHORT).show();
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String nipp = etNipp.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String konfirmasiPassword = etKonfirmasiPassword.getText().toString().trim();
                String alamat = etAlamat.getText().toString().trim();
                String penempatan = autoTextPenempatan.getText().toString().trim();
                String jabatan = autoTextJabatan.getText().toString().trim();
                String noHp = etNoHp.getText().toString().trim();



                if (username.isEmpty()) {
                    etUsername.setError("Username tidak boleh kosong");
                    etUsername.requestFocus();
                } else if (nipp.isEmpty()) {
                    etNipp.setError("Password tidak boleh kosong");
                    etNipp.requestFocus();
                } else if (email.isEmpty()) {
                    etEmail.setError("Password tidak boleh kosong");
                    etEmail.requestFocus();
                } else if (password.isEmpty() || password.length() < 6) {
                    etPassword.setError("Password Minimum 6 Karakter");
                    etPassword.requestFocus();
                } else if (konfirmasiPassword.isEmpty() || konfirmasiPassword.length() < 6) {
                    etKonfirmasiPassword.setError("Konfirmasi Password Minimum 6 Karakter");
                    etKonfirmasiPassword.requestFocus();
                } else if (alamat.isEmpty()) {
                    etAlamat.setError("Alamat tidak boleh kosong");
                    etAlamat.requestFocus();
                }  else if (noHp.isEmpty()) {
                    etNoHp.setError("Nomor HP tidak boleh kosong");
                    etNoHp.requestFocus();
                } else {
                    registerAuth(username, nipp, email, password, konfirmasiPassword, alamat, penempatan, jabatan, noHp);
                }
            }
        });
    }

    private void registerAuth(String username, String nipp, String email, String password, String konfirmasiPassword, String alamat, String penempatan, String jabatan, String noHp) {
        progressDialog.setMessage("Loading Registrasi...");
        progressDialog.show();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();

        mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", mAuth.getCurrentUser().getUid());
                map.put("username", username);
                map.put("nipp", nipp);
                map.put("email", email);
                map.put("password", password);
                map.put("konfirmasi_password", konfirmasiPassword);
                map.put("alamat", alamat);
                map.put("penempatan", penempatan);
                map.put("jabatan", jabatan);
                map.put("no_hp", noHp);

                if (jabatan.equals("Manager")) {
                    reference.child("TabelManager").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                Toast.makeText(RegisterActivity.this, "Register Berhasil!!", Toast.LENGTH_SHORT).show();
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Gagal!!", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else if (jabatan.equals("Karyawan")) {
                    reference.child("TabelKaryawan").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Gagal!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

        });
    }

}
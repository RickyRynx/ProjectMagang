package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelAbsensiKeluar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AbsensiKeluarActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;

    EditText etNama, etJabatan, etTanggal;
    Calendar myCalendar;
    Button btnUpload, btnAbsenOut;
    private DatabaseReference reference;
    FirebaseDatabase database;

    private static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_keluar);

        etNama = findViewById(R.id.etNama);
        etJabatan = findViewById(R.id.etJbt);
        etTanggal = findViewById(R.id.etTgl);
        btnAbsenOut = findViewById(R.id.btnAbsenKeluar);
        myCalendar = Calendar.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        reference = FirebaseDatabase.getInstance().getReference();


        btnAbsenOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNama.getText().toString();
                String jabatan = etJabatan.getText().toString();
                String tanggal = etTanggal.getText().toString();

                if (nama.isEmpty()) {
                    etNama.setError("Nama tidak boleh kosong");
                    etNama.requestFocus();
                } else if (jabatan.isEmpty()) {
                    etJabatan.setError("Jabatan tidak boleh kosong");
                    etJabatan.requestFocus();
                } else if (tanggal.isEmpty()) {
                    etTanggal.setError("Tanggal Tidak Boleh Kosong");
                    etTanggal.requestFocus();
                } else {
                    absenOut(nama, jabatan, tanggal);
                }
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabelOut();
            }
        };

        etTanggal.setOnClickListener(View -> {
            new DatePickerDialog(AbsensiKeluarActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void absenOut(String nama, String jabatan, String tanggal) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mUser");
        String postId = ref.push().getKey();
        ModelAbsensiKeluar modelAbsensiKeluar = new ModelAbsensiKeluar();
        reference = database.getReference("modelAbsensiKeluar");
        reference.push().setValue(modelAbsensiKeluar);

        HashMap<String, Object> map = new HashMap<>();
        map.put("postid", postId);
        map.put("nama", nama);
        map.put("jabatan", jabatan);
        map.put("tanggal", tanggal);

        reference.child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AbsensiKeluarActivity.this, MainActivity.class);
                    Toast.makeText(AbsensiKeluarActivity.this, "Absensi Berhasil!!", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AbsensiKeluarActivity.this, "Absensi Gagal!!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateLabelOut() {
        String myFormat = "MM/yy/dd EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etTanggal.setText(dateFormat.format(myCalendar.getTime()));
    }

}
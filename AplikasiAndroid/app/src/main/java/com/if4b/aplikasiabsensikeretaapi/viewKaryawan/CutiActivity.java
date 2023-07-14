package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelCuti;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class CutiActivity extends AppCompatActivity {

    EditText etNama, etKeterangan, etMulai, etSelesai;
    String[] jabatan = {"Karyawan"};
    AutoCompleteTextView autoText;
    ArrayAdapter<String> adapterItems;
    Button btnCuti;
    Calendar calendarMulai;
    Calendar calendarSelesai;
    FirebaseDatabase database;
    private DatabaseReference reference;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuti);

        etNama = findViewById(R.id.et_nama_cuti);
        autoText = findViewById(R.id.auto_jabatan_cuti);
        etKeterangan = findViewById(R.id.et_keterangan_cuti);
        etMulai = findViewById(R.id.et_mulai_cuti);
        etSelesai = findViewById(R.id.et_selesai_cuti);
        btnCuti = findViewById(R.id.btn_cuti);
        calendarMulai = Calendar.getInstance();
        calendarSelesai = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_select_dropdown, jabatan);
        autoText.setAdapter(adapterItems);

        autoText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Jabatan: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);

            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            // Terapkan channel pada NotificationManager
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        btnCuti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nama = etNama.getText().toString().trim();
                String jabatan = autoText.getText().toString().trim();
                String keterangan = etKeterangan.getText().toString().trim();
                String mulai = etMulai.getText().toString();
                String selesai = etSelesai.getText().toString();

                if (nama.isEmpty()) {
                    etNama.setError("Nama Tidak Boleh Kosong!!");
                    etNama.requestFocus();
                } else if (keterangan.isEmpty()) {
                    etKeterangan.setError("Keterangan Tidak Boleh Kosong!!");
                    etKeterangan.requestFocus();
                } else if (mulai.isEmpty()) {
                    etKeterangan.setError("Tanggal Mulai Cuti Tidak Boleh Kosong!!");
                    etKeterangan.requestFocus();
                } else if (selesai.isEmpty()) {
                    etKeterangan.setError("Tanggal Selesai Cuti Tidak Boleh Kosong!!");
                    etKeterangan.requestFocus();
                } else {
                    pengajuanCuti(nama, jabatan, keterangan, mulai, selesai);
                }

            }
        });

        DatePickerDialog.OnDateSetListener dateMulai = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarMulai.set(Calendar.YEAR, year);
                calendarMulai.set(Calendar.MONTH, month);
                calendarMulai.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        DatePickerDialog.OnDateSetListener dateSelesai = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendarSelesai.set(Calendar.YEAR, year);
                calendarSelesai.set(Calendar.MONTH, month);
                calendarSelesai.set(Calendar.DAY_OF_MONTH, day);
                updateLabelSelesai();
            }
        };


        etMulai.setOnClickListener(View -> {
            new DatePickerDialog(CutiActivity.this, dateMulai, calendarMulai.get(Calendar.YEAR), calendarMulai.get(Calendar.MONTH), calendarMulai.get(Calendar.DAY_OF_MONTH)).show();
        });

        etSelesai.setOnClickListener(View -> {
            new DatePickerDialog(CutiActivity.this, dateSelesai, calendarSelesai.get(Calendar.YEAR), calendarSelesai.get(Calendar.MONTH), calendarSelesai.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void pengajuanCuti(String nama, String jabatan, String keterangan, String mulai, String selesai) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mUser");
        String postId = ref.push().getKey();
        ModelCuti modelCuti = new ModelCuti();
        reference = database.getReference("TabelPengajuanCuti");
        reference.push().setValue(modelCuti);

        HashMap<String, Object> map = new HashMap<>();
        map.put("nama", nama);
        map.put("jabatan", jabatan);
        map.put("keterangan_cuti", keterangan);
        map.put("tanggal_mulai_cuti", mulai);
        map.put("tanggal_selesai_cuti", selesai);

        reference.child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(CutiActivity.this, DashKaryawanActivity.class);
                    Toast.makeText(CutiActivity.this, "Pengajuan Cuti Berhasil", Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(CutiActivity.this, "notification");
                    builder.setContentTitle("Pengajuan Cuti");
                    builder.setContentText("Pengajuan Cuti Berhasil!!.");
                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setAutoCancel(true);

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CutiActivity.this);
                    if (ActivityCompat.checkSelfPermission(CutiActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    managerCompat.notify(2, builder.build());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }




    private void updateLabel() {
        String myFormat = "MM/yy/dd EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etMulai.setText(dateFormat.format(calendarMulai.getTime()));
    }

    private void updateLabelSelesai() {
        String myFormat = "MM/yy/dd EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etSelesai.setText(dateFormat.format(calendarSelesai.getTime()));
    }
}
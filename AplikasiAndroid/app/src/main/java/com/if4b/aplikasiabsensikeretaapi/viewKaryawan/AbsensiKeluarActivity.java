package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AbsensiKeluarActivity extends AppCompatActivity {

    EditText etNama, etJabatan, etTanggal;
    Calendar myCalendar;
    Button btnAbsenOut;
    private static final String PREFS_NAME = "AbsenKeluarKaryawan";
    private static final String LAST_ABSEN_DATE = "BatasAbsenKeluarKaryawan";
    private DatabaseReference reference;
    FirebaseDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_keluar);

        etNama = findViewById(R.id.etNama);
        etJabatan = findViewById(R.id.etJbt);
        etTanggal = findViewById(R.id.etTgl);
        btnAbsenOut = findViewById(R.id.btnAbsenKeluar);
        myCalendar = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        reference = FirebaseDatabase.getInstance().getReference();


        int jamAbsen = 24;
        if (hour >= jamAbsen) {
            btnAbsenOut.setEnabled(false);
        } else {
            btnAbsenOut.setEnabled(true);
        }



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
                }

                if (isAbsenAllowed()) {
                    // Izinkan absensi
                    absenOut(nama, jabatan, tanggal);
                } else {
                    Toast.makeText(AbsensiKeluarActivity.this, "Anda Sudah Melakukan Absensi Hari Ini!!.", Toast.LENGTH_SHORT).show();
                }

                if (!isAbsenAllowed()) {
                    disableAbsenButton();
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

    private void disableAbsenButton() {
        btnAbsenOut.setEnabled(false);
    }

    private boolean isAbsenAllowed() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String lastAbsenDate = prefs.getString(LAST_ABSEN_DATE, "");

        // Mendapatkan tanggal saat ini
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        String todayDate = dateFormat.format(currentDate);

        // Membandingkan tanggal terakhir kali absen dengan tanggal saat ini
        return !lastAbsenDate.equals(todayDate);
    }

    private void absenOut(String nama, String jabatan, String tanggal) {
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mUser");
        String postId = ref.push().getKey();
        ModelAbsensiKeluar modelAbsensiKeluar = new ModelAbsensiKeluar(postId, nama, jabatan, tanggal);
        reference = database.getReference("TabelAbsensiKeluarKaryawan");
        reference.push().setValue(modelAbsensiKeluar);
        saveLastAbsenDate();

        HashMap<String, Object> map = new HashMap<>();
        map.put("postid", postId);
        map.put("nama", nama);
        map.put("jabatan", jabatan);
        map.put("tanggal", tanggal);

        reference.child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(AbsensiKeluarActivity.this, DashKaryawanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                    Toast.makeText(AbsensiKeluarActivity.this, "Absensi Keluar Berhasil!!", Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(AbsensiKeluarActivity.this, "notification");
                    builder.setContentTitle("Absen Keluar");
                    builder.setContentText("Absen Keluar Berhasil!!.");
                    builder.setSmallIcon(R.drawable.ic_notification);
                    builder.setAutoCancel(true);

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

                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AbsensiKeluarActivity.this);
                    if (ActivityCompat.checkSelfPermission(AbsensiKeluarActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    managerCompat.notify(1, builder.build());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AbsensiKeluarActivity.this, "Absensi Gagal!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLastAbsenDate() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Mendapatkan tanggal saat ini
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date currentDate = Calendar.getInstance().getTime();
        String todayDate = dateFormat.format(currentDate);

        // Simpan tanggal saat ini ke SharedPreferences sebagai tanggal terakhir kali absen
        editor.putString(LAST_ABSEN_DATE, todayDate);
        editor.apply();
    }


    private void updateLabelOut() {
        String myFormat = "MM/yy/dd EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etTanggal.setText(dateFormat.format(myCalendar.getTime()));
    }

}
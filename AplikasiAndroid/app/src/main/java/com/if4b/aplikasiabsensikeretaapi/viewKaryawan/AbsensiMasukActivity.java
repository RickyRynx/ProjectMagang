package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelAbsensiMasuk;
import com.if4b.aplikasiabsensikeretaapi.view.FaceIdActivity;


import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class AbsensiMasukActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText etTanggal;
    Calendar myCalendar;
    ImageView ivAbsen;
    Uri imageUri;
    private StorageReference storageReference;
    private LocationRequest locationRequest;
    private FirebaseStorage storage;
    Button btnUpload, btnAbsen;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    private DatabaseReference reference;
    FirebaseDatabase database;
    FirebaseUser User;
    FirebaseAuth mAuth;
    private GoogleMap myMap;
    MapView mapView;
    private LatLng circleCenter;
    private float circleRadius = 100f;
    private Circle circle;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final String PREFS_NAME = "AbsenMasukKaryawan";
    private static final String LAST_ABSEN_DATE = "BatasAbsenMasukKaryawan";


    private static final int REQUEST_CODE = 100;
    private static final int MY_READ_PERMISSION_CODE = 101;
    private static final float DEFAULT_ZOOM = 18f;
    private static final int CAMERA_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_masuk);

        ivAbsen = findViewById(R.id.iv_absen);
        etTanggal = findViewById(R.id.et_tanggal);
        btnUpload = findViewById(R.id.btnCapture);
        btnAbsen = findViewById(R.id.btnAbsen);
        myCalendar = Calendar.getInstance();
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mapView = findViewById(R.id.map_view_in);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        int jamAbsen = 24;
        if (hour >= jamAbsen) {
            btnAbsen.setEnabled(false);
        } else {
            btnAbsen.setEnabled(true);
        }


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent open_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                startActivityForResult(open_camera, 200);
            }
        });

        btnAbsen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String tanggal = etTanggal.getText().toString();
                String poto = "image_" + System.currentTimeMillis() + ".jpg";

                if (tanggal.isEmpty()) {
                    etTanggal.setError("Tanggal Tidak Boleh Kosong");
                    etTanggal.requestFocus();
                }

                if (isAbsenAllowed()) {
                    // Izinkan absensi
                    absenIn(tanggal, poto);
                } else {
                    Toast.makeText(AbsensiMasukActivity.this, "Anda Sudah Melakukan Absensi Hari Ini!!.", Toast.LENGTH_SHORT).show();
                }

                if (!isAbsenAllowed()) {
                    disableAbsenButton();
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(AbsensiMasukActivity.this, "notification");
                builder.setContentTitle("Absen Masuk");
                builder.setContentText("Absen Masuk Berhasil!!.");
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AbsensiMasukActivity.this);
                if (ActivityCompat.checkSelfPermission(AbsensiMasukActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerCompat.notify(0, builder.build());

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

                checkDistance();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            }
        };

        etTanggal.setOnClickListener(View -> {
            new DatePickerDialog(AbsensiMasukActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
        }

    }

    private void disableAbsenButton() {
        btnAbsen.setEnabled(false);
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



    private void checkDistance() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        float[] results = new float[1];
                        Location.distanceBetween(location.getLatitude(), location.getLongitude(), circleCenter.latitude, circleCenter.longitude, results);

                        float distance = results[0];
                        if (distance <= circleRadius) {
                            // User is within the allowed radius
                            Toast.makeText(AbsensiMasukActivity.this,  "Anda Berada Di Dalam Radius", Toast.LENGTH_SHORT).show();
                        } else {
                            // User is outside the allowed radius
                            Toast.makeText(AbsensiMasukActivity.this, "Anda Berada Di Luar Radius", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
        }
    }


    private void absenIn(String tanggal, String poto) {
        Bitmap bitmap = ((BitmapDrawable) ivAbsen.getDrawable()).getBitmap();
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baOs);
        byte[] imageByte = baOs.toByteArray();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        String postId = ref.push().getKey();
        ModelAbsensiMasuk modelAbsensiMasuk = new ModelAbsensiMasuk();
        StorageReference imageRef = storageReference.child(poto);
        reference = database.getReference("TabelAbsensiMasukKaryawan");
        reference.push().setValue(modelAbsensiMasuk);
        saveLastAbsenDate();



        HashMap<String, Object> map = new HashMap<>();
        map.put("postid", postId);
        map.put("tanggal", tanggal);
        map.put("poto", poto);

        reference.child(postId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                UploadTask uploadTask = imageRef.putBytes(imageByte);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

                                if (task.isSuccessful()) {
                                    Intent intent = new Intent(AbsensiMasukActivity.this, FaceIdActivity.class);
                                    Toast.makeText(AbsensiMasukActivity.this, "Absensi Masuk Berhasil!!", Toast.LENGTH_SHORT).show();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    finish();
                                    NotificationCompat.Builder builder = new NotificationCompat.Builder(AbsensiMasukActivity.this, "notification");
                                    builder.setContentTitle("Absen Masuk");
                                    builder.setContentText("Absen Masuk Berhasil!!.");
                                    builder.setSmallIcon(R.drawable.ic_notification);
                                    builder.setAutoCancel(true);

                                    NotificationManagerCompat managerCompat = NotificationManagerCompat.from(AbsensiMasukActivity.this);
                                    if (ActivityCompat.checkSelfPermission(AbsensiMasukActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    managerCompat.notify(0, builder.build());

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
                                }
                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AbsensiMasukActivity.this, "Absensi Gagal!!", Toast.LENGTH_SHORT).show();
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


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapView.getMapAsync(this);
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Bitmap img = (Bitmap) (data.getExtras().get("data"));
                ivAbsen.setImageBitmap(img);
            }
        }
    }


    private void updateLabel() {
        String myFormat = "MM/yy/dd EEEE";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etTanggal.setText(dateFormat.format(myCalendar.getTime()));
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng location = new LatLng(-2.9952018760828563, 104.77711597303835);
        myMap.addMarker(new MarkerOptions()
                .position(location)
                .title("Marker in Location"));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));

        circleCenter = location;
        circleRadius = 200;
        circle = googleMap.addCircle(new CircleOptions()
                .center(circleCenter)
                .radius(circleRadius)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 255, 0, 0)));
    }

}
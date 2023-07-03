package com.if4b.aplikasiabsensikeretaapi.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.google.maps.android.SphericalUtil;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelAbsensiMasuk;
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.DashKaryawanActivity;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
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


        int jamAbsen = 16;
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
                } else {
                    absenIn(tanggal, poto);
                }

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


    private void absenIn(String tanggal, String poto) {
        Bitmap bitmap = ((BitmapDrawable) ivAbsen.getDrawable()).getBitmap();
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baOs);
        byte[] imageByte = baOs.toByteArray();
        database = FirebaseDatabase.getInstance();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("mUser");
        String postId = ref.push().getKey();
        ModelAbsensiMasuk modelAbsensiMasuk = new ModelAbsensiMasuk();
        StorageReference imageRef = storageReference.child(poto);
        reference = database.getReference("TabelAbsensiMasukKaryawan");
        reference.push().setValue(modelAbsensiMasuk);



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
                            }
                        });
                    }
                });

                if (task.isSuccessful()) {
                    Intent intent = new Intent(AbsensiMasukActivity.this, DashKaryawanActivity.class);
                    Toast.makeText(AbsensiMasukActivity.this, "Absensi Berhasil!!", Toast.LENGTH_SHORT).show();
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(AbsensiMasukActivity.this, "Absensi Gagal!!", Toast.LENGTH_SHORT).show();
            }
        });

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
        circle = googleMap.addCircle(new CircleOptions()
                .center(circleCenter)
                .radius(circleRadius)
                .strokeColor(Color.RED)
                .fillColor(Color.argb(50, 255, 0, 0)));

        LatLngBounds bounds = toBounds(circleCenter, circleRadius);
        googleMap.setLatLngBoundsForCameraTarget(bounds);
    }

    private LatLngBounds toBounds(LatLng center, double radius) {
        double distanceFromCenterToCorner = radius * Math.sqrt(2);
        LatLng southwest = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225);
        LatLng northeast = SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45);
        return new LatLngBounds(southwest, northeast);
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);

        if (ContextCompat.checkSelfPermission(AbsensiMasukActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(AbsensiMasukActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_READ_PERMISSION_CODE);
        }
    }


    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    private LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            if (locationResult == null) {
                return;
            }

            for (Location location : locationResult.getLocations()) {
                if (location != null) {
                    checkLocationInRadius(location);
                }
            }
        }
    };

    public void checkLocationInRadius(Location location) {
        float distance = calculateDistance(location.getLatitude(), location.getLongitude(),
                circleCenter.latitude, circleCenter.longitude);

        if (distance < circleRadius) {
            Toast.makeText(AbsensiMasukActivity.this, "Anda Berada Di Luar Radius Absensi!", Toast.LENGTH_SHORT).show();
            // Nonaktifkan tombol absen atau lakukan tindakan sesuai kebutuhan
        } else {
            String tes = String.valueOf(location.getLongitude());
            Toast.makeText(AbsensiMasukActivity.this, "Anda Berada Di Dalam Radius Absensi", Toast.LENGTH_SHORT).show();
            // Aktifkan tombol absen atau lakukan tindakan sesuai kebutuhan

        }
    }

    private float calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return results[0];
    }


}
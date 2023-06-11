package com.if4b.aplikasiabsensikeretaapi;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.if4b.aplikasiabsensikeretaapi.model.ModelAbsensiMasuk;
import com.if4b.aplikasiabsensikeretaapi.view.MainActivity;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AbsensiMasukActivity extends AppCompatActivity {
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView tvAlamat, tvKota, tvNegara, tvLatitude, tvLongitude;
    EditText etNama, etJabatan, etTanggal;
    Calendar myCalendar;
    ImageView ivAbsen;
    Uri imageUri;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    Button btnUpload, btnGet, btnAbsen;
    ProgressDialog progressDialog;
    Bitmap bitmap;
    private DatabaseReference reference;
    FirebaseDatabase database;


    private static final int REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_absensi_masuk);

        tvAlamat = findViewById(R.id.tv_alamat);
        tvKota = findViewById(R.id.tv_kota);
        tvNegara = findViewById(R.id.tv_negara);
        tvLatitude = findViewById(R.id.tv_latitude);
        tvLongitude = findViewById(R.id.tv_longitude);
        ivAbsen = findViewById(R.id.iv_absen);
        etNama = findViewById(R.id.et_nama);
        etJabatan = findViewById(R.id.et_jabatan);
        etTanggal = findViewById(R.id.et_tanggal);
        btnUpload = findViewById(R.id.btnCapture);
        btnGet = findViewById(R.id.btnUpload);
        btnAbsen = findViewById(R.id.btnAbsen);
        myCalendar = Calendar.getInstance();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        reference = FirebaseDatabase.getInstance().getReference("ModelAbsensiMasuk");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();







        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastLocation();
            }
        });

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
                String nama = etNama.getText().toString();
                String jabatan = etJabatan.getText().toString();
                String tanggal = etTanggal.getText().toString();
                String alamat = tvAlamat.getText().toString();
                String kota = tvKota.getText().toString();
                String negara = tvNegara.getText().toString();
                String latitude = tvLatitude.getText().toString();
                String longitude = tvLongitude.getText().toString();
                String poto = "image_" + System.currentTimeMillis() + ".jpg";

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
                    absenIn(nama, jabatan, tanggal, alamat, kota, negara, latitude, longitude, poto);
                }

            }

        });


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
    }

    private void absenIn(String nama, String jabatan, String tanggal, String alamat, String kota, String negara, String latitude, String longitude, String poto) {
        Bitmap bitmap = ((BitmapDrawable) ivAbsen.getDrawable()).getBitmap();
        ByteArrayOutputStream baOs = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baOs);
        byte[] imageByte = baOs.toByteArray();
        database = FirebaseDatabase.getInstance();
        ModelAbsensiMasuk modelAbsensiMasuk = new ModelAbsensiMasuk();
        StorageReference imageRef = storageReference.child(poto);
        reference = database.getReference("modelAbsensiMasuk");
        reference.push().setValue(modelAbsensiMasuk);


        HashMap<String, Object> map = new HashMap<>();
        map.put("nama", nama);
        map.put("jabatan", jabatan);
        map.put("tanggal", tanggal);
        map.put("poto", poto);
        map.put("alamat",alamat);
        map.put("kota", kota);
        map.put("negara", negara);
        map.put("latitude", latitude);
        map.put("longitude" ,longitude);

        reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                    Intent intent = new Intent(AbsensiMasukActivity.this, MainActivity.class);
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



    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                Geocoder geocoder = new Geocoder(AbsensiMasukActivity.this, Locale.getDefault());
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    tvLatitude.setText("" + addresses.get(0).getLatitude());
                                    tvLongitude.setText("" + addresses.get(0).getLongitude());
                                    tvAlamat.setText(addresses.get(0).getAddressLine(0));
                                    tvKota.setText(addresses.get(0).getLocality());
                                    tvNegara.setText(addresses.get(0).getCountryName());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(AbsensiMasukActivity.this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Required Permission", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

}
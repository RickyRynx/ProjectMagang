package com.if4b.aplikasiabsensikeretaapi.viewKaryawan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelKaryawan;
import com.if4b.aplikasiabsensikeretaapi.model.ModelProfil;
import com.if4b.aplikasiabsensikeretaapi.model.ModelUser;
import com.if4b.aplikasiabsensikeretaapi.view.SettingActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfilActivity extends AppCompatActivity {
    TextView tvNama, tvNipp, tvEmail, tvAlamat, tvJabatan, tvNo, tvTempat;
    ImageView ivprofil, ivBack;
    AppCompatButton btnChange;
    private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAMERA = 2;
    private DatabaseReference reference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private Uri imageUri;
    private String myUri;
    private StorageTask uploadTask;
    private StorageReference profilReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        tvNama = findViewById(R.id.tvNamaProfil);
        tvNipp = findViewById(R.id.tvNipp);
        tvEmail = findViewById(R.id.tvProfilEmail);
        tvJabatan = findViewById(R.id.tvProfilJabatan);
        tvAlamat = findViewById(R.id.tvProfilAlamat);
        tvTempat = findViewById(R.id.tvProfilPenempatan);
        tvNo = findViewById(R.id.tvProfilNo);
        ivprofil = findViewById(R.id.iv_profile_change);
        ivBack = findViewById(R.id.iv_back);
        btnChange = findViewById(R.id.btn_ubah_foto);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelKaryawan").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                    String urlFotoProfil = modelProfil.getUrl_foto_profil();
                    if (urlFotoProfil != null) {
                        Glide.with(ProfilActivity.this)
                                .load(urlFotoProfil)
                                .into(ivprofil);
                    }
                    tvNama.setText("Nama: " + modelProfil.getUsername());
                    tvNipp.setText("NIPP: " + modelProfil.getNipp());
                    tvEmail.setText("Email: " + modelProfil.getEmail());
                    tvJabatan.setText("Jabatan: " + modelProfil.getJabatan());
                    tvAlamat.setText("Alamat: " + modelProfil.getAlamat());
                    tvTempat.setText("Penempatan: " + modelProfil.getPenempatan());
                    tvNo.setText("Nomor HP: " + modelProfil.getNo_hp());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_IMAGE_GALLERY && data != null) {
                imageUri = data.getData();
                ivprofil.setImageURI(imageUri);
                uploadImageToFirebase();
            }
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            profilReference = FirebaseStorage.getInstance().getReference("profil_images/karyawan" + firebaseUser.getUid() + ".jpg");

            // Mengubah gambar menjadi byte array
            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                byte[] imageByteArray = byteArrayOutputStream.toByteArray();

                // Mulai mengunggah gambar ke Firebase Storage
                uploadTask = profilReference.putBytes(imageByteArray);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Jika berhasil mengunggah gambar, dapatkan URL gambar yang diunggah
                        profilReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                myUri = uri.toString();
                                // Simpan URL gambar ke database atau lakukan hal lain yang diperlukan
                                // Misalnya, simpan URL gambar di tabel profil karyawan
                                reference.child("url_foto_profil").setValue(myUri)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Glide.with(ProfilActivity.this)
                                                        .load(myUri)
                                                        .into(ivprofil);
                                                Toast.makeText(ProfilActivity.this, "Foto profil berhasil diunggah!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProfilActivity.this, "Gagal menyimpan URL gambar di database", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfilActivity.this, "Gagal mendapatkan URL gambar yang diunggah", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilActivity.this, "Gagal mengunggah foto profil", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Gagal mengunggah foto profil", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show();
        }
    }

}
package com.if4b.aplikasiabsensikeretaapi.viewManager;

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
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.ProfilActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProfilManagerActivity extends AppCompatActivity {
    TextView tvNama, tvNipp, tvEmail, tvAlamat, tvJabatan, tvNo, tvTempat;
    ImageView ivProfil, ivBack;
    AppCompatButton btnEdit;
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
        setContentView(R.layout.activity_profil_manager);

        tvNama = findViewById(R.id.tvNamaProfilManager);
        tvNipp = findViewById(R.id.tvNippManager);
        tvEmail = findViewById(R.id.tvProfilEmailManager);
        tvJabatan = findViewById(R.id.tvProfilJabatanManager);
        tvAlamat = findViewById(R.id.tvProfilAlamatManager);
        tvTempat = findViewById(R.id.tvProfilPenempatanManager);
        tvNo = findViewById(R.id.tvProfilNoManager);
        ivProfil = findViewById(R.id.iv_profile_manager_change);
        ivBack = findViewById(R.id.iv_back_manager);
        btnEdit = findViewById(R.id.btn_ubah_foto_manager);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilManagerActivity.this, SettingManagerActivity.class);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("TabelManager").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    ModelProfil modelProfil = snapshot.getValue(ModelProfil.class);
                    String urlFotoProfil = modelProfil.getUrl_foto_profil();
                    if (urlFotoProfil != null) {
                        Glide.with(ProfilManagerActivity.this)
                                .load(urlFotoProfil)
                                .into(ivProfil);
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
                ivProfil.setImageURI(imageUri);
                uploadImageToFirebase();
            }
        }
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            profilReference = FirebaseStorage.getInstance().getReference("profil_images/manager" + firebaseUser.getUid() + ".jpg");

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
                                                Glide.with(ProfilManagerActivity.this)
                                                        .load(myUri)
                                                        .into(ivProfil);
                                                Toast.makeText(ProfilManagerActivity.this, "Foto profil berhasil diunggah!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ProfilManagerActivity.this, "Gagal menyimpan URL gambar di database", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfilManagerActivity.this, "Gagal mendapatkan URL gambar yang diunggah", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilManagerActivity.this, "Gagal mengunggah foto profil", Toast.LENGTH_SHORT).show();
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
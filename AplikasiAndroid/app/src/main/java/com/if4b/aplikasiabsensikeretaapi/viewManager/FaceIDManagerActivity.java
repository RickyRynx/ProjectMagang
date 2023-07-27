package com.if4b.aplikasiabsensikeretaapi.viewManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.core.Mat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.viewKaryawan.DashKaryawanActivity;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class FaceIDManagerActivity extends CameraActivity {
    private static String LOGTAG = "OpenCV_Log";
    private CameraBridgeViewBase mOpenCameraView;
    private CascadeClassifier cascadeClassifier;
    private Mat grayscaleImage;
    Button btnVerif;
    private DatabaseReference reference;
    FirebaseDatabase database;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    ProgressDialog progressDialog;


    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.v(LOGTAG, "OpenCV Loaded");
                    loadCascadeClassifier();
                    mOpenCameraView.enableView();
                }break;
                default: {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };

    private void loadCascadeClassifier() {
        try {
            InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_default);
            File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
            File cascadeFile = new File(cascadeDir, "haarcascade_frontalface_default");
            FileOutputStream os = new FileOutputStream(cascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
            cascadeClassifier = new CascadeClassifier(cascadeFile.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_id);

        mOpenCameraView = (CameraBridgeViewBase) findViewById(R.id.camera_view);
        mOpenCameraView.setVisibility(SurfaceView.VISIBLE);
        mOpenCameraView.setCvCameraViewListener(cvCameraViewListener);
        mOpenCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);

        reference = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        progressDialog = new ProgressDialog(this);
        btnVerif = findViewById(R.id.btn_face_verifikasi);

        btnVerif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();


                NotificationCompat.Builder builder = new NotificationCompat.Builder(FaceIDManagerActivity.this, "notification");
                builder.setContentTitle("Absen Masuk");
                builder.setContentText("Verifikasi Wajah Berhasil!!.");
                builder.setSmallIcon(R.drawable.ic_notification);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(FaceIDManagerActivity.this);
                if (ActivityCompat.checkSelfPermission(FaceIDManagerActivity.this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                managerCompat.notify(4, builder.build());

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
        });
    }

    private void takePhoto() {
        mOpenCameraView.disableView();
        progressDialog.setMessage("Memproses...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Bitmap photoBitmap = Bitmap.createBitmap(grayscaleImage.cols(), grayscaleImage.rows(), Bitmap.Config.ARGB_8888);
        org.opencv.android.Utils.matToBitmap(grayscaleImage, photoBitmap);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] photoData = baos.toByteArray();

        String fileName = "face_photo_" + System.currentTimeMillis() + ".jpg";
        StorageReference photoRef = storageReference.child("photos/" + fileName);

        UploadTask uploadTask = photoRef.putBytes(photoData);
        uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            // Simpan URL download ke Firebase Realtime Database atau lakukan tindakan sesuai kebutuhan
                            Toast.makeText(FaceIDManagerActivity.this, "Foto berhasil diunggah", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FaceIDManagerActivity.this, DashManagerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(FaceIDManagerActivity.this, "Gagal mendapatkan URL foto", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(FaceIDManagerActivity.this, "Gagal mengunggah foto", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();
            }
        });

    }


    @Override
    protected List<?extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mOpenCameraView);
    }

    private CameraBridgeViewBase.CvCameraViewListener2 cvCameraViewListener = new CameraBridgeViewBase.CvCameraViewListener2() {
        @Override
        public List<? extends CameraBridgeViewBase> getCameraViewList() {
            return null;
        }

        @Override
        public void onCameraViewStarted(int width, int height) {
            if (width > 0 && height > 0) {
                grayscaleImage = new Mat();
            }
        }

        @Override
        public void onCameraViewStopped() {
            if (grayscaleImage != null) {
                grayscaleImage.release();
            }
        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            Mat frame = inputFrame.gray();
            if (frame.cols() > 0 && frame.rows() > 0) {
                grayscaleImage = frame.clone();
                MatOfRect faces = new MatOfRect();

                if (cascadeClassifier != null) {
                    cascadeClassifier.detectMultiScale(grayscaleImage, faces, 1.1, 3, 0,
                            new Size(grayscaleImage.cols() * 0.2, grayscaleImage.rows() * 0.2),
                            new Size(grayscaleImage.cols() * 0.8, grayscaleImage.rows() * 0.8));
                }

                Rect[] facesArray = faces.toArray();
                for (org.opencv.core.Rect face : facesArray) {
                    Imgproc.rectangle(grayscaleImage, face.tl(), face.br(), new Scalar(0, 255, 0), 4);
                }

                if (frame.empty() || frame.cols() == 0 || frame.rows() == 0) {
                    return grayscaleImage;
                }

            }

            return grayscaleImage;

        }


    };





    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(LOGTAG, "Open Camera Tidak Terdeteksi, Inisialisasi");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mOpenCameraView != null) {
            mOpenCameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mOpenCameraView != null) {
            mOpenCameraView.disableView();
        }
    }
}

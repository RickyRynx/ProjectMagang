package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.if4b.aplikasiabsensikeretaapi.R;

public class PrivacyActivity extends AppCompatActivity {
    TextView tvKebijakan, tvDescKebijakan, tvPengumpulan, tvDescPengumpulan, tvPenggunaan, tvDescPenggunaan,
             tvBagikan, tvDescBagikan, tvKeamanan, tvDescKeamanan, tvIzin, tvDescIzin, tvCookie, tvDescCookie,
             tvKontak, tvDescKontak, tvPembaruan, tvDescPembaruan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        tvKebijakan = findViewById(R.id.tv_kebijakan);
        tvDescKebijakan = findViewById(R.id.tv_desc_kebijakan);
        tvPengumpulan = findViewById(R.id.tv_pengumpulan);
        tvDescPengumpulan = findViewById(R.id.tv_desc_pengumpulan);
        tvPenggunaan = findViewById(R.id.tv_penggunaan);
        tvDescPenggunaan = findViewById(R.id.tv_desc_penggunaan);
        tvBagikan = findViewById(R.id.tv_bagikan);
        tvDescBagikan = findViewById(R.id.tv_desc_bagikan);
        tvKeamanan = findViewById(R.id.tv_keamanan);
        tvDescKeamanan = findViewById(R.id.tv_desc_keamanan);
        tvIzin = findViewById(R.id.tv_izin);
        tvDescIzin = findViewById(R.id.tv_desc_izin);
        tvCookie = findViewById(R.id.tv_cookie);
        tvDescCookie = findViewById(R.id.tv_desc_cookie);
        tvKontak = findViewById(R.id.tv_kontak);
        tvDescKontak = findViewById(R.id.tv_desc_kontak);
        tvPembaruan = findViewById(R.id.tv_pembaruan);
        tvDescPembaruan = findViewById(R.id.tv_desc_pembaruan);

        tvKebijakan.setText("KEBIJAKAN DAN KEAMANAN APLIKASI ABSENSI PT KERETA API INDONESIA");
        tvDescKebijakan.setText("Aplikasi Absensi PT Kereta Api Indonesia sangat menghargai privasi dan keamanan data pengguna. Dalam deskripsi ini, kami menjelaskan bagaimana kami mengumpulkan, menggunakan, dan melindungi informasi pribadi Anda ketika menggunakan aplikasi kami. Harap periksa deskripsi ini secara seksama sebelum menggunakan aplikasi kami.");
        tvPengumpulan.setText("PENGUMPULAN DATA");
        tvDescPengumpulan.setText("Aplikasi Absensi Kereta Api mengumpulkan informasi yang Anda berikan secara sukarela saat menggunakan aplikasi kami, seperti nama, alamat email, nipp, nomor hp, dan foto profil. Selain itu, kami juga dapat mengumpulkan informasi non-pribadi, seperti data penggunaan aplikasi dan statistik serta lokasi pengguna.");
        tvPenggunaan.setText("PENGGUNAAN DATA");
        tvDescPenggunaan.setText("Informasi yang kami kumpulkan digunakan untuk menyediakan layanan dan fitur aplikasi kami. Kami dapat menggunakan alamat email untuk mengirimkan pembaruan terkini tentang aplikasi atau untuk memberi tahu Anda tentang info terbaru serta fitur terbaru. Data penggunaan aplikasi kami juga dapat digunakan untuk memahami bagaimana aplikasi kami digunakan dan untuk meningkatkan pengalaman pengguna dan sebagai project pengembangan aplikasi magang di PT Kereta Api Indonesia khususnya kantor DIVRE III Palembang.");
        tvBagikan.setText("BAGIKAN DATA");
        tvDescBagikan.setText("Kami tidak akan membagikan informasi pribadi Anda dengan pihak ketiga tanpa izin Anda, kecuali jika diharuskan oleh hukum. Namun, kami dapat menggunakan pihak ketiga untuk menyediakan layanan atau fitur tertentu dalam aplikasi kami, seperti penyimpanan data di Firebase Realtime Database dan cloud storage.");
        tvKeamanan.setText("KEAMANAN DATA");
        tvDescKeamanan.setText("Kami menjaga keamanan data Anda dengan menerapkan langkah-langkah yang sesuai untuk melindungi data dari akses, penggunaan, atau pengungkapan yang tidak sah. Data pribadi Anda disimpan dalam server yang aman dan dilindungi dengan enkripsi dan verifikasi melalui admin.");
        tvIzin.setText("IZIN APLIKASI");
        tvDescIzin.setText("Aplikasi Absensi PT Kereta Api Indonesia meminta izin untuk mengakses kamera dan penyimpanan perangkat Anda. Izin ini diperlukan untuk mengambil foto, menyimpan hasil pemindaian wajah Anda di galeri perangkat, izin lokasi pengguna, serta data kepegawaian guna memberikan informasi yang sesuai.");
        tvCookie.setText("KEBIJAKAN COOKIE");
        tvDescCookie.setText("Aplikasi Absensi PT Kereta Api Indonesia tidak menggunakan cookie atau teknologi pelacakan serupa.");
        tvKontak.setText("KONTAK PENGEMBANG");
        tvDescKontak.setText("Jika Anda memiliki pertanyaan atau kekhawatiran tentang privasi dan keamanan aplikasi kami, silakan hubungi kami melalui:\nEmail: ricky12112002@gmail.com atau rickyariekurniawan@mhs.mdp.ac.id\nNomor HP: 085609249288\nGithub: https://github.com/RickyRynx.");
        tvPembaruan.setText("PEMBARUAN KEBIJAKAN");
        tvDescPembaruan.setText("Deskripsi privasi dan keamanan kami dapat diperbarui dari waktu ke waktu. Perubahan signifikan dalam kebijakan akan diberitahukan kepada Anda melalui pembaruan aplikasi atau melalui email.");
    }
}
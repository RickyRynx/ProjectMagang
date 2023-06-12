package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelAbsensiKeluar {
    String nama, jabatan, tanggal, alamat, kota, negara, latitude, longitude, postId;

    public ModelAbsensiKeluar() {

    }

    public ModelAbsensiKeluar(String nama, String jabatan, String tanggal, String alamat, String kota, String negara, String latitude, String longitude, String postId) {
        this.nama = nama;
        this.jabatan = jabatan;
        this.tanggal = tanggal;
        this.alamat = alamat;
        this.kota = kota;
        this.negara = negara;
        this.latitude = latitude;
        this.longitude = longitude;
        this.postId = postId;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}

package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelAbsensiMasuk {

    private String nama;
    private String tanggal;
    private String jamMasuk;
    private String jamKeluar;
    private String jabatan;
    private String lokasi;
    private String kota;
    private String negara;
    private String latitude;
    private String longitude;
    private String poto;

    public ModelAbsensiMasuk() {
        this.nama = this.nama;
        this.tanggal = this.tanggal;
        this.poto = this.poto;
        this.jamMasuk = jamMasuk;
        this.jamKeluar = jamKeluar;
        this.jabatan = this.jabatan;
        this.lokasi = lokasi;
        this.kota = this.kota;
        this.negara = this.negara;
        this.latitude = this.latitude;
        this.longitude = this.longitude;
    }


    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPoto() {
        return poto;
    }

    public void setPoto(String poto) {
        this.poto = poto;
    }

    public String getJamMasuk() {
        return jamMasuk;
    }

    public void setJamMasuk(String jamMasuk) {
        this.jamMasuk = jamMasuk;
    }

    public String getJamKeluar() {
        return jamKeluar;
    }

    public void setJamKeluar(String jamKeluar) {
        this.jamKeluar = jamKeluar;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
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

}

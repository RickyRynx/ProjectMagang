package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelAbsensi {

    private String nama;
    private String tanggal;
    private String jamMasuk;
    private String jamKeluar;

    public ModelAbsensi(String nama, String tanggal, String jamMasuk, String jamKeluar) {
        this.nama = nama;
        this.tanggal = tanggal;
        this.jamMasuk = jamMasuk;
        this.jamKeluar = jamKeluar;
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

    @Override
    public String toString() {
        return "Absensi{" +
                "nama='" + nama + '\'' +
                ", tanggal='" + tanggal + '\'' +
                ", jamMasuk='" + jamMasuk + '\'' +
                ", jamKeluar='" + jamKeluar + '\'' +
                '}';
    }

}

package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelAbsensiKeluarManager {
    String nama, jabatan, tanggal;

    public ModelAbsensiKeluarManager() {
    }

    public ModelAbsensiKeluarManager(String nama, String jabatan, String tanggal) {
        this.nama = nama;
        this.jabatan = jabatan;
        this.tanggal = tanggal;
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
}

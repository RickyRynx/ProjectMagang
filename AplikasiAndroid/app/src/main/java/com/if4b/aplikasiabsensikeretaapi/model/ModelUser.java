package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelUser {
    String username, password, konfirmasi, alamat, jabatan, nomor;

    public ModelUser() {
    }

    public ModelUser(String username, String password, String konfirmasi, String alamat, String jabatan, String nomor) {
        this.username = username;
        this.password = password;
        this.konfirmasi = konfirmasi;
        this.alamat = alamat;
        this.jabatan = jabatan;
        this.nomor = nomor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKonfirmasi() {
        return konfirmasi;
    }

    public void setKonfirmasi(String konfirmasi) {
        this.konfirmasi = konfirmasi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }
}

package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelManager{
    String username, email, password, konfirmasi, alamat, jabatan, nomor, postId, idUser;

    public ModelManager() {
    }


    public ModelManager(String postId, String idUser,String username, String email, String password, String konfirmasi, String alamat, String jabatan, String nomor) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.konfirmasi = konfirmasi;
        this.alamat = alamat;
        this.jabatan = jabatan;
        this.nomor = nomor;
        this.postId = postId;
        this.idUser = idUser;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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


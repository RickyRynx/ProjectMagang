package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelProfil {


        private String username;
        private String nipp;
        private String email;
        private String password;
        private String konfirmasi_password;
        private String alamat;
        private String penempatan;
        private String jabatan;
        private String no_hp;
        String url_foto_profil;

        public ModelProfil() {
        }

    public ModelProfil(String username, String nipp, String email, String password, String konfirmasi_password, String alamat, String penempatan, String jabatan, String no_hp, String url_foto_profil) {
        this.username = username;
        this.nipp = nipp;
        this.email = email;
        this.password = password;
        this.konfirmasi_password = konfirmasi_password;
        this.alamat = alamat;
        this.penempatan = penempatan;
        this.jabatan = jabatan;
        this.no_hp = no_hp;
        this.url_foto_profil = url_foto_profil;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNipp() {
        return nipp;
    }

    public void setNipp(String nipp) {
        this.nipp = nipp;
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

    public String getKonfirmasi_password() {
        return konfirmasi_password;
    }

    public void setKonfirmasi_password(String konfirmasi_password) {
        this.konfirmasi_password = konfirmasi_password;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPenempatan() {
        return penempatan;
    }

    public void setPenempatan(String penempatan) {
        this.penempatan = penempatan;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }

    public String getUrl_foto_profil() {
        return url_foto_profil;
    }

    public void setUrl_foto_profil(String url_foto_profil) {
        this.url_foto_profil = url_foto_profil;
    }
}

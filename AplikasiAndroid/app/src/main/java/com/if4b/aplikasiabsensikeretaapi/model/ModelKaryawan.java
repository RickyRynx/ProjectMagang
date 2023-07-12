package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelKaryawan {

        String username, email, password, konfirmasi, alamat, jabatan, nomor_hp, postId;

        public ModelKaryawan() {
        }


        public ModelKaryawan(String postId, String idUser,String username, String email, String password, String konfirmasi, String alamat, String jabatan, String nomor_hp) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.konfirmasi = konfirmasi;
            this.alamat = alamat;
            this.jabatan = jabatan;
            this.nomor_hp = nomor_hp;
            this.postId = postId;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
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

        public String getNomor_hp() {
            return nomor_hp;
        }

        public void setNomor_hp(String nomor) {
            this.nomor_hp = nomor;
        }
    }




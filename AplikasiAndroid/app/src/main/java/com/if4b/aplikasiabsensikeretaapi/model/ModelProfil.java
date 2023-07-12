package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelProfil {


        private String username;
        private String email;
        private String password;
        private String konfirmasi;
        private String alamat;
        private String jabatan;
        private String no_hp;

        public ModelProfil() {
        }


        public ModelProfil(String username, String email, String password, String konfirmasi, String alamat, String jabatan, String no_hp) {
            this.username = username;
            this.email = email;
            this.password = password;
            this.konfirmasi = konfirmasi;
            this.alamat = alamat;
            this.jabatan = jabatan;
            this.no_hp = no_hp;
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

        public String getNoHp() {
            return no_hp;
        }

        public void setNoHp(String no_hp) {
            this.no_hp = no_hp;
        }
}

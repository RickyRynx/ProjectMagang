package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelCuti {
    private String nama;
    private String jabatan;
    private String keteranganCuti;
    private String mulai;
    private String selesai;

    public ModelCuti() {
        this.nama = nama;
        this.jabatan = jabatan;
        this.keteranganCuti = keteranganCuti;
        this.mulai = mulai;
        this.selesai = selesai;
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

    public String getKeteranganCuti() {
        return keteranganCuti;
    }

    public void setKeteranganCuti(String keteranganCuti) {
        this.keteranganCuti = keteranganCuti;
    }

    public String getMulai() {
        return mulai;
    }

    public void setMulai(String mulai) {
        this.mulai = mulai;
    }

    public String getSelesai() {
        return selesai;
    }

    public void setSelesai(String selesai) {
        this.selesai = selesai;
    }
}

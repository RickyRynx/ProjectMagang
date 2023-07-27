package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelRekapCuti {
    private String rekapId;
    private String nama;
    private String jabatan;
    private String keterangan_cuti;
    private String tanggal_mulai_cuti;
    private String tanggal_selesai_cuti;

    public ModelRekapCuti() {
    }

    public ModelRekapCuti(String rekapId, String nama, String jabatan, String keterangan_cuti, String tanggal_mulai_cuti, String tanggal_selesai_cuti) {
        this.rekapId = rekapId;
        this.nama = nama;
        this.jabatan = jabatan;
        this.keterangan_cuti = keterangan_cuti;
        this.tanggal_mulai_cuti = tanggal_mulai_cuti;
        this.tanggal_selesai_cuti = tanggal_selesai_cuti;
    }

    public String getRekapId() {
        return rekapId;
    }

    public void setRekapId(String rekapId) {
        this.rekapId = rekapId;
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

    public String getKeterangan_cuti() {
        return keterangan_cuti;
    }

    public void setKeterangan_cuti(String keterangan_cuti) {
        this.keterangan_cuti = keterangan_cuti;
    }

    public String getTanggal_mulai_cuti() {
        return tanggal_mulai_cuti;
    }

    public void setTanggal_mulai_cuti(String tanggal_mulai_cuti) {
        this.tanggal_mulai_cuti = tanggal_mulai_cuti;
    }

    public String getTanggal_selesai_cuti() {
        return tanggal_selesai_cuti;
    }

    public void setTanggal_selesai_cuti(String tanggal_selesai_cuti) {
        this.tanggal_selesai_cuti = tanggal_selesai_cuti;
    }
}

package com.if4b.aplikasiabsensikeretaapi.model;

public class ModelScanData {
    private String status;
    private String imageUrl;

    public ModelScanData() {

    }

    public ModelScanData(String status, String imageUrl) {
        this.status = status;
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

package com.example.aclgyro.model;

import java.util.ArrayList;

public class User {
    private String namaLengkap;
    private int umur;
    private int jenisKelamin; // 0=L, 1=P
    private ArrayList<DataSensor> dataSensors;

    public User() {
    }

    public User(String namaLengkap, int umur, int jenisKelamin) {
        this.namaLengkap = namaLengkap;
        this.umur = umur;
        this.jenisKelamin = jenisKelamin;
    }

    public String getNamaLengkap() {
        return namaLengkap;
    }

    public void setNamaLengkap(String namaLengkap) {
        this.namaLengkap = namaLengkap;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }

    public int getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(int jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }
}

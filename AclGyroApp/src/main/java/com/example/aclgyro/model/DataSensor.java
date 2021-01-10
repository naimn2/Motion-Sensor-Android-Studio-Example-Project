package com.example.aclgyro.model;

import java.util.ArrayList;

public class DataSensor {
    public static final String DATABASE_REFERENCE = "dataSensors";

    private int jenisSensor; // 0 = Acl, 1 = Gyro
    private long startTime;
    private long endTime;
    private ArrayList<Coordinate> coordinates;
    private boolean isFallen;

    public DataSensor() {
    }

    public DataSensor(int jenisSensor, long startTime, long endTime, ArrayList<Coordinate> coordinates, boolean isFallen) {
        this.jenisSensor = jenisSensor;
        this.startTime = startTime;
        this.endTime = endTime;
        this.coordinates = coordinates;
        this.isFallen = isFallen;
    }

    public int getJenisSensor() {
        return jenisSensor;
    }

    public void setJenisSensor(int jenisSensor) {
        this.jenisSensor = jenisSensor;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getendTime() {
        return endTime;
    }

    public void setendTime(long endTime) {
        this.endTime = endTime;
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public void setCoordinate(ArrayList<Coordinate> coordinates) {
        this.coordinates = coordinates;
    }

    public boolean isFallen() {
        return isFallen;
    }

    public void setFallen(boolean fallen) {
        isFallen = fallen;
    }
}

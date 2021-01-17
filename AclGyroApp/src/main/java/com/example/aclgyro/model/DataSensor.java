package com.example.aclgyro.model;

import java.util.ArrayList;

public class DataSensor {
	public static final String DATABASE_REFERENCE = "dataSensors";
	public static final String[] JENIS_JENIS_AKTIVITAS = {"NORMAL", "JATUH"};

	private int jenisSensor; // 0 = Acl, 1 = Gyro, 2 = both
	private long startTime;
	private long endTime;
	private ArrayList<Coordinate> aclCoords;
	private ArrayList<Coordinate> gyroCoords;
	private int label; // 0 = Normal, 1 = Jatuh

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public void setJenisSensor(int jenisSensor) {
		this.jenisSensor = jenisSensor;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public void setAclCoords(ArrayList<Coordinate> aclCoords) {
		this.aclCoords = aclCoords;
	}

	public void setGyroCoords(ArrayList<Coordinate> gyroCoords) {
		this.gyroCoords = gyroCoords;
	}

	public ArrayList<Coordinate> getAclCoords(){
		return aclCoords;
	}

	public int getJenisSensor(){
		return jenisSensor;
	}

	public long getStartTime(){
		return startTime;
	}

	public long getEndTime(){
		return endTime;
	}

	public ArrayList<Coordinate> getGyroCoords(){
		return gyroCoords;
	}
}
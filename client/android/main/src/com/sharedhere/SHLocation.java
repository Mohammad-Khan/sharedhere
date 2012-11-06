package com.sharedhere;

public class SHLocation {
	
	private double latitude;
	private double longitude;
	private int radius;
	
	public SHLocation(double lat, double lon, int rad) {
		latitude = lat;
		longitude = lon;
		radius = rad;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	
}

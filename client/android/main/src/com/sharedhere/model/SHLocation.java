package com.sharedhere.model;

public class SHLocation {
	
	private double latitude;
	private double longitude;
	
	public SHLocation(double lat, double lon) {
		latitude = lat;
		longitude = lon;
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
}

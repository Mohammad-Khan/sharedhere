package com.sharedhere.model;

import java.io.Serializable;

import android.location.Location;

import com.google.android.maps.GeoPoint;

public class SHLocation implements Serializable {

	private static final long serialVersionUID = 1L;
	private double latitude;
	private double longitude;

	public SHLocation(Location location) {
		this.latitude = location.getLatitude();
		this.longitude = location.getLongitude();
	}

	public SHLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public final GeoPoint toGeoPoint() {
		return new GeoPoint((int) (this.latitude * 1E6),
				(int) (this.longitude * 1E6));
	}

	public String toString() {
		return String.valueOf(this.latitude) + ", "
				+ String.valueOf(this.longitude);
	}
}

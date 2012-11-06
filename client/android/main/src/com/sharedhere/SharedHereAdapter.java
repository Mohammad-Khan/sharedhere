package com.sharedhere;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SharedHereAdapter extends MapActivity
{
	
	private MapView mapView = null;
	private MapController mapController = null;

	private Location location = null;
	private LocationManager locManager = null;
	private LocationListener locListener = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// get the Points of Interest
		SHClientServer cs = new SHClientServer();
		//List<GeoPoint> points = cs.getPoi();
		//cs.getPoi();
		
		//SHLocation l = new SHLocation(41.23430739, -81.2343922, 10);
		//JSONArray jArray = cs.listContent(l);
		
		//cs.download("droid.jpg");
		//cs.upload("/mnt/sdcard/droid_img.bmp");
		//cs.upload("/mnt/sdcard/index.txt");
		
		//initTracking();
		//initMapView(location.getLatitude(), location.getLongitude());
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	
	/**
	 * 
	 */
	private void initTracking() {
		locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (locListener == null)
			locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());
				Log.e("GPS", "location changed: lat="+lat+", lon="+lon);

				location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				mapController.setCenter(new LatLonPoint(location.getLatitude(), location.getLongitude()));
				mapView.invalidate();
			}
			public void onStatusChanged(String provider, int status, Bundle extras) {	
			}
			public void onProviderEnabled(String provider) {	
			}
			public void onProviderDisabled(String provider) {
			}
		};

		locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
	}

	/**
	 * 
	 */
	private void initMapView(double latitude, double longitude) {
		Log.e("GPS_initmapview", "lat="+latitude+", lon="+longitude);
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		//mapView.setSatellite(true);

		mapController = mapView.getController();
		mapController.setZoom(18);
		mapController.setCenter(new LatLonPoint(latitude, longitude));
	}

	/**
	 * 
	 * @param view
	 */
	public void onClickRefresh(final View view) {
		updateView();
	}

	/**
	 * 
	 */
	public void updateView() {
		final TextView valueView = (TextView) findViewById(R.id.textview_gps);
		valueView.setText(Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()));    
		mapController.setCenter(new LatLonPoint(location.getLatitude(), location.getLongitude()));
	}
}
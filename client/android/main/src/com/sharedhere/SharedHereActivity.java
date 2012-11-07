package com.sharedhere;

import java.util.List;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import com.sharedhere.model.LatLonPoint;
import com.sharedhere.model.SHClientServer;
import com.sharedhere.model.SHLocation;

public class SharedHereActivity extends MapActivity
{
	private MapView mapView = null;
	private MapController mapController = null;

	private Location location = null;
	private LocationManager locationManager = null;
	private LocationListener locationListener = null;
	private String locationProvider = null;
	private Criteria locationCriteria = null;
	
	private SHClientServer shConnection = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		//shConnection = new SHClientServer(findViewById(R.string.server_address).toString());
		shConnection = new SHClientServer("http://10.0.0.240/sharedhere/");
		//List<GeoPoint> points = cs.getPoi();
				//cs.getPoi();

				//SHLocation l = new SHLocation(41.23430739, -81.2343922, 10);
				//JSONArray jArray = cs.listContent(l);

				//cs.download("droid.jpg");
				//cs.upload("/mnt/sdcard/droid_img.bmp");
				//cs.upload("/mnt/sdcard/index.txt");
		initTracking();
		initMapView(location.getLatitude(), location.getLongitude());
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
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationCriteria = new Criteria();
		locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
		locationProvider = locationManager.getBestProvider(locationCriteria, true);

		Log.d("GPS", "bestprovider: "+locationProvider);

		location = locationManager.getLastKnownLocation(locationProvider);

		if (locationListener == null)
			locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					String lat = String.valueOf(location.getLatitude());
					String lon = String.valueOf(location.getLongitude());
					
					Log.d("GPS", "location changed: lat="+lat+", lon="+lon);
	
					location = locationManager.getLastKnownLocation(locationProvider);
	
					mapController.animateTo(new LatLonPoint(location.getLatitude(), location.getLongitude()));
					mapView.invalidate();
				}
				public void onStatusChanged(String provider, int status, Bundle extras) {       
				}
				public void onProviderEnabled(String provider) {        
				}
				public void onProviderDisabled(String provider) {
				}
		};

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, locationListener);
	}

	/**
	 * 
	 */
	private void initMapView(double latitude, double longitude) {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(18);
		//mapView.setSatellite(true);
		
		mapController.setCenter(new LatLonPoint(latitude, longitude));
		mapView.invalidate();
	}

	/**
	 * 
	 * @param view
	 */
	public void onClickCheckHere(final View view) {	
		List<SHLocation> locations = shConnection.getPoi();
		for (SHLocation l: locations) {
			Log.i("POI", "lat: " + l.getLatitude() + " lon:" + l.getLongitude());
		}
		
		updateView();
	}

	/**
	 * 
	 * @param view
	 */
	public void onClickShareHere(final View view) {
		//List<GeoPoint> points = cs.getPoi();
		//cs.getPoi();

		//SHLocation l = new SHLocation(41.23430739, -81.2343922, 10);
		//JSONArray jArray = cs.listContent(l);

		//cs.download("droid.jpg");
		
		//shConnection.upload("/mnt/sdcard/dont_panic.jpg", location.getLatitude(), location.getLongitude());
		
		updateView();
	}

	/**
	 * 
	 */
	public void updateView() {
		final TextView valueView = (TextView) findViewById(R.id.textview_gps);
		if (location != null) {
			valueView.setText(Double.toString(location.getLatitude()) + "," + Double.toString(location.getLongitude()));    
			mapController.setCenter(new LatLonPoint(location.getLatitude(), location.getLongitude()));
		}
	}
}
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

	private Location locationCurrent = null;
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
		//initMapView(location.getLatitude(), location.getLongitude());
		initMapView();
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
		locationCurrent = locationManager.getLastKnownLocation(locationProvider);

		Log.d("GPS", "bestprovider: "+locationProvider);
		
		if (locationListener == null)
			locationListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					String lat = String.valueOf(location.getLatitude());
					String lon = String.valueOf(location.getLongitude());
				
					Log.d("GPS", "location changed: lat="+lat+", lon="+lon);

					locationCurrent = location;
					mapController.animateTo(new LatLonPoint(location.getLatitude(), location.getLongitude()));
					mapView.invalidate();
					
					updateView();
				}
				public void onStatusChanged(String provider, int status, Bundle extras) {       
				}
				public void onProviderEnabled(String provider) {        
				}
				public void onProviderDisabled(String provider) {
				}
		};

		locationManager.requestLocationUpdates(locationProvider, 5000, 5, locationListener);
	}

	/**
	 * 
	 */
//	private void initMapView(double latitude, double longitude) {
	private void initMapView() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(18);
		//mapView.setSatellite(true);
		
		//mapController.setCenter(new LatLonPoint(latitude, longitude));
		//mapView.invalidate();
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
	}

	/**
	 * 
	 * @param view
	 */
	public void onClickShareHere(final View view) {
		//List<GeoPoint> points = cs.getPoi();
		//cs.getPoi();

		//JSONArray jArray = cs.listContent(l);

		//cs.download("droid.jpg");
		
		// Lars: testing upload
		//shConnection.upload("/mnt/sdcard/dont_panic.jpg", locationCurrent.getLatitude(), locationCurrent.getLongitude());
		//shConnection.upload("/mnt/sdcard/ultimate_trick.jpg", locationCurrent.getLatitude(), locationCurrent.getLongitude());
		shConnection.upload("/mnt/sdcard/bsdrest.gif", locationCurrent.getLatitude(), locationCurrent.getLongitude());
		//shConnection.upload("/mnt/sdcard/tia_logo_large.jpg", locationCurrent.getLatitude(), locationCurrent.getLongitude());
		//shConnection.upload("/mnt/sdcard/etmessage_nrao.jpg", locationCurrent.getLatitude(), locationCurrent.getLongitude());
	}

	/**
	 * 
	 */
	public void updateView() {
		final TextView valueView = (TextView) findViewById(R.id.textview_gps);
		if (locationCurrent != null) {
			valueView.setText(Double.toString(locationCurrent.getLatitude()) + "," + Double.toString(locationCurrent.getLongitude()));    
			mapController.setCenter(new LatLonPoint(locationCurrent.getLatitude(), locationCurrent.getLongitude()));
		}
	}
}
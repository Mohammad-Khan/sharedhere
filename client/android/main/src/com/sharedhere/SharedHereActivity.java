package com.sharedhere;

import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import com.sharedhere.model.SHClientServer;
import com.sharedhere.model.SHItemizedOverlay;
import com.sharedhere.model.SHLocation;

public class SharedHereActivity extends MapActivity
{
	private MapView mapView = null;
	private MapController mapController = null;
	private List<Overlay> mapOverlays = null;

	private LocationManager locationManager = null;
	private LocationListener locationListener = null;
	private String locationProvider = null;
	private Criteria locationCriteria = null;

	private SHClientServer shConnection = null;
	private SHItemizedOverlay shOverlay = null;
	private SHLocation shCurrentLocation = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		shConnection = new SHClientServer(getString(R.string.server_address));
		

		initTracking();
		initMapView();
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	/**
	 * Initialized gps tracking
	 * 
	 * @author Lars Lindgren <chrono@eeky.net>
	 * 
	 * TODO: Move code?
	 */
	private void initTracking() {
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationCriteria = new Criteria();
		locationCriteria.setAccuracy(Criteria.ACCURACY_COARSE);
		locationProvider = locationManager.getBestProvider(locationCriteria, true);
		shCurrentLocation = new SHLocation(locationManager.getLastKnownLocation(locationProvider));

		Log.d("initTracking", "bestprovider: "+locationProvider);

		if (locationListener == null)
			locationListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				String lat = String.valueOf(location.getLatitude());
				String lon = String.valueOf(location.getLongitude());

				Log.d("initTracking", "location changed: lat="+lat+", lon="+lon);

				shCurrentLocation.setLatitude(location.getLatitude());
				shCurrentLocation.setLongitude(location.getLongitude());

				shOverlay.clear();
				shOverlay.addOverlay(new OverlayItem(shCurrentLocation.toGeoPoint(), "Hey!","Stop poking me!!!"));
				mapOverlays.clear();
				mapOverlays.add(shOverlay);

				mapController.animateTo(shCurrentLocation.toGeoPoint());
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
	 * Initialized map objects
	 * 
	 * @author Lars Lindgren <chrono@eeky.net>
	 * 
	 * TODO: Move code?
	 */
	private void initMapView() {
		mapView = (MapView) findViewById(R.id.mapview);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(18);
		//mapView.setSatellite(true);

		mapOverlays = mapView.getOverlays();
		shOverlay = new SHItemizedOverlay(this.getResources().getDrawable(R.drawable.cecil), this);
	}

	/**
	 * Handles clicks from "Check Here" button
	 * 
	 * @param view
	 */
	public void onClickCheckHere(final View view) {	
				
		Intent i = new Intent(this, DownloadActivity.class);
		i.putExtra("SHLocation", shCurrentLocation);
		startActivity(i);
	}

	/**
	 * Handles clicks from "Share Here" button
	 * 
	 * @param view
	 */
	public void onClickShareHere(final View view) {
		//shConnection.download("droid.jpg", shCurrentLocation);

		// Lars: testing upload
		//shConnection.upload("/mnt/sdcard/dont_panic.jpg", shCurrentLocation, "");
		//shConnection.upload("/mnt/sdcard/ultimate_trick.jpg", shCurrentLocation, "");
		//shConnection.upload("/mnt/sdcard/bsdrest.gif", shCurrentLocation, "");
		//shConnection.upload("/mnt/sdcard/tia_logo_large.jpg", shCurrentLocation, "NOWHERE TO HIDE");
		//shConnection.upload("/mnt/sdcard/etmessage_nrao.gif", shCurrentLocation, "");
	
		Intent i = new Intent(this, UploadActivity.class);
		i.putExtra("SHLocation", shCurrentLocation);
		startActivity(i);
	}

	/**
	 * Updates parts of the main view 
	 * 
	 * @author Lars Lindgren <chrono@eeky.net>
	 */
	public void updateView() {
		if (shCurrentLocation != null) {
			final TextView valueView = (TextView) findViewById(R.id.textview_gps);
			valueView.setText(shCurrentLocation.toString());    
			mapController.setCenter(shCurrentLocation.toGeoPoint());
		}
	}
}
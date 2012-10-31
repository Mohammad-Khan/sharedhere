package com.sharedhere;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import com.sharedhere.R;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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
    
        initTracking();
        initMapView(-location.getLatitude(), location.getLongitude()); 
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
		if (locManager == null)
			locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		if (locListener == null)
			locListener = new LocationListener() {
				public void onLocationChanged(Location location) {
					String lat = String.valueOf(location.getLatitude());
			        String lon = String.valueOf(location.getLongitude());
			        Log.e("GPS", "location changed: lat="+lat+", lon="+lon);
			        
			        location = locManager.getLastKnownLocation("gps");
			        
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
			
		locManager.requestLocationUpdates("gps", 0, 0, locListener);
		
        if (location == null)
			location = locManager.getLastKnownLocation("gps");
	}
	
	/**
	 * 
	 */
	private void initMapView(double latitude, double longitude) {
		if (mapView == null) {
			mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);
			//mapView.setSatellite(true);
		}
		
		if (mapController == null) {
			mapController = mapView.getController();
			mapController.setZoom(18);
			mapController.setCenter(new LatLonPoint(latitude, longitude));
		}
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
    }
}
package com.sharedhere;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

import com.sharedhere.R;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SharedHereAdapter extends MapActivity
{
	private MapView mapView = null;
	private MapController mapController = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
	private void initMapView() {
		if (mapView == null) {
			mapView = (MapView) findViewById(R.id.mapview);
			mapView.setBuiltInZoomControls(true);
		}
		
		if (mapController == null) {
			mapController = mapView.getController();
			//mapController.setZoom(15);
			//mapController.setCenter(new GeoPoint(10000,10000));
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
		valueView.setText("hello");    
    }
}
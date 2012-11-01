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

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;

public class SharedHereAdapter extends MapActivity
{
	public static final String SERVER_ADDRESS =
			"http://10.0.2.2/sharedhere/index.php";
	public static final int REQUEST_POI_DOWNLOAD = 	0;
	public static final int REQUEST_DATA_UPLOAD = 	1;
	public static final int REQUEST_DATA_DOWNLOAD =	2;
	
	private HttpClient httpClient = null;
	private HttpPost httpPost = null;
	private HttpResponse httpResponse = null;
	
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

		initPoi();
		initTracking();
		initMapView(-location.getLatitude(), location.getLongitude()); 
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Request the server for "points of interest"
	 * 
	 */
	private void initPoi() {
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(SERVER_ADDRESS);
		try {
			List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
			requestEntity.add(new BasicNameValuePair
					("request_id", Integer.toString(REQUEST_POI_DOWNLOAD)));
			httpPost.setEntity(new UrlEncodedFormEntity(requestEntity));
			
			httpResponse = httpClient.execute(httpPost);
			HttpEntity responseEntity = httpResponse.getEntity();
			InputStream is = responseEntity.getContent();
			BufferedReader br = new BufferedReader(
					new InputStreamReader(is),
					80
			);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line=br.readLine()) != null) {
				sb.append(line+"\n");
			}
			is.close();
			
			String result = sb.toString();
			Log.i("HTTP_Info", "Result: "+result);
			JSONArray jArray = new JSONArray(result);
			JSONObject jsonData = jArray.getJSONObject(0);
			
			// Temp: Display one of the points to the textview
			TextView httpTextView = (TextView) findViewById(R.id.textview_poi);
			httpTextView.setText(Double.toString(jsonData.getDouble("lat")) +
					"," + Double.toString(jsonData.getDouble("lon")) + ")");
			// print one the points to Log
			Log.i("HTTP_Info", "POI: "+"("+
							Double.toString(jsonData.getDouble("lat"))+
							","+Double.toString(jsonData.getDouble("lon"))+")");
			
		} catch (ClientProtocolException cpeException) {
			Log.e("HTTP_Err", "HTTP Protocol Exception: "+cpeException.getMessage());
			// Handle exception
		} catch (IOException ioException) {
			Log.e("HTTP_Err", "IO Exception: "+ioException.getMessage());
			// Handle IO Exception
		} catch (JSONException jsonException) {
			Log.e("HTTP_Err", "JSON Exceptoin: "+jsonException.getMessage());
		} catch (Exception exception) {
			Log.e("HTTP_Err", "Other Exceptoin: "+exception.getMessage());
		}
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
package com.sharedhere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.maps.GeoPoint;

import android.os.Environment;
import android.util.Log;

public class SHClientServer {
	public static final String SERVER_ADDRESS = "http://10.0.2.2/sharedhere/";
			//"http://sharedhere.zapto.org/index.php";
	public static final int REQUEST_POI_DOWNLOAD = 	0;
	public static final int REQUEST_DATA_UPLOAD = 	1;
	public static final int REQUEST_DATA_DOWNLOAD =	2;
	
	private HttpClient httpClient = null;
	private HttpPost httpPost = null;
	private HttpResponse httpResponse = null;
	
	/**
	 * Request the server for "points of interest"
	 * 
	 * @author naser
	 * 
	 * @return	List of GeoPoint objects
	 */
	public List<GeoPoint> getPoi() {
		httpClient = new DefaultHttpClient();
		String serverPage = SERVER_ADDRESS+"init.php";
		httpPost = new HttpPost(serverPage);
		List<GeoPoint> points = null;
		JSONArray jsonArray = null;
		JSONObject jsonObject = null;
		HttpEntity responseEntity = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = null;
		String line = null;
		String result = null;
		
		try {
			List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
			requestEntity.add(new BasicNameValuePair
					("request_id", Integer.toString(REQUEST_POI_DOWNLOAD)));
			httpPost.setEntity(new UrlEncodedFormEntity(requestEntity));
			
			httpResponse = httpClient.execute(httpPost);
			responseEntity = httpResponse.getEntity();
			inputStream = responseEntity.getContent();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			stringBuilder = new StringBuilder();	
			
			while ((line=bufferedReader.readLine()) != null) {
				stringBuilder.append(line+"\n");
			}
			inputStream.close();
			
			result = stringBuilder.toString();
			//Log.i("HTTP_Info", "Result: "+result);
			
			jsonArray = new JSONArray(result);
			points = new ArrayList<GeoPoint>(); //GeoPoint[jsonArray.length()];
			for(int i=0; i<jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				double x = (double)jsonObject.getDouble("latitude");
				double y = (double)jsonObject.getDouble("longitude");
				points.add(new GeoPoint((int)x, (int)y)); // TODO Points should be double
			}

		} catch (ClientProtocolException e) {
			Log.e("getPoi_Err", "HTTP Protocol Exception: "+e.getMessage());
		} catch (IOException e) {
			Log.e("getPoi_Err", "IO Exception: "+e.getMessage());
		} catch (JSONException e) {
			Log.e("getPoi_Err", "JSON Exceptoin: "+e.getMessage());
		} catch (Exception e) {
			Log.e("getPoi_Err", "Other Exceptoin: "+e.getMessage());
		}
		
		return points;
	}
	
	/**
	 * Lists the files that are available for a particular POI
	 * 
	 *  @author naser
	 *  
	 *  @param	poi	The point of Interest for which files available are desired
	 *  @return		list of available files with their details in a JSONArray
	 */
	public JSONArray listContent(SHLocation location) {
		String serverPage = SERVER_ADDRESS + "download.php";
		httpClient = new DefaultHttpClient();
		httpPost = new HttpPost(serverPage);
		HttpEntity responseEntity = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = null;
		String line = null;
		String result = null;
		JSONArray jContentArray = null;
		JSONObject jsonObject = null;
		
		try {
			List<NameValuePair> requestEntity = new ArrayList<NameValuePair>();
			requestEntity.add(new BasicNameValuePair
					("request_id", Integer.toString(REQUEST_POI_DOWNLOAD)));
			requestEntity.add(new BasicNameValuePair
					("latitude", Double.toString(location.getLatitude())));
			requestEntity.add(new BasicNameValuePair
					("longitude", Double.toString(location.getLongitude())));
			
			httpPost.setEntity(new UrlEncodedFormEntity(requestEntity));
			
			httpResponse = httpClient.execute(httpPost);
			responseEntity = httpResponse.getEntity();
			inputStream = responseEntity.getContent();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader); 
			stringBuilder = new StringBuilder();
			while ((line=bufferedReader.readLine()) != null) {
				stringBuilder.append(line+"\n");
			}
			inputStream.close();
			
			result = stringBuilder.toString();
			Log.i("HTTP_Info", "Result: "+result);
			jContentArray = new JSONArray(result);
			for(int i=0; i<jContentArray.length(); i++) {
				jsonObject = jContentArray.getJSONObject(i);
				String filename = (String)jsonObject.getString("filename"); // TODO change to double???
				String description = (String)jsonObject.getString("description");
				String timestamp = (String)jsonObject.getString("timestamp");
				Log.i("download_info", filename+description+timestamp);
			}

		} catch (ClientProtocolException cpeException) {
			Log.e("download_Err", "HTTP Protocol Exception: "+cpeException.getMessage());
		} catch (IOException ioException) {
			Log.e("download_Err", "IO Exception: "+ioException.getMessage());
		} catch (JSONException jsonException) {
			Log.e("download_Err", "JSON Exceptoin: "+jsonException.getMessage());
		} catch (Exception exception) {
			Log.e("download_Err", "Other Exceptoin: "+exception.getMessage());
		}
		
		return jContentArray;
	}
	
	/**
	 * Download file
	 * 
	 * @author naser
	 * 
	 * @param	filename	filename to be downloaded
	 * @return	
	 * 
	 */
	public void download(String filename) {
		String serverFileLocation = SERVER_ADDRESS+"content/";
		File sdCardRoot = Environment.getExternalStorageDirectory();
		try {
		    //this is the file to be downloaded
		    URL url = new URL(serverFileLocation+filename);
		    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		    urlConnection.setRequestMethod("GET");
		    urlConnection.setDoOutput(true);
		    urlConnection.connect();

		    File file = new File(sdCardRoot, filename);
		    FileOutputStream fileOutput = new FileOutputStream(file);
		    InputStream inputStream = urlConnection.getInputStream();

		    byte[] buffer = new byte[1024];
		    int bufferLength = 0;
		    while ( (bufferLength = inputStream.read(buffer)) > 0 ) {
		            fileOutput.write(buffer, 0, bufferLength);
		    }
		    Log.i("DownloadInfo", "at: "+file.toString());
		    fileOutput.close();

		} catch (MalformedURLException e) {
		    Log.e("SH_download", e.getMessage());
		} catch (IOException e) {
		    Log.e("SH_download", e.getMessage());
		}
	}
	
	public void upload(String pathname) {
		String serverPage = SERVER_ADDRESS + "upload.php";
		HttpClient httpclient = new DefaultHttpClient();
		HttpEntity responseEntity = null;
		StringBody requestId=null;
		MultipartEntity requestEntity = null;
		FileBody inputFile = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuilder stringBuilder = null;
		String line = null;
		String result = null;
		
        try {
            httpPost = new HttpPost(serverPage);

            inputFile = new FileBody(new File(pathname));

            requestEntity = new MultipartEntity();
            requestEntity.addPart("sharedfile", inputFile);
            //reqEntity.addPart("comment", comment);
            requestId = new StringBody(String.valueOf(REQUEST_DATA_UPLOAD));
            requestEntity.addPart("request_id", requestId);

            httpPost.setEntity(requestEntity);

            Log.i("UPLOAD_info","executing request " + httpPost.getRequestLine());
            httpResponse = httpclient.execute(httpPost);

            responseEntity = httpResponse.getEntity();
            
            //Log.i("HTTP_Info", resEntity.toString());

			inputStream = responseEntity.getContent();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			
			stringBuilder = new StringBuilder();
			while ((line=bufferedReader.readLine()) != null) {
				stringBuilder.append(line+"\n");
			}
			inputStream.close();
			
			result = stringBuilder.toString();
			Log.i("upload_infot", "Result: "+result);
         
        } catch (Exception e){
           Log.e("upload_Err", e.getMessage());
        }
		
	}
	
	
}

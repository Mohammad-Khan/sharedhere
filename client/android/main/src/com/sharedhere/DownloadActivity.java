package com.sharedhere;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sharedhere.model.SHClientServer;
import com.sharedhere.model.SHFileInfo;
import com.sharedhere.model.SHLocation;

/**
 * 
 * @author Zain
 *
 */
public class DownloadActivity extends ListActivity
{
	private SHLocation shCurrentLocation = null;
	private SHClientServer shConnection = null;
	JSONObject jsonObject = null;
	JSONArray jArray = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ListContentTask task = new ListContentTask();
		task.execute();
	}

	// OnClick checkhere button event,downloading file
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		
        SHFileInfo fileInfo = (SHFileInfo)l.getItemAtPosition(position);
        String filename = fileInfo.GetName();
//		DownloadTask task = new DownloadTask();
//		task.execute(filename);
		Intent i = new Intent(this, FileDialog.class);
		i.putExtra("SHLocation", shCurrentLocation);
		i.putExtra("FileName", filename);
		startActivity(i);
	}
	
	
	private class ListContentTask extends AsyncTask<Void, Void, SHFileInfo[]> {
    	@Override
    	protected SHFileInfo[] doInBackground(Void... arg) {
			try {

				shCurrentLocation = (SHLocation) getIntent().getSerializableExtra("SHLocation");

				shConnection = new SHClientServer(getString(R.string.server_address));

				// Content available at current location returned from Client-Server
				// activity
				jArray = shConnection.listContent(shCurrentLocation);
							
				ArrayList<SHFileInfo> fileInformation = new ArrayList<SHFileInfo>();
				// Parsing the JSON array
				try {

					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						String filename = (String) jsonObject.getString("filename");
						Long size = Long.decode(jsonObject.getString("size"));
						String description = (String)jsonObject.getString("description");
						Double latitude = Double.valueOf(jsonObject.getString("latitude"));
						Double longitude = Double.valueOf(jsonObject.getString("longitude"));
						
						//String displayString = String.format("File Name: %s\nSize: %s KB\nDesc: %s", filename, size, description);
						SHFileInfo fileInfo = new SHFileInfo(size, filename, description, latitude, longitude );
												
						fileInformation.add(fileInfo);
						Log.d("filename", fileInfo.toString());

					}
					SHFileInfo[] fnames = new SHFileInfo[fileInformation.size()];
					return fileInformation.toArray(fnames);
				
				} catch (Exception e) {
					return new SHFileInfo[0];
				}
			}
			catch (Exception e){
				return new SHFileInfo[0];
			}
    	}
    	
    	@Override
    	protected void onPostExecute(SHFileInfo[] result){
			// If no records available at this location show a message and exit activity
			if(result.length==0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(DownloadActivity.this);
				builder.setMessage("No files here!");
				builder.setPositiveButton("OK", new OnClickListener() {	
					public void onClick(DialogInterface dialog, int which) {
						DownloadActivity.this.finish();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}else{							
				ArrayAdapter<SHFileInfo> mArrayAdapter = new ArrayAdapter<SHFileInfo>(DownloadActivity.this,
						android.R.layout.simple_expandable_list_item_1, result);
				setListAdapter(mArrayAdapter);
			}
    	}
     }
	
}

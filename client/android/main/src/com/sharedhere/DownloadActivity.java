package com.sharedhere;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.sharedhere.model.SHClientServer;
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
		//setContentView(R.layout.download);

		ListContentTask task = new ListContentTask();
		task.execute();
	}

	// OnClick checkhere button event,downloading file
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String filename = l.getItemAtPosition(position).toString();
		DownloadTask task = new DownloadTask();
		task.execute(filename);
//		shConnection.download(filename, shCurrentLocation);
//
//		// Displaying a toast message
//		Context context = getApplicationContext();
//		CharSequence text = "Finished Downloading!";
//		int duration = Toast.LENGTH_SHORT;
//
//		Toast toast = Toast.makeText(context, text, duration);
//		toast.show();
	}
	
	private class DownloadTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String filename = params[0];
			return shConnection.download(filename, shCurrentLocation);			
		}
	 	@Override
    	protected void onPostExecute(Boolean b){
			Context context = getApplicationContext();
			CharSequence text = "Finished Downloading!";
			int duration = Toast.LENGTH_SHORT;

			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
	 	}
	}
	
	private class ListContentTask extends AsyncTask<Void, Void, String[]> {
    	@Override
    	protected String[] doInBackground(Void... arg) {
			try {

				shCurrentLocation = (SHLocation) getIntent().getSerializableExtra("SHLocation");

				shConnection = new SHClientServer(getString(R.string.server_address));

				// Content available at current location returned from Client-Server
				// activity
				jArray = shConnection.listContent(shCurrentLocation);
							
				ArrayList<String> fileInformation = new ArrayList<String>();
				// Parsing the JSON array
				try {

					for (int i = 0; i < jArray.length(); i++) {
						jsonObject = jArray.getJSONObject(i);
						String filename = (String) jsonObject.getString("filename");
						String size = (String)jsonObject.getString("size");
						String description = (String)jsonObject.getString("description");
						String latitude = (String)jsonObject.getString("latitude");
						String longitude = (String)jsonObject.getString("longitude");
						
						String displayString = String.format("File Name: %s\nSize: %s KB\nDesc: %s", filename, size, description);
						//TODO Change this to use SHFileInfo objects						
						fileInformation.add(displayString);
						Log.d("filename", displayString);

					}
					String[] fnames = new String[fileInformation.size()];
					return fileInformation.toArray(fnames);
				
				} catch (Exception e) {
					return new String[0];
				}
			}
			catch (Exception e){
				return new String[0];
			}
    	}
    	
    	@Override
    	protected void onPostExecute(String[] result){
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
				ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(DownloadActivity.this,
						android.R.layout.simple_expandable_list_item_1, result);
				setListAdapter(mArrayAdapter);
			}
    	}
     }
	
}

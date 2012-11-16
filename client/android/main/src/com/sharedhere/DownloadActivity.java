package com.sharedhere;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
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

		shCurrentLocation = (SHLocation) getIntent().getSerializableExtra("SHLocation");

		shConnection = new SHClientServer(getString(R.string.server_address));

		// Content available at current location returned from Client-Server
		// activity
		jArray = shConnection.listContent(shCurrentLocation);
		
		// If no records available at this location show a message and exit activity
		if(jArray == null || jArray.length()==0) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("No files available for your current location");
			builder.setPositiveButton("OK", new OnClickListener() {	
				public void onClick(DialogInterface dialog, int which) {
					DownloadActivity.this.finish();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
		
		ArrayList<String> filenames = new ArrayList<String>();

		// Parsing the JSON array
		try {

			for (int i = 0; i < jArray.length(); i++) {
				jsonObject = jArray.getJSONObject(i);
				String filename = (String) jsonObject.getString("filename");

				filenames.add(filename);
				Log.d("filename", filename);

			}
			String[] fnames = new String[filenames.size()];
			fnames = filenames.toArray(fnames);
			ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_expandable_list_item_1, fnames);
			setListAdapter(mArrayAdapter);

		} catch (Exception e) {
		}

	}

	// OnClick checkhere button event,downloading file
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		String filename = l.getItemAtPosition(position).toString();
		shConnection.download(filename, shCurrentLocation);

		// Displaying a toast message
		Context context = getApplicationContext();
		CharSequence text = "Finished Downloading!";
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
}

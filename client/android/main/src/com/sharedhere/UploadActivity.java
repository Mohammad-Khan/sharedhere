package com.sharedhere;

import java.io.File;

import org.apache.http.HttpEntity;
import org.apache.http.entity.mime.MultipartEntity;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sharedhere.model.SHArrayAdapter;
import com.sharedhere.model.SHClientServer;
import com.sharedhere.model.SHLocation;

public class UploadActivity extends ListActivity {
	private File currentDirectory = null;
	private File parentDirectory = null;
	private SHLocation currentLocation = null;
	private String serverAddress = null;
	static final String ROOT_DIRECTORY = "/mnt/sdcard";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Set current and parent directories to root (initially)
		currentDirectory = new File(ROOT_DIRECTORY);
		parentDirectory = currentDirectory;
		// Set current location
		currentLocation = (SHLocation) getIntent().getSerializableExtra(
				"SHLocation");
		// Process the root directory
		processDirectory();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		File file = (File) getListAdapter().getItem(position);
		if (file.isDirectory()) {
			currentDirectory = file;
			parentDirectory = file.getParentFile() == null ? new File(
					ROOT_DIRECTORY) : file.getParentFile();
			processDirectory();
		} else {	
			//Changed this part of the method completely to be asynchronous. This way no Network I/O is on the main form
			//VN & SH
			UploadTask task = new UploadTask();
			String fileName = file.getAbsolutePath();
			String description = "Is it a game, or is it real? It's probably real.";
			task.execute(fileName, description);
		}
	}

	/**
	 * Processes the current directory Assigns the content of current directory
	 * to the upload array adapter
	 */
	private void processDirectory() {
		File[] files = currentDirectory.listFiles();
		SHArrayAdapter adapter = new SHArrayAdapter(this, files);
		setListAdapter(adapter);
	}
	
	
	private class UploadTask extends AsyncTask<String, Void, Boolean> {
    	@Override
    	protected Boolean doInBackground(String... params) {
			try {
				String filePath = params[0];
				String description = params[1];
				serverAddress = getString(R.string.server_address);
				SHClientServer shConnection = new SHClientServer(serverAddress);
				return shConnection.upload(filePath, currentLocation, description);
				
			} catch (Exception e) {
				return false;
			}
    	}
    	
    	@Override
    	protected void onPostExecute(Boolean result){
        	try	{
        		if (result)
    				Toast.makeText(UploadActivity.this, "File uploaded", Toast.LENGTH_LONG).show();
    			else
    				Toast.makeText(UploadActivity.this, "Upload failed", Toast.LENGTH_LONG).show();
        	}
        	catch (Exception ex){
        		System.out.println("Broken");
        	}
    	}
     }
	 
}

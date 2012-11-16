package com.sharedhere;

import java.io.File;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
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
			serverAddress = getString(R.string.server_address);
			SHClientServer shConnection = new SHClientServer(serverAddress);
			// TODO CleintServer.download should return/throw boolean/exception
			// and
			// the return should be checked and the toast below be set
			// accordingly
			boolean uploadStatus = shConnection.upload(file.getAbsolutePath(),
					currentLocation, "Is it a game, or is it real?");
			if (uploadStatus)
				Toast.makeText(this, "File uploaded", Toast.LENGTH_LONG).show();
			else
				Toast.makeText(this, "Upload failed", Toast.LENGTH_LONG).show();
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
}

package com.sharedhere;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.sharedhere.model.SHArrayAdapter;
import com.sharedhere.model.SHClientServer;
import com.sharedhere.model.SHLocation;

public class FileDialog extends Activity {
	private File currentDirectory = null;
	private File parentDirectory = null;
	private SHLocation shCurrentLocation = null;
	private SHClientServer shConnection = null;
	private String serverAddress = null;
	private String fileName = null;
	static final String ROOT_DIRECTORY = "/mnt/sdcard";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_dialog);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	// Set current and parent directories to root (initially)
		currentDirectory = new File(ROOT_DIRECTORY);
		parentDirectory = currentDirectory;
		shCurrentLocation = (SHLocation) getIntent().getSerializableExtra("SHLocation");
		fileName = (String)getIntent().getStringExtra("FileName");
		// Process the root directory
		processDirectory();		
    }
           
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_location_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	/**
	 * Processes the current directory Assigns the content of current directory
	 * to the upload array adapter
	 */
	private void processDirectory() {
		File[] files = currentDirectory.listFiles();
		ArrayList<File> filteredData = new ArrayList<File>();
		filteredData.add(parentDirectory);//add the parent directory as the first element in the array for navigation
		for(File f:files){
			if (f.isDirectory()){
				filteredData.add(f);
			}
		}		
		File[] adapterData = filteredData.toArray(new File[filteredData.size()]);
		SHArrayAdapter adapter = new SHArrayAdapter(this, adapterData);
		final ListView lv = (ListView)findViewById(R.id.listViewFileStructure);
		
	    lv.setAdapter(adapter);
	    lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> listView, View v,  int position, long id) {
				File file = (File)lv.getItemAtPosition(position);
				currentDirectory = file;
				parentDirectory = file.getParentFile() == null ? new File(
						ROOT_DIRECTORY) : file.getParentFile();
				processDirectory();			
			}
		});
	    
	}
	
	/**
	 * Handles clicks from "Use Current Button" button
	 * 
	 * @param view
	 */
	public void onClickUseCurrentFolder(final View view) {	
		String path = currentDirectory.getAbsolutePath();
		DownloadTask task = new DownloadTask();
		task.execute(path);
	}
	
	private class DownloadTask extends AsyncTask<String, Void, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			String path = params[0];
			shConnection = new SHClientServer(getString(R.string.server_address));		
			return shConnection.download(fileName, shCurrentLocation, path);			
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
}

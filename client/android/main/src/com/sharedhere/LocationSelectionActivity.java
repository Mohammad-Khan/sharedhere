package com.sharedhere;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.sharedhere.model.SHArrayAdapter;
import com.sharedhere.model.SHLocation;

public class LocationSelectionActivity extends Activity {
	private File currentDirectory = null;
	private File parentDirectory = null;
	private SHLocation currentLocation = null;
	private String serverAddress = null;
	static final String ROOT_DIRECTORY = "/mnt/sdcard";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
    	// Set current and parent directories to root (initially)
		currentDirectory = new File(ROOT_DIRECTORY);
		parentDirectory = currentDirectory;
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
		for(File f:files){
			if (f.isDirectory()){
				filteredData.add(f);
			}
		}		
		File[] adapterData = (File[])filteredData.toArray();
		SHArrayAdapter adapter = new SHArrayAdapter(this, adapterData);
		ListView lv = (ListView)findViewById(R.id.listViewFileStructure); 
	    lv.setAdapter(adapter);
	}
	

}

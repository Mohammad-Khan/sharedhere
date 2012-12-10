package com.sharedhere.model;

import java.util.Date;

/**
 * A class that encapsulates the information that is received from the server regarding a file.
 * @author SHamidi
 *
 */
public class SHFileInfo {	
	Long mSize;
	String mDescription;
	String mName;
	Double mLatitude;
	Double mLongitude;
	
	/**	 
	 * @param size size of file
	 * @param desc Description of file
	 * @param lat Latitude 
	 * @param lon Longitude
	 */
	public SHFileInfo(Long size, String name,String desc, Double lat, Double lon){		
		mSize = size;
		mDescription = desc;
		mLatitude = lat;
		mLongitude = lon;
		mName= name;
	}
	
	public String GetName(){
		return mName;
	}
	
	public Double GetLatitude(){
		return mLatitude;
	}
	
	public Double GetLongitude(){
		return mLongitude;
	}
	
	public Long GetSize(){
		return mSize;
	}
	
	public String GetDescription(){
		return mDescription;
	}
	
	@Override
	public String toString(){
		return String.format("File Name: %s\nSize: %s KB\nDesc: %s", mName, mSize, mDescription);
	}
	
}

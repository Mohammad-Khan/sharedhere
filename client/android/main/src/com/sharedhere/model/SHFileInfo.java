package com.sharedhere.model;

import java.util.Date;

/**
 * A class that encapsulates the information that is received from the server regarding a file.
 * @author SHamidi
 *
 */
public class SHFileInfo {
	Date mTimeStamp;
	Long mSize;
	String mDescription;
	Double mLatitude;
	Double mLongitude;
	
	/**	
	 * @param ts Timestamp
	 * @param size size of file
	 * @param desc Description of file
	 * @param lat Latitude 
	 * @param lon Longitude
	 */
	public SHFileInfo(Date ts, Long size, String desc, Double lat, Double lon){
		mTimeStamp = ts;
		mSize = size;
		mDescription = desc;
		mLatitude = lat;
		mLongitude = lon;
	}
	
	public Date GetTimestamp(){
		return mTimeStamp;
	}
	
	public Long GetSize(){
		return mSize;
	}
	
	public String GetDescription(){
		return mDescription;
	}
	
}

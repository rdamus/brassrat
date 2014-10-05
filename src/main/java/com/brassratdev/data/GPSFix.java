/**
 * 
 */
package com.brassratdev.data;

import java.util.Date;


/**
 * A gps fix in time. This container has latitude and longitude and time value
 * for the fix. Latitude and Longitude are stored in Decimal Degrees. Valid is
 * determined by parsing the fix. Typically, an 'A' or 'V' character is
 * indicated in the NEMA string used for parsing the fix.
 * 
 * @author rdamus
 * @see GPSUtils
 */
public class GPSFix {
	double latitude = 0.0;
	double longitude = 0.0;
	double altitude = 0.0;
	Date time = new Date();
	boolean valid;

	public GPSFix(double latitude, double longitude, Date time) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
		this.valid = true;
	}
	
	public GPSFix(double latitude, double longitude, Date time, double altitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.time = time;
		this.valid = true;
		this.altitude = altitude;
	}

	/**
	 * sets this fix to 0.0,0.0 (off the coast of Africa)
	 */
	public GPSFix() {
		this(0.0, 0.0, new Date());
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String toString() {
		return latitude + "," + longitude + "@" + time + " valid: " + isValid();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
}
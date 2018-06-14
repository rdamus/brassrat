package com.brassratdev.util.geo;

import java.util.Date;

/**
 * A GPS fix in time. This container has latitude and longitude and time value
 * for the fix. Latitude and Longitude are stored in Decimal Degrees. Valid is
 * determined by parsing the fix. Typically, an 'A' or 'V' character is
 * indicated in the NMEA string used for parsing the fix.
 * 
 * @author rob
 */
public class GPSFix extends GridFix {
	Date time = new Date();
	boolean valid;
	double altitude;

	public GPSFix(double latitude, double longitude, Date time) {
		super(longitude, latitude);
		this.time = time;
		this.valid = true;
	}

	/**
	 * sets this fix to 0.0,0.0 (off the coast of Africa)
	 */
	public GPSFix() {
		this(0.0, 0.0, new Date());
	}

	public double getLatitude() {
		return getY();
	}

	public void setLatitude(double latitude) {
		this.y = latitude;
	}

	public double getLongitude() {
		return getX();
	}

	public void setLongitude(double longitude) {
		this.x = longitude;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String toString() {
		return getLatitude() + "," + getLongitude() + "@" + time + " valid: "
				+ isValid();
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	
	public double getAltitude() {
		return altitude;
	}
}

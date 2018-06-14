package com.brassratdev.sim.model;

import java.util.Date;

/**
 * Wrapper class for a latitude and longitude pair.
 * 
 * @author rdamus
 * 
 */
public class Location {
	// /latitude -90.0 to +90.0
	double latitude;
	// /longitude -180.0 to +180.0 deg
	double longitude;
	// /the time that the latitude and longitude were valid
	Date timeStamp;

	public Location() {
		this(0.0f, 0.0f);
	}

	public Location(double lat, double lon) {
		this.latitude = lat;
		this.longitude = lon;
		this.timeStamp = new Date();
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((timeStamp == null) ? 0 : timeStamp.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (Double.doubleToLongBits(latitude) != Double
				.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double
				.doubleToLongBits(other.longitude))
			return false;
		if (timeStamp == null) {
			if (other.timeStamp != null)
				return false;
		} else if (!timeStamp.equals(other.timeStamp))
			return false;
		return true;
	}

	public double distanceTo(Location l) {
		double dx = Math.pow(l.getLatitude() - getLatitude(), 2.0);
		double dy = Math.pow(l.getLongitude() - getLongitude(), 2.0);
		return Math.sqrt(dx + dy);
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String toString() {
		return "lat: " + getLatitude() + ", lon: " + getLongitude()
				+ " " + getTimeStamp();
	}
}
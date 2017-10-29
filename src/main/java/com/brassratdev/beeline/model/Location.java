package com.brassratdev.beeline.model;

import java.util.Date;

/**
 * Wrapper class for a latitude and longitude pair.
 * 
 * TODO: needs a timestamp
 * 
 * @author rdamus
 * 
 */
public class Location {
    // /latitude -90.0 to +90.0
    float latitude;
    // /longitude -180.0 to +180.0 deg
    float longitude;
    // /the time that the latitude and longitude were valid
    Date timeStamp;

    public Location() {
	this(0.0f, 0.0f);
    }

    public Location(float lat, float lon) {
	this.latitude = lat;
	this.longitude = lon;
	this.timeStamp = new Date();
    }

    public float getLatitude() {
	return latitude;
    }

    public float getLongitude() {
	return longitude;
    }

    public void setLatitude(float latitude) {
	this.latitude = latitude;
    }

    public void setLongitude(float longitude) {
	this.longitude = longitude;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Float.floatToIntBits(latitude);
	result = prime * result + Float.floatToIntBits(longitude);
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
	if (Float.floatToIntBits(latitude) != Float
		.floatToIntBits(other.latitude))
	    return false;
	if (Float.floatToIntBits(longitude) != Float
		.floatToIntBits(other.longitude))
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
		+ " - valid at " + getTimeStamp();
    }
}
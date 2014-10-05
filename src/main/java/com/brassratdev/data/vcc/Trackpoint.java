package com.brassratdev.data.vcc;
import java.util.Date;

public class Trackpoint {
	Date date;
	float heading;
	double latitude;
	double longitude;
	float speed;
	
	public Trackpoint(){}
	
	public Trackpoint(Date date, float heading, double latitude,
			double longitude, float speed) {
		super();
		this.date = date;
		this.heading = heading;
		this.latitude = latitude;
		this.longitude = longitude;
		this.speed = speed;
	}

	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public float getHeading() {
		return heading;
	}
	public void setHeading(float heading) {
		this.heading = heading;
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
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
}

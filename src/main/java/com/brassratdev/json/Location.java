package com.brassratdev.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
/**
 * {
        "age": 0,
        "latitude": "37.787189",
        "longitude": "-122.249298",
        "elevation": "8.1",
        "course": "",
        "speed": "1.058"
}

 * @author rdamus
 *
 */
public class Location extends GenericJson {
	@Key
	String age;
	@Key
	String latitude;
	@Key
	String longitude;
	@Key
	String elevation;
	@Key
	String course;
	@Key
	String speed;
	
	public Location(String age, String latitude, String longitude, String elevation, String course, String speed) {
		super();
		this.age = age;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.course = course;
		this.speed = speed;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getElevation() {
		return elevation;
	}
	public void setElevation(String elevation) {
		this.elevation = elevation;
	}
	public String getCourse() {
		return course;
	}
	public void setCourse(String course) {
		this.course = course;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
}

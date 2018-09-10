package com.brassratdev.json;

public class LocationRequest extends RequestBody {
	public LocationRequest(){
		set("data", "[{\"cmd\":\"loc\"}]");
	}
}

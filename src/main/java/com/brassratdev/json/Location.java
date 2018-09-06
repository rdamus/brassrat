package com.brassratdev.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public class Location extends GenericJson {
	@Key
	String latitude;
	@Key
	String longitude;
	
}

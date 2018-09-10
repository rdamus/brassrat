package com.brassratdev.json;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
/**
 * @author rdamus
 *
 */
public class Error extends GenericJson {
	@Key
	String error;

	
	public Error(String error) {
		super();
		this.error = error;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
}

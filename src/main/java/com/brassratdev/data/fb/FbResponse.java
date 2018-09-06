package com.brassratdev.data.fb;

public class FbResponse {
	FbData data;
	
	public FbResponse() {
		super();
	}

	public FbResponse(FbData data) {
		super();
		this.data = data;
	}

	public FbData getData() {
		return data;
	}

	public void setData(FbData data) {
		this.data = data;
	}
	
}

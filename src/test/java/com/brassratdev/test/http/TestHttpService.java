package com.brassratdev.test.http;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.brassratdev.json.DataRequest;
import com.brassratdev.json.Location;
import com.brassratdev.json.LocationResult;
import com.brassratdev.net.JsonHttpService;
import com.google.api.client.http.HttpRequest;

public class TestHttpService {
	JsonHttpService jsonService;
	HttpRequest request;
	String url = "http://localhost:8080";
	@Before
	public void setUp() throws Exception {
		jsonService = new JsonHttpService();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHttpRequest() throws IOException{
		DataRequest<Location> data = new DataRequest<>();
		
		request = jsonService.createPostRequest(url, data);
		jsonService.parse(LocationResult.class, request.execute());
	}
}

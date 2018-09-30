package com.brassratdev.test.http;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.brassratdev.json.LocationRequest;
import com.brassratdev.json.Location;
import com.brassratdev.json.LocationResult;
import com.brassratdev.net.JsonHandler;
import com.brassratdev.net.JsonHttpService;
import com.brassratdev.net.NetworkServer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.json.jackson2.JacksonFactory;

public class TestHttpService {
	JsonHttpService jsonService;
	HttpRequest request;
	String url = "http://localhost:8080";
	MockLocationServer locServer;
	@Before
	public void setUp() throws Exception {
		jsonService = new JsonHttpService();
		locServer = new MockLocationServer(8080, new LocationHandler());
		locServer.start();
	}

	@After
	public void tearDown() throws Exception {
		locServer.stop();
	}
	
	@Test
	public void testJson() throws IOException{
		LocationRequest req = new LocationRequest();
		System.out.println("DataRequest Empty: " + req.toPrettyString());
	}
	
	@Test
	public void testLocationJson() throws IOException{
		Location loc = new Location("0", "42", "-118", "10", "335", "2.5");
		System.out.println("Location: " + loc.toPrettyString());	
	}
	
	@Test
	public void testHttpRequest() throws IOException{
		LocationRequest req = new LocationRequest();
		request = jsonService.createPostRequest(url, req);
		
		jsonService.parse(LocationResult.class, request.execute());
	}
	
	@Test
	public void testHttpTunnelRequest() throws IOException{
		LocationRequest req = new LocationRequest();
		request = jsonService.createPostRequest("http://localhost:5000", req);
		
		jsonService.parse(LocationResult.class, request.execute());
	}
}

class LocationHandler extends JsonHandler{

	@Override
	protected byte[] reply() {
		Location loc = new Location("0", "42", "-118", "10", "335", "2.5");
		try {
			return JacksonFactory.getDefaultInstance().toByteArray(loc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{}".getBytes();
		}
	}
	
	
}

class MockLocationServer extends NetworkServer<LocationHandler>{

	public MockLocationServer(int port, LocationHandler handler) {
		super(port, handler);
	}}

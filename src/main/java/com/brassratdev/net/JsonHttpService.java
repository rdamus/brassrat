package com.brassratdev.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brassratdev.json.RequestBody;
import com.brassratdev.util.IOUtils;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

public class JsonHttpService extends HttpService{
	/** Global instance of the JSON factory. */
	private static JsonFactory jsonFactory;
	
	private static final Logger log = LogManager.getLogger(JsonHttpService.class);

	public JsonHttpService() throws GeneralSecurityException, IOException {
		super();
		jsonFactory = JacksonFactory.getDefaultInstance();
	}

	
	public <T> T parse(Class<T> clazz, HttpResponse response) throws IOException {
		InputStream ris = response.getContent();
		ByteArrayOutputStream baos = IOUtils.toByteArrayOutputStream(ris);
		debug(response, baos);
		return (T) jsonFactory.fromInputStream(new ByteArrayInputStream(baos.toByteArray()), clazz);
	}
	
	@Override
	protected HttpContent createHttpContent(RequestBody data) {
		return new JsonHttpContent(jsonFactory, data);
	}
	
	protected HttpHeaders createHeaders() {
		HttpHeaders h = new HttpHeaders();
		h.setContentType("application/json");
		return h;
	}


	@Override
	protected void debug(HttpRequest request) throws IOException {
		super.debug(request);
		
		JsonHttpContent content = (JsonHttpContent) request.getContent();
		if( content != null ) {
			Object data = content.getData();
			log.debug("HttpContent length: " + content.getLength());
			log.debug("HttpContent data: " + data.toString());
		}
	}
}

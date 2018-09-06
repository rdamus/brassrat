package com.brassratdev.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.brassratdev.json.RequestBody;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;

public abstract class HttpService {
	/** OAuth 2 scope. */
	protected String scope = "https://www.googleapis.com/auth/photoslibrary";
	protected String apiPrefix = "https://photoslibrary.googleapis.com/v1/";
	/** Global instance of the HTTP transport. */
	private static HttpTransport httpTransport;
	private static enum Endpoint{albums,mediaItems,sharedAlbums};
	private static enum Method{create,get,list,search,join};
	
	private static final Logger log = LogManager.getLogger(HttpService.class);

	public HttpService() throws GeneralSecurityException, IOException {
		System.err.println("initializing...");
		httpTransport = GoogleNetHttpTransport.newTrustedTransport();	
	}

	private boolean authorize() {
		return false;
	}
		
	public HttpRequest createGetRequest(Method m, Endpoint e, String arg) throws IOException {
		//authorize
		String url = createUrl(m, e, arg);
		
		//json required
		HttpRequest r = httpTransport
				.createRequestFactory()
				.buildGetRequest( new GenericUrl(url) )
				.setHeaders( createHeaders() );
		debug(r);
		return r;
	}
	
	public HttpRequest createPostRequest(Method m, Endpoint e, RequestBody data) throws IOException {
		//authorize
		String url = createUrl(m, e, null);
		HttpContent content = createHttpContent(data);
		
		HttpRequest r = httpTransport
				.createRequestFactory()
				.buildPostRequest(new GenericUrl(url), content)
				.setHeaders( createHeaders() );
		debug(r);
		return r;
	}
	
	public HttpRequest createPostRequest(String url, RequestBody data) throws IOException {
		HttpContent content = createHttpContent(data);
		
		HttpRequest r = httpTransport
				.createRequestFactory()
				.buildPostRequest(new GenericUrl(url), content)
				.setHeaders( createHeaders() );
		debug(r);
		return r;
	}


	abstract protected HttpContent createHttpContent(RequestBody data);
	abstract protected HttpHeaders createHeaders();

	private String createUrl(Method method, Endpoint ep, String arg) {
		String url = apiPrefix + ep.name();
		
		url = handleMethod(method, arg, url);
//		url += "?access_token=" + cmgr.getAccessToken();
		log.debug("Url: " + url);
		return url;
	}

	/**
	 * default behavior
	 * @param method
	 * @param arg
	 * @param url
	 * @return
	 */
	protected String handleMethod(Method method, String arg, String url) {
		switch(method) {
		case search:
		case join:
			url += ":" + method.name();
			break;
		case get:
			url += "/" + arg;
			break;
		default:
			break;
		}
		return url;
	}
	
	protected void debug(HttpRequest request) throws IOException {
		log.debug("HttpRequest Method: " + request.getRequestMethod());
		HttpHeaders headers = request.getHeaders();
		
		for(Entry<String, Object> h:headers.entrySet()){
			log.debug("HttpRequest key: " + h.getKey() + ", val: " + h.getValue());
		}
	}
	
	protected void debug(HttpResponse response, ByteArrayOutputStream content) throws IOException {
		log.debug("HttpResponse-----");
		log.debug("Content Type: " + response.getContentType());
		log.debug("response: " + content.toString());
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getApiPrefix() {
		return apiPrefix;
	}

	public void setApiPrefix(String apiPrefix) {
		this.apiPrefix = apiPrefix;
	}
}

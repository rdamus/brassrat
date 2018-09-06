/**
 * 
 */
package com.brassratdev.net;

import java.io.IOException;
import java.io.OutputStream;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Abstract base class for implementing a Network Link to handle Google Earth
 * requests
 * 
 * @see NetworkLinkServer
 * @author rdamus
 * 
 */
public abstract class JsonHandler implements HttpHandler {
	public void handle(HttpExchange exchange) throws IOException {
		String requestMethod = exchange.getRequestMethod();
		if (requestMethod.equalsIgnoreCase("GET")) {
			Headers responseHeaders = exchange.getResponseHeaders();
			responseHeaders.set("Content-Type", "application/json");
			exchange.sendResponseHeaders(200, 0);

			OutputStream responseBody = exchange.getResponseBody();
			responseBody.write(reply());
			responseBody.close();
		}
	}

	abstract protected byte[] reply();
}
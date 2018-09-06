package com.brassratdev.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * Generic {@link HttpServer} for handling http:// requests on a desired port
 * 
 * @author rdamus
 * 
 */
public class NetworkServer<T extends HttpHandler> {
	T handler;
	HttpServer server;
	InetSocketAddress address;
	boolean running = false;
	private static final Logger log = LogManager.getLogger(NetworkServer.class);


	public NetworkServer(int port) {
		address = new InetSocketAddress(port);
		handler = createHandler();
	}

	/**
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected T createHandler() {
		return (T) new JsonHandler();
	}

	public T getHandler() {
		return handler;
	}

	public void setHandler(T handler) {
		this.handler = handler;
	}

	public HttpServer getServer() {
		return server;
	}

	public void setServer(HttpServer server) {
		this.server = server;
	}

	public InetSocketAddress getAddress() {
		return address;
	}

	public void setAddress(InetSocketAddress address) {
		this.address = address;
	}

	public void start() throws IOException {
		server = HttpServer.create(address, 0);
		server.createContext("/", handler);
		server.setExecutor(Executors.newCachedThreadPool());
		server.start();
		logMessage("Server is listening on " + getAddress());
		setRunning(true);
	}

	private void logMessage(String msg) {
		log.debug(msg);
	}

	public void stop() {
		if (isRunning())
			server.stop(2);// wait 2s to stop all connections
		logMessage("NetworkLinkServer stopped listening on " + getAddress());
		setRunning(false);
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

}

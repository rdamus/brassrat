package com.brassratdev.data.fb;

public class FbData {
	String service;
	String target;
	String generated;
	String start;
	String end;
	
	IPAddresses ipAddresses = new IPAddresses();
	
	
	public FbData() {
		super();
	}

	public IPAddresses getIPAddresses() {
		return ipAddresses;
	}

	public void setIPAddresses(IPAddresses ipAddresses) {
		this.ipAddresses = ipAddresses;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getGenerated() {
		return generated;
	}

	public void setGenerated(String generated) {
		this.generated = generated;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}
	
	
}

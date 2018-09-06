package com.brassratdev.data.fb;

public class IPAddress {
	String ip;
	String time;
	
	public IPAddress(){}
	
	public IPAddress(String ip, String time) {
		super();
		this.ip = ip;
		this.time = time;
	}

	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
}

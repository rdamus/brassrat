package com.brassratdev.data.fb;

import java.util.ArrayList;
import java.util.List;

public class IPAddresses {
	List<IPAddress> addresses = new ArrayList<>();
	
	public IPAddresses() {
		// TODO Auto-generated constructor stub
	}
	
	public void addIPAddress(IPAddress ip){
		addresses.add(ip);
	}

	public List<IPAddress> getAddresses() {
		return addresses;
	}

	public void setAddresses(List<IPAddress> list) {
		this.addresses = list;
	}
	
}

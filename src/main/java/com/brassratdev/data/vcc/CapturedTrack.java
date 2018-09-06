package com.brassratdev.data.vcc;

public class CapturedTrack {
	Trackpoints trackPoints;
    DeviceInfo deviceInfo;
    
    MinLatitude minLatitude;
    double MaxLatitude;
    double MinLongitude;
    double MaxLongitude;

    String downloadedOn;
	String name; 
	String numberTrkpts;
	
	public CapturedTrack(){
		trackPoints = new Trackpoints();
	}
	
	

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public void setTrackPoints(Trackpoints trackPoints) {
		this.trackPoints = trackPoints;
	}
	
	public String getDownloadedOn() {
		return downloadedOn;
	}

	public void setDownloadedOn(String downloadedOn) {
		this.downloadedOn = downloadedOn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumberTrkpts() {
		return numberTrkpts;
	}

	public void setNumberTrkpts(String numberTrkpts) {
		this.numberTrkpts = numberTrkpts;
	}

	public Trackpoints getTrackPoints() {
		return trackPoints;
	}

	public MinLatitude getMinLatitude() {
		return minLatitude;
	}

	public void setMinLatitude(MinLatitude minLatitude) {
		this.minLatitude = minLatitude;
	}

	
}

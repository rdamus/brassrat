package com.brassratdev.data.vcc;

public class VelocitekControlCenter {
	CapturedTrack track;
	
	public VelocitekControlCenter(){}
	
	public VelocitekControlCenter(CapturedTrack track) {
		super();
		this.track = track;
	}

	public void setTrack(CapturedTrack track){
		this.track = track;
	}

	public CapturedTrack getTrack() {
		return track;
	}

	
}

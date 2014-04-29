package com.brassratdev.data.vcc;

import java.util.ArrayList;
import java.util.List;

public class Trackpoints {
	List<Trackpoint> trackPoints;

	public Trackpoints(){
		trackPoints = new ArrayList<>();
	}
	
	public void addPoint(Trackpoint point){
		this.trackPoints.add( point );
	}

	public List<Trackpoint> getTrackPoints() {
		return trackPoints;
	}

	public void setTrackPoints(List<Trackpoint> trackpoints) {
		this.trackPoints = trackpoints;
	}
	
}

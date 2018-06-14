package com.brassratdev.sim.model;

import com.brassratdev.sim.model.Location;

public class Trackline {
	Location startPoint;
	Location endPoint;
	
	public Trackline() {
		this( new Location(), new Location() );
	}
	
	public Trackline(Location startPoint, Location endPoint) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}
	
	public double length(){
		return startPoint.distanceTo( endPoint );
	}
	
	public Location getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Location startPoint) {
		this.startPoint = startPoint;
	}
	public Location getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Location endPoint) {
		this.endPoint = endPoint;
	}
	
	
}

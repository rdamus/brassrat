package com.brassratdev.beeline.model;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Directions {
	static double AVG_VELOCITY = 10.0;//m/s
	List<Location> locations;
	/**time, in seconds*/
	double expectedDuration = -1.0;
	

	public Directions(){}
	
	public Directions(List<Location> locations){
		this.locations = locations;
		calculateDuration();
	}
	
	private void calculateDuration(){
		if( locations.isEmpty() || locations.size() == 1)
			return;
		
		double timeInSeconds = 0.0;
		for(int i = locations.size(); i >= 1; i--){
			double d = locations.get( i ).distanceTo( locations.get( i - 1) );
			timeInSeconds += d / AVG_VELOCITY;
		}
		setExpectedDuration( timeInSeconds );
	}
	
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
		calculateDuration();
	}
	public double getExpectedDuration(TimeUnit units) {
		switch(units){
		case MINUTES:
			return expectedDuration / 60.0;
		case HOURS:
			return expectedDuration / 3600.0;
		case SECONDS:
			default:
				return expectedDuration;
		}
		
	}
	public void setExpectedDuration(double expectedDuration) {
		this.expectedDuration = expectedDuration;
	}
	
	
}

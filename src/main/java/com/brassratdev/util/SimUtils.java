package com.brassratdev.util;

import com.brassratdev.sim.model.Location;
import com.brassratdev.sim.model.Trackline;

public class SimUtils {

	/**
	 * 
	 * @param yaw yaw in radians
	 * @return the heading, in degrees
	 */
	static public double headingFromYaw(double yaw) {
		double yawdeg = Math.toDegrees(yaw);
		return yawdeg > 0 ? (360.0 - yawdeg) : -yawdeg;
	}

	/**
	 * 
	 * @param angle yaw in radians
	 * @return the angle, in radians, between 0 and 2*PI
	 */
	static public double angleWrap(double angle) {
		if (angle < Math.PI && angle > -Math.PI)
			return angle;

		angle += Math.PI;

		angle %= 2 * Math.PI;

		return angle == 0.0 ? Math.PI : angle - Math.PI;
	}

	/**
	 * 
	 * @param lat latitude in decimal deg
	 * @param lon longitude in decimal deg
	 * @param radius the radius, in meters
	 * @return a Location that is within the radius specified
	 */
	static public Location locationWithin(double lat, double lon, double radius){
		double az = Math.random();//always between 0 - 1.0 rad
		double dx = Math.cos( az ) * radius;
		double dy = Math.sin( az ) * radius;
		return new Location(lat + dy, lon + dx);
	}
	
	/**
	 * 
	 * @param t1 Trackline of interest
	 * @return the heading along the trackline, in compass degrees
	 */
	static public double heading(Trackline t1) {
		Location start = t1.getStartPoint();
		Location end = t1.getEndPoint();
		
		double dx = end.getLongitude() - start.getLongitude();
		double dy = end.getLatitude() - start.getLatitude();
		
		double angle = Math.atan2(dy, dx);
		//return 90.0 - Math.toDegrees( angle );
		return headingFromYaw( angle - (Math.PI / 2.0 ) );
	}
}
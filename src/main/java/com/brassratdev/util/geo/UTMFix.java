package com.brassratdev.util.geo;
/**
 * An x,y fix in UTM zone coordinates.
 * @author rob
 *
 */
public class UTMFix extends GridFix{
	String utmZone;

	public UTMFix(double easting, double northing) {
		this(easting, northing, "Z");// not set
	}

	public UTMFix(double easting, double northing, String utmZone) {
		super(easting, northing);
		setUtmZone(utmZone);
	}

	public double getNorthing() {
		return getY();
	}

	public double getEasting() {
		return getX();
	}

	public void setNorthing(double northing) {
		setY(northing);
	}

	public void setEasting(double easting) {
		setX(easting);
	}

	public String getUtmZone() {
		return utmZone;
	}

	public void setUtmZone(String utmZone) {
		this.utmZone = utmZone;
	}

	@Override
	public String toString() {
		return "Easting: " + x + ",Northing:" + y + " - Zone: " + utmZone;
	}
}

package com.brassratdev.util.geo;


/**
 * Java version of the CMOOSGeodesy class
 * 
 * @author rdamus
 * 
 */
public class Geodesy {

	boolean STEP_AFTER_INIT;
	String UTMZone;
	Ellipsoid refEllipsoid;
	UTMFix originUTM;
	double metersEast;
	double metersNorth;
	double originLongitude;
	double originLatitude;
	double localGridX;
	double localGridY;

	final double FOURTHPI = Math.PI / 4;
	final double deg2rad = Math.PI / 180;
	final double rad2deg = 180.0 / Math.PI;

	public UTMFix LLtoUTM(double Lat, double Long) {

		// converts lat/long to UTM coords. Equations from USGS Bulletin 1532
		// East Longitudes are positive, West longitudes are negative.
		// North latitudes are positive, South latitudes are negative
		// Lat and Long are in decimal degrees
		// Written by Chuck Gantz- chuck.gantz@globalstar.com

		double a = refEllipsoid.radius();
		double eccSquared = refEllipsoid.ecc();
		double k0 = 0.9996;

		double LongOrigin;
		double eccPrimeSquared;
		double N, T, C, A, M;

		// Make sure the longitude is between -180.00 .. 179.9
		// -180.00
		// ..
		// 179.9;
		double LongTemp = (Long + 180) - (int) ((Long + 180) / 360) * 360 - 180;

		double LatRad = Lat * deg2rad;
		double LongRad = LongTemp * deg2rad;
		double LongOriginRad;
		int ZoneNumber;

		ZoneNumber = (int) (((LongTemp + 180) / 6) + 1);

		if (Lat >= 56.0 && Lat < 64.0 && LongTemp >= 3.0 && LongTemp < 12.0)
			ZoneNumber = 32;

		// Special zones for Svalbard
		if (Lat >= 72.0 && Lat < 84.0) {
			if (LongTemp >= 0.0 && LongTemp < 9.0)
				ZoneNumber = 31;
			else if (LongTemp >= 9.0 && LongTemp < 21.0)
				ZoneNumber = 33;
			else if (LongTemp >= 21.0 && LongTemp < 33.0)
				ZoneNumber = 35;
			else if (LongTemp >= 33.0 && LongTemp < 42.0)
				ZoneNumber = 37;
		}
		LongOrigin = (ZoneNumber - 1) * 6 - 180 + 3; // +3 puts origin in middle
		// of zone
		LongOriginRad = LongOrigin * deg2rad;

		eccPrimeSquared = (eccSquared) / (1 - eccSquared);

		N = a / Math.sqrt(1 - eccSquared * Math.sin(LatRad) * Math.sin(LatRad));
		T = Math.tan(LatRad) * Math.tan(LatRad);
		C = eccPrimeSquared * Math.cos(LatRad) * Math.cos(LatRad);
		A = Math.cos(LatRad) * (LongRad - LongOriginRad);

		M = a
				* ((1 - eccSquared / 4 - 3 * eccSquared * eccSquared / 64 - 5
						* eccSquared * eccSquared * eccSquared / 256)
						* LatRad
						- (3 * eccSquared / 8 + 3 * eccSquared * eccSquared
								/ 32 + 45 * eccSquared * eccSquared
								* eccSquared / 1024)
						* Math.sin(2 * LatRad)
						+ (15 * eccSquared * eccSquared / 256 + 45 * eccSquared
								* eccSquared * eccSquared / 1024)
						* Math.sin(4 * LatRad) - (35 * eccSquared * eccSquared
						* eccSquared / 3072)
						* Math.sin(6 * LatRad));

		double UTMEasting = (double) (k0
				* N
				* (A + (1 - T + C) * A * A * A / 6 + (5 - 18 * T + T * T + 72
						* C - 58 * eccPrimeSquared)
						* A * A * A * A * A / 120) + 500000.0);

		double UTMNorthing = (double) (k0 * (M + N
				* Math.tan(LatRad)
				* (A * A / 2 + (5 - T + 9 * C + 4 * C * C) * A * A * A * A / 24 + (61
						- 58 * T + T * T + 600 * C - 330 * eccPrimeSquared)
						* A * A * A * A * A * A / 720)));
		// 10000000 meter offset for southern hemisphere
		if (Lat < 0)
			UTMNorthing += 10000000.0;

		// compute the UTM Zone from the latitude and longitude
		UTMFix fix = new UTMFix(UTMEasting, UTMNorthing);
		fix.setUtmZone("" + ZoneNumber + UTMLetterDesignator(Lat));

		return fix;
	}

	public static char UTMLetterDesignator(double Lat) {
		// This routine determines the correct UTM letter designator for the
		// given latitude
		// returns 'Z' if latitude is outside the UTM limits of 84N to 80S
		// Written by Chuck Gantz- chuck.gantz@globalstar.com
		char LetterDesignator;

		if ((84 >= Lat) && (Lat >= 72))
			LetterDesignator = 'X';
		else if ((72 > Lat) && (Lat >= 64))
			LetterDesignator = 'W';
		else if ((64 > Lat) && (Lat >= 56))
			LetterDesignator = 'V';
		else if ((56 > Lat) && (Lat >= 48))
			LetterDesignator = 'U';
		else if ((48 > Lat) && (Lat >= 40))
			LetterDesignator = 'T';
		else if ((40 > Lat) && (Lat >= 32))
			LetterDesignator = 'S';
		else if ((32 > Lat) && (Lat >= 24))
			LetterDesignator = 'R';
		else if ((24 > Lat) && (Lat >= 16))
			LetterDesignator = 'Q';
		else if ((16 > Lat) && (Lat >= 8))
			LetterDesignator = 'P';
		else if ((8 > Lat) && (Lat >= 0))
			LetterDesignator = 'N';
		else if ((0 > Lat) && (Lat >= -8))
			LetterDesignator = 'M';
		else if ((-8 > Lat) && (Lat >= -16))
			LetterDesignator = 'L';
		else if ((-16 > Lat) && (Lat >= -24))
			LetterDesignator = 'K';
		else if ((-24 > Lat) && (Lat >= -32))
			LetterDesignator = 'J';
		else if ((-32 > Lat) && (Lat >= -40))
			LetterDesignator = 'H';
		else if ((-40 > Lat) && (Lat >= -48))
			LetterDesignator = 'G';
		else if ((-48 > Lat) && (Lat >= -56))
			LetterDesignator = 'F';
		else if ((-56 > Lat) && (Lat >= -64))
			LetterDesignator = 'E';
		else if ((-64 > Lat) && (Lat >= -72))
			LetterDesignator = 'D';
		else if ((-72 > Lat) && (Lat >= -80))
			LetterDesignator = 'C';
		else
			LetterDesignator = 'Z'; // This is here as an error flag to show
		// that the Latitude is outside the UTM
		// limits

		return LetterDesignator;
	}

	/**
	 *This method is the interface to this class and allows the client to query
	 * the amount of ground covered with respect to the origin where the origin
	 * is defined as a point in the UTM grid where we got an initial GPS fix
	 * that we defined to be the origin. What this method does not take into
	 * account is the curvature of the reference ellipsoid at a particular
	 * Lat/Lon value. Curvature influences the deltaX and deltaY that this
	 * method calculates for determining the overall distance traveled wrt the
	 * origin. Therefore, at Lat/Lon values that are significantly far enough
	 * (~300km) away from the origin of the UTM grid (0,0), a shift in one
	 * dimension, i.e. just along Latitude, or just along Longitude, does not
	 * map to a corresponding one dimensional shift in our "local" grid where we
	 * should be seeing just a deltaX or deltaY result in moving in only one
	 * direction. Instead, we have observed that moving just .0001 degrees in
	 * Longitude (~1m in local) results in both a deltaX that is coupled to a
	 * deltaY.
	 * 
	 *@param lat
	 *            The current Latitude the vehicle is at
	 *@param lon
	 *            The current Longitude the vehicle is at
	 * 
	 *@return a {@link UTMFix} indicating the distance in meters traveled
	 *         North,East in the current UTM Zone
	 */
	public UTMFix LatLong2LocalUTM(double lat, double lon) {

		// first turn the lat/lon into UTM
		double dN = 0.0, dE = 0.0;

		UTMFix utmFix = LLtoUTM(lat, lon);

		// could check for the UTMZone differing, and if so, return false

		// If this is the first time through the loop,then
		// compare the returned Northing & Easting values with the origin.
		// This does not need to be split like into before and after first
		// reading, but makes the calculation clearer. Plus, can add other
		// features like logging or publishing of value
		if (STEP_AFTER_INIT) {
			dN = utmFix.getNorthing() - getOriginNorthing();
			dE = utmFix.getEasting() - getOriginEasting();
			setMetersNorth(dN);
			setMetersEast(dE);

			STEP_AFTER_INIT = !STEP_AFTER_INIT;
		} else {
			double totalNorth = utmFix.getNorthing() - getOriginNorthing();
			dN = totalNorth - getMetersNorth();
			// add the increment to the current North value
			setMetersNorth(dN + getMetersNorth());

			double totalEast = utmFix.getEasting() - getOriginEasting();
			dE = totalEast - getMetersEast();
			// add the increment to the current East value
			setMetersEast(dE + getMetersEast());
		}

		// This is the total distance traveled thus far, either North or East
		return new UTMFix(getMetersEast(), getMetersNorth(), utmFix
				.getUtmZone());
	}

	/**
	 *Utility method for converting from a local grid fix to the global Lat,
	 * Lon pair. This method will work for small grid approximations - <300km sq
	 * 
	 * @param dfEast
	 *            The current local grid distance in meters traveled East (X
	 *            dir) wrt to Origin
	 *@param dfNorth
	 *            The current local grid distance in meters traveled North (Y
	 *            dir) wrt to Origin
	 *@return a {@link GPSFix} with the calculated lat,lon
	 */
	public GPSFix LocalGrid2LatLong(double dfEast, double dfNorth) {

		// (semimajor axis)
		double dfa = 6378137;
		// (semiminor axis)
		double dfb = 6356752;

		double dftanlat2 = Math.pow(Math.tan(getOriginLatitude() * deg2rad), 2);
		double dfRadius = dfb * Math.sqrt(1 + dftanlat2)
				/ Math.sqrt((Math.pow(dfb, 2) / Math.pow(dfa, 2)) + dftanlat2);

		// first calculate lat arc
		// returns result in rad
		double dfYArcRad = Math.asin(dfNorth / dfRadius);
		double dfYArcDeg = dfYArcRad * rad2deg;

		double dfXArcRad = Math.asin(dfEast
				/ (dfRadius * Math.cos(getOriginLatitude() * deg2rad)));
		double dfXArcDeg = dfXArcRad * rad2deg;

		// add the origin to these arc lengths
		GPSFix fix = new GPSFix();
		fix.setLatitude(dfYArcDeg + getOriginLatitude());
		fix.setLongitude(dfXArcDeg + getOriginLongitude());

		return fix;
	}

	// boolean UTM2LatLong(double dfX, double dfY, double dfLat, double dfLong)
	// {
	// }

	public boolean initialise(double lat, double lon) {
		// We will use the WGS-84 standard Reference Ellipsoid
		// This can be modified by a client if they desire with
		// @see MOOSGeodesy.h
		setRefEllipsoid(Ellipsoid.WGS84);

		// Set the Origin of the local Grid Coordinate System
		setOriginLatitude(lat);
		setOriginLongitude(lon);

		// Translate the Origin coordinates into Northings and Eastings
		// Then set the Origin for the Northing/Easting coordinate frame
		// Also make a note of the UTM Zone we are operating in
		setOriginUTM(LLtoUTM(lat, lon));

		// We set this flag to indicate that the first calculation of distance
		// traveled is with respect to the origin coordinates.
		setSTEP_AFTER_INIT(true);

		return true;
	}

	public static double DecDeg2DecMin(double dfDecDeg) {
		return GPSUtils.decimalMinutes(dfDecDeg);
	}

	public GridFix LatLong2LocalGrid(double lat, double lon) {

		// (semimajor axis)
		double dfa = 6378137;
		// (semiminor axis)
		double dfb = 6356752;

		double dftanlat2 = Math.pow(Math.tan(lat * deg2rad), 2);
		double dfRadius = dfb * Math.sqrt(1 + dftanlat2)
				/ Math.sqrt((Math.pow(dfb, 2) / Math.pow(dfa, 2)) + dftanlat2);

		// the decimal degrees conversion should take place elsewhere
		double dXArcDeg = (lon - getOriginLongitude()) * deg2rad;
		double dX = dfRadius * Math.sin(dXArcDeg) * Math.cos(lat * deg2rad);

		double dYArcDeg = (lat - getOriginLatitude()) * deg2rad;
		double dY = dfRadius * Math.sin(dYArcDeg);

		// This is the total distance traveled thus far, either North or East
		setLocalGridX(dX);
		setLocalGridY(dY);

		return new GridFix(getLocalGridX(), getLocalGridY());
	}

	public static double DMS2DecDeg(double dfVal) {
		return GPSUtils.decimalValue(dfVal);
	}

	public boolean isSTEP_AFTER_INIT() {
		return STEP_AFTER_INIT;
	}

	public void setSTEP_AFTER_INIT(boolean step_after_init) {
		STEP_AFTER_INIT = step_after_init;
	}

	public double getOriginEasting() {
		return originUTM.getEasting();
	}

	public void setOriginEasting(double originEasting) {
		this.originUTM.setEasting(originEasting);
	}

	public double getOriginNorthing() {
		return originUTM.getNorthing();
	}

	public void setOriginNorthing(double originNorthing) {
		this.originUTM.setNorthing(originNorthing);
	}

	public double getMetersEast() {
		return metersEast;
	}

	public void setMetersEast(double metersEast) {
		this.metersEast = metersEast;
	}

	public double getMetersNorth() {
		return metersNorth;
	}

	public void setMetersNorth(double metersNorth) {
		this.metersNorth = metersNorth;
	}

	public double getOriginLongitude() {
		return originLongitude;
	}

	public void setOriginLongitude(double originLongitude) {
		this.originLongitude = originLongitude;
	}

	public double getOriginLatitude() {
		return originLatitude;
	}

	public void setOriginLatitude(double originLatitude) {
		this.originLatitude = originLatitude;
	}

	public double getLocalGridX() {
		return localGridX;
	}

	public void setLocalGridX(double localGridX) {
		this.localGridX = localGridX;
	}

	public double getLocalGridY() {
		return localGridY;
	}

	public void setLocalGridY(double localGridY) {
		this.localGridY = localGridY;
	}

	public Ellipsoid getRefEllipsoid() {
		return refEllipsoid;
	}

	public void setRefEllipsoid(Ellipsoid refEllipsoid) {
		this.refEllipsoid = refEllipsoid;
	}

	public UTMFix getOriginUTM() {
		return originUTM;
	}

	public void setOriginUTM(UTMFix originUTM) {
		this.originUTM = originUTM;
	}

}

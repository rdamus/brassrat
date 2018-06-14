package com.brassratdev.util.geo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class GPSUtils {
	protected static SimpleDateFormat dateConvert = new SimpleDateFormat(
			"ddMMyyzHHmmss");
	protected static SimpleDateFormat UTCDateConvert = new SimpleDateFormat(
			"HHmmss");
	protected static SimpleDateFormat f = new SimpleDateFormat("ddMMyy");

	public enum NMEAType {
		GGA, // Fix Information
		RMC // Recommended Minimum Data
	}

	public enum NMEASentence {
		$GPGGA, $GPGSA, $GPRMC
	}

	public enum GGAFields {
		GGA, UTC, Lat, LatDir, Lon, LonDir, FixQual, NumSat, HDOP, Alt, AltM, GEOID, GEOIDM, DGPSLast, DGPSID, CS
	}

	public enum RMCFields {
		RMC, UTC, Status, Lat, LatDir, Lon, LonDir, SOG, Track, Date, Mag, MagDir, CS
	}

	public static String DELIM = ",";

	public static double parseLatitude(String NMEAString) throws Exception {
		String[] tokens = NMEAString.split(DELIM);
		System.out.println("parseLatitude(NMEAString: " + NMEAString);
		if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPGGA.name())) {
			return parseLatitude(tokens[GGAFields.Lat.ordinal()],
					tokens[GGAFields.LatDir.ordinal()]);
		} else if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPRMC.name())) {
			return parseLatitude(tokens[RMCFields.Lat.ordinal()],
					tokens[RMCFields.LatDir.ordinal()]);
		} else {
			throw new Exception("Can only parse GGA and RMC NMEA Strings");
		}
	}

	public static double parseLongitude(String NMEAString) throws Exception {
		String[] tokens = NMEAString.split(DELIM);
		if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPGGA.name())) {
			return parseLongitude(tokens[GGAFields.Lon.ordinal()],
					tokens[GGAFields.LonDir.ordinal()]);
		} else if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPRMC.name())) {
			return parseLongitude(tokens[RMCFields.Lon.ordinal()],
					tokens[RMCFields.LonDir.ordinal()]);
		} else {
			throw new Exception("Can only parse GGA and RMC NMEA Strings");
		}
	}

	public static double parseLatitude(String lat, String dir) {
		return latitudeDir(dir) * decimalValue(lat);
	}

	public static double parseLongitude(String lon, String dir) {
		return longitudeDir(dir) * decimalValue(lon);
	}

	public static Date parseUTCDate(String NMEAString) throws Exception {
		String[] tokens = NMEAString.split(DELIM);

		if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPGGA.name())) {
			// GGA date is GMT time
			Date GGATime = UTCDateConvert
					.parse(tokens[GGAFields.UTC.ordinal()]);// + "GMT");
			// if the time today is between the timezone offset and midnight,
			// add day to the GGA time
			Calendar today = Calendar.getInstance();
			int flipOverHr = (24 + (today.getTimeZone().getRawOffset() / 3600000)) % 24;
			int day = today.get(Calendar.DAY_OF_YEAR);
			int hr = today.get(Calendar.HOUR_OF_DAY);
			int yr = today.get(Calendar.YEAR);
			if (hr > flipOverHr)
				day += 1;
			Calendar gpsTime = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
			gpsTime.setTime(GGATime);
			gpsTime.set(Calendar.YEAR, yr);
			gpsTime.set(Calendar.DAY_OF_YEAR, day);
			return gpsTime.getTime();
		} else if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPRMC.name())) {
			String time = tokens[RMCFields.Date.ordinal()] + "GMT"
					+ tokens[RMCFields.UTC.ordinal()];
			return dateConvert.parse(time);
		} else {
			throw new Exception("Can only parse GGA and RMC NMEA Strings");
		}
	}

	/**
	 * converts UTC date to the {@link TimeZone} specified by toZone
	 * 
	 * @param origDate
	 *            the date to convert. must be in UTC (GMT+00:00) time
	 * @param toZone
	 *            TimeZone to convert to
	 * @return the UTC time in the specified TimeZone
	 */
	public static Date convertUTCDate(Date origDate, TimeZone toZone) {
		Calendar c = Calendar.getInstance(toZone);
		c.setTime(origDate);
		return c.getTime();
	}

	/**
	 * shifts a given UTC date to the {@link TimeZone} specified by toZone, thus
	 * changing the original date
	 * 
	 * @param origDate
	 *            the date to convert. must be in UTC (GMT+00:00) time
	 * @param toZone
	 *            TimeZone to convert to
	 * @return a shifted the UTC time in the specified TimeZone
	 */
	public static Date shiftUTCDate(Date origDate, TimeZone toZone) {
		return new Date(origDate.getTime() + toZone.getRawOffset());
	}

	/**
	 * 
	 * @param dir
	 *            latitude direction
	 * @return -1.0 if dir is "S", 1.0 if dir is "N"
	 */
	public static double latitudeDir(String dir) {
		return (dir.equals("N") ? 1.0 : -1.0);
	}

	/**
	 * 
	 * @param dir
	 *            longitude direction
	 * @return -1.0 if dir is "W", 1.0 if dir is "E"
	 */
	public static double longitudeDir(String dir) {
		return (dir.equals("E") ? 1.0 : -1.0);
	}

	public static String latitudeDir(double lat) {
		return lat > 0.0 ? "N" : "S";
	}

	public static String longitudeDir(double lon) {
		return lon > 0.0 ? "E" : "W";
	}

	/**
	 * for converting an NMEA decimal minute value to decimal degrees
	 * 
	 * @param latlon
	 *            NMEA decimal minute value
	 * @return decimal degree equivalent
	 */
	public static double decimalValue(double latlon) {
		double dec = (latlon % 100.0) / 60.0;
		double deg = (latlon / 100.0) - ((latlon % 100) / 100);

		return deg + dec;
	}

	/**
	 * for converting an NMEA decimal minute value to decimal degrees
	 * 
	 * @param latlon
	 *            NMEA decimal minute value
	 * @return decimal degree equivalent
	 */
	public static double decimalValue(String latlon) {
		return decimalValue(Double.parseDouble(latlon));
	}

	/**
	 * for converting decimal degrees value to decimal minutes
	 * 
	 * @param decDeg
	 *            NMEA decimal degree input
	 * @return the lat/lon in decimal minutes
	 */
	public static double decimalMinutes(double decDeg) {
		int nDeg = (int) decDeg;

		double dfDecMin = (decDeg - (double) nDeg) * 60.0;

		return ((100.0 * (double) nDeg) + dfDecMin);
	}

		
	/**
	 * example GGA
	 * $GPGGA,123519,4807.038,N,01131.000,E,1,08,0.9,545.4,M,46.9,M,,*47
	 * 
	 * @return the {@link GPSFix} as a GGA string from a
	 *         {@link GPSFix}
	 * 
	 */
	public static String toGGA(GPSFix fix) {
		StringBuffer b = new StringBuffer();
		b.append(NMEASentence.$GPGGA.name());
		b.append(",");
		b.append(UTCDateConvert.format(fix.getTime()));
		b.append(",");
		b.append(Math.abs(decimalMinutes(fix.getLatitude())));
		b.append(",");
		b.append(latitudeDir(fix.getLatitude()));
		b.append(",");
		b.append(Math.abs(decimalMinutes(fix.getLongitude())));
		b.append(",");
		b.append(longitudeDir(fix.getLongitude()));
		b.append(",");
		if (fix.isValid()) {
			b.append("1,");
			b.append("03,");
		} else {
			b.append("0,");
			b.append("00,");
		}
		b.append("0.0,0.0,M,0.0,M,,*00");

		return b.toString();
	}


	/**
	 * 
	 * @param NMEAString
	 *            NMEA compatible string
	 * @param zone
	 *            the {@link TimeZone} to convert the fix time to
	 * @return a {@link GPSFix} containing the parsed lat/lon/time of the NMEA
	 *         input
	 * @throws Exception
	 *             if the string cannot be parsed
	 * @see parseFix(String) method
	 */
	public static GPSFix parseFix(String NMEAString, TimeZone zone)
			throws Exception {
		double lat = 0.0;
		double lon = 0.0;

		GPSFix fix = new GPSFix(lat, lon, new Date());
		fix.setLatitude(parseLatitude(NMEAString));
		fix.setLongitude(parseLongitude(NMEAString));
		fix.setTime(parseUTCDate(NMEAString));
		fix.setValid(parseValidity(NMEAString));
		fix.setAltitude(parseAltitude(NMEAString));
		return fix;
	}

	/**
	 * @param string
	 * @return
	 */
	private static boolean parseValidity(String NMEAString) throws Exception {
		String[] tokens = NMEAString.split(DELIM);
		String valid = new String();
		if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPGGA.name())) {
			valid = tokens[GGAFields.FixQual.ordinal()];
		} else if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPRMC.name())) {
			valid = tokens[RMCFields.Status.ordinal()];
		} else {
			throw new Exception("Can only parse GGA and RMC NMEA Strings");
		}
		return valid.equals("V") ? false : true;// V is void
	}

	/**
	 * 
	 * @param NMEAString
	 *            NMEA compatible string
	 * @return a {@link GPSFix} containing the parsed lat/lon/time of the NMEA
	 *         input
	 * @throws Exception
	 *             if the string cannot be parsed
	 * @see {@link GGAFields}, {@link RMCFields}
	 */
	public static GPSFix parseFix(String NMEAString) throws Exception {
		return parseFix(NMEAString, TimeZone.getDefault());
	}
	
	/**
	 * converts time to a date object
	 * @param time time, in decimal seconds, since January 1, 1970 00:00:00.000 GMT.
	 * @return
	 */
	public static Date convert(double time){
		double timeInMs = time * 1000.0;
		long lTime = (long)timeInMs;
		return new Date(lTime);
	}
	
	public static double parseAltitude(String NMEAString) throws Exception{
		String[] tokens = NMEAString.split(DELIM);
		System.out.println("parseAltitude(NMEAString: " + NMEAString);
		if (NMEAString.startsWith(GPSUtils.NMEASentence.$GPGGA.name())) {
			return Double.parseDouble( tokens[GGAFields.Alt.ordinal()]);
		} else {
			throw new Exception("Can only parse GGA NMEA Strings");
		}
	}
}

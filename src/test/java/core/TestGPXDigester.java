package core;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

import com.brassratdev.data.vcc.CapturedTrack;
import com.brassratdev.data.vcc.DeviceInfo;
import com.brassratdev.data.vcc.Trackpoint;
import com.brassratdev.data.vcc.Trackpoints;
import com.brassratdev.data.vcc.VelocitekControlCenter;
import com.brassratdev.io.vcc.VccDigester;
import com.brassratdev.net.JsonHttpService;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.Placemark;

public class TestGPXDigester {
	private static final Logger log = LogManager.getLogger(TestGPXDigester.class);
	VccDigester digester;
	Path path;
	Path kmlPath;

	@Before
	public void setUp() throws Exception {
		digester = new VccDigester();
		path = Paths.get("d:\\", "Wabbit", "20140418",
				"EYC-Beercan-Apr18_2014_542PM.vcc");
		// path = Paths.get("d:\\", "Wabbit", "20140418", "test.vcc");
		kmlPath = Paths.get("d:\\", "Wabbit", "20140418", "test.kml");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	public void testVcc() {
		VelocitekControlCenter vcc = digester.parse(path);
		print(vcc);
	}

	@Ignore
	public void testCreateControlCenter() throws IOException, SAXException {
		Digester digester = new Digester();
		digester.addObjectCreate("VelocitekControlCenter",
				"com.brassratdev.data.vcc.VelocitekControlCenter");

		digester.addSetProperties("VelocitekControlCenter");
		// capturedtrack
		digester.addObjectCreate("VelocitekControlCenter/CapturedTrack",
				"com.brassratdev.data.vcc.CapturedTrack");
		digester.addSetProperties("VelocitekControlCenter/CapturedTrack");
		// digester.addObjectCreate("VelocitekControlCenter/CapturedTrack/Trackpoint",
		// "com.brassratdev.data.vcc.Trackpoint");

		// digester.addSetProperties("VelocitekControlCenter/CapturedTrack/Trackpoint");
		digester.addBeanPropertySetter("VelocitekControlCenter/CapturedTrack",
				"minLatitude");

		digester.addSetNext("VelocitekControlCenter/CapturedTrack", "setTrack",
				"com.brassratdev.data.vcc.CapturedTrack");
		digester.addBeanPropertySetter("maxLatitude");
		digester.addBeanPropertySetter("minLongitude");
		digester.addBeanPropertySetter("maxLongitude");

		// digester.addSetNext("VelocitekControlCenter/CapturedTrack/Trackpoint",
		// "addPoint",
		// "com.brassratdev.data.vcc.TrackPoint");

		VelocitekControlCenter vcc = digester.parse(path.toFile());
		print(vcc);
	}

	@Ignore
	public void testRulesModule() throws IOException, SAXException {
		DigesterLoader loader = DigesterLoader.newLoader(new TrackModule());
		Digester digester = loader.newDigester();

		VelocitekControlCenter vcc = digester.parse(path.toFile());
		print(vcc);
	}

	@Ignore
	public void testVccDigester() throws IOException, SAXException {
		VccDigester digester = new VccDigester();

		VelocitekControlCenter vcc = digester.parse(path);
		print(vcc);
	}

	@Test
	public void testVccDigesterWithKml() throws IOException, SAXException {
		VccDigester digester = new VccDigester();

		VelocitekControlCenter vcc = digester.parse(path);
		writeKmlColoredLineStringPath(vcc);
	}

	@Ignore
	public void testMaxVel() throws IOException, SAXException {
		VccDigester digester = new VccDigester();

		VelocitekControlCenter vcc = digester.parse(path);
		maxVel(vcc);
	}

	
	void writeKmlLineStringPath(VelocitekControlCenter vcc)
			throws FileNotFoundException {
		Kml kml = KmlFactory.createKml();
		Placemark p = kml.createAndSetPlacemark();
		LineString l = p.createAndSetLineString();
		int kCnt = 0;
		for (Trackpoint t : vcc.getTrack().getTrackPoints().getTrackPoints()) {
			log.info("writing trackpoint " + ++kCnt);
			l.addToCoordinates(t.getLongitude(), t.getLatitude());
		}
		kml.marshal(kmlPath.toFile());
	}

	void filter(List<Trackpoint> points){
		ArrayList<Trackpoint> pointsToRemove = new ArrayList<>();
		for (Trackpoint t : points) {
			if( t.getSpeed() == 0f ){
				pointsToRemove.add( t );
			}
		}
		System.out.println("removing " + pointsToRemove.size());
		points.removeAll( pointsToRemove );
	}
	
	void writeKmlColoredLineStringPath(VelocitekControlCenter vcc) throws FileNotFoundException {
		List<Trackpoint> points = vcc.getTrack().getTrackPoints().getTrackPoints();
		filter( points );
		writeKml( points );
	}
	
	void writeKml(List<Trackpoint> points) throws FileNotFoundException {
		Kml kml = KmlFactory.createKml();
		Document d = kml.createAndSetDocument();
		for (int i = 1; i < points.size(); i++){
			String name = "trackpoint_" + i;
			Trackpoint start = points.get(i-1);
			Trackpoint end = points.get(i);
			
			Placemark p = d.createAndAddPlacemark().withName(name);
			p.createAndAddStyle().createAndSetLineStyle()
					.withColor( colorForSpeed( end.getSpeed() ) )
					.withWidth(2.0);
			LineString ls = p.createAndSetLineString();
			ls.addToCoordinates(start.getLongitude(), start.getLatitude());
			ls.addToCoordinates(end.getLongitude(), end.getLatitude());
		}
		kml.marshal(kmlPath.toFile());
	}
	
	void maxVel(VelocitekControlCenter vcc) {
		
		List<Trackpoint> points = vcc.getTrack().getTrackPoints().getTrackPoints();
		float maxVel = 0.0f;
		for (Trackpoint t:points){
			if( t.getSpeed() > maxVel )
				maxVel = t.getSpeed();
		}
		System.out.println("maxVel is: " + maxVel);
	}

	// /@return hex color string
	String colorForSpeed(float vel) {
		int rgb = (int) ((vel / 8.126f) * 1024);
		Color c = new Color(rgb);
		String hex = Integer.toHexString(c.getRGB());
		System.out.println("rgb: " + rgb + " as hex: " + hex);
		
		return hex;
	}

	void print(VelocitekControlCenter vcc) {
		System.out.println("downloaded: " + vcc.getTrack().getDownloadedOn());
		System.out.println("name      : " + vcc.getTrack().getName());
		System.out.println("# trk pts : " + vcc.getTrack().getNumberTrkpts());
		// System.out.println("max lat   : " + vcc.getTrack().getMaxLatitude());
		// System.out.println("min lat   : " + vcc.getTrack().getMinLatitude());
		// System.out.println("max lon   : " +
		// vcc.getTrack().getMaxLongitude());
		// System.out.println("min lon   : " +
		// vcc.getTrack().getMinLongitude());
		System.out.println("trkpts sze: "
				+ vcc.getTrack().getTrackPoints().getTrackPoints().size());
		for (Trackpoint t : vcc.getTrack().getTrackPoints().getTrackPoints()) {
			System.out.println("trackpoint hdg: " + t.getHeading());
			System.out.println("trackpoint lat: " + t.getLatitude());
			System.out.println("trackpoint lon: " + t.getLongitude());
			System.out.println("trackpoint vel: " + t.getSpeed());
		}
	}
}

class GPXTrackModule extends AbstractRulesModule {
	@Override
	protected void configure() {
		forPattern("VelocitekControlCenter").createObject().ofType(
				VelocitekControlCenter.class);

		forPattern("VelocitekControlCenter/CapturedTrack").createObject()
				.ofType(CapturedTrack.class).then().setProperties().then()
				.setNext("setTrack");
		// does not matter where we put this, all that matters is proper
		// treatment of
		// the capitalization of the property name -i.e., minLatitude *not*
		// MinLatitude

		forPattern("VelocitekControlCenter/CapturedTrack/Trackpoints")
				.createObject().ofType(Trackpoints.class).then()
				.setProperties().then().setNext("setTrackPoints");

		forPattern("VelocitekControlCenter/CapturedTrack/DeviceInfo")
				.createObject().ofType(DeviceInfo.class).then().setProperties()
				.then().setNext("setDeviceInfo");

		forPattern(
				"VelocitekControlCenter/CapturedTrack/Trackpoints/Trackpoint")
				.createObject().ofType(Trackpoint.class).then().setProperties()
				.then().setNext("addPoint");

	}
}

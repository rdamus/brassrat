package com.brassratdev.io.vcc;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

import com.brassratdev.XMLDigester;
import com.brassratdev.data.vcc.CapturedTrack;
import com.brassratdev.data.vcc.DeviceInfo;
import com.brassratdev.data.vcc.Trackpoint;
import com.brassratdev.data.vcc.Trackpoints;
import com.brassratdev.data.vcc.VelocitekControlCenter;

public class VccDigester extends XMLDigester {

	public VccDigester() {
		init();
	}

	protected void init() {
		digester = DigesterLoader.newLoader( new VelocitekRulesModule() ).newDigester();
	}

	public VelocitekControlCenter parse(Path path) {
		try {
			return digester.parse(path.toFile());
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

class VelocitekRulesModule extends AbstractRulesModule{
	@Override
	protected void configure(){
		forPattern("VelocitekControlCenter")
				.createObject()
				.ofType(VelocitekControlCenter.class);
		
		forPattern("VelocitekControlCenter/CapturedTrack")
				.createObject()
				.ofType(CapturedTrack.class)
				.then()
				.setProperties()
				.then()
				.setNext("setTrack");
		//does not matter where we put this, all that matters is proper treatment of 
		//the capitalization of the property name -i.e., minLatitude *not* MinLatitude
	
		forPattern("VelocitekControlCenter/CapturedTrack/Trackpoints")
				.createObject()
				.ofType(Trackpoints.class)
				.then()
				.setProperties()
				.then()
				.setNext("setTrackPoints");
		
		forPattern("VelocitekControlCenter/CapturedTrack/DeviceInfo")
				.createObject()
				.ofType(DeviceInfo.class)
				.then()
				.setProperties()
				.then()
				.setNext("setDeviceInfo");
				
		forPattern("VelocitekControlCenter/CapturedTrack/Trackpoints/Trackpoint")
				.createObject()
				.ofType(Trackpoint.class)
				.then()
				.setProperties()
				.then()
				.setNext("addPoint");
		
	}
}
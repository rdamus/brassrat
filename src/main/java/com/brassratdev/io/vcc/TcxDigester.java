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

public class TcxDigester extends XMLDigester {

	public TcxDigester() {
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

class TcxRulesModule extends AbstractRulesModule{
	@Override
	protected void configure(){
		forPattern("VelocitekControlCenter")
				.createObject()
				.ofType(TrainingCenterDatabase.class);
		
		forPattern("TrainingCenterDatabase/Activities")
				.createObject()
				.ofType(Activities.class)
				.then()
				.setProperties()
				.then()
				.setNext("setTrack");
		//does not matter where we put this, all that matters is proper treatment of 
		//the capitalization of the property name -i.e., minLatitude *not* MinLatitude
	
		forPattern("TrainingCenterDatabase/Activities/Activity")
				.createObject()
				.ofType(Trackpoints.class)
				.then()
				.setProperties()
				.then()
				.setNext("setActivity");
		
		forPattern("TrainingCenterDatabase/Activities/Activity/Id")
				.createObject()
				.ofType(DeviceInfo.class)
				.then()
				.setProperties()
				.then()
				.setNext("setActivityId");
		forPattern("TrainingCenterDatabase/Activities/Activity/Notes")
		.createObject()
		.ofType(DeviceInfo.class)
		.then()
		.setProperties()
		.then()
		.setNext("setActivityNotes");
		forPattern("TrainingCenterDatabase/Activities/Activity/Lap")
		.createObject()
		.ofType(Lap.class)
		.then()
		.setProperties()
		.then()
		.setNext("addLap");	
		forPattern("VelocitekControlCenter/CapturedTrack/Trackpoints/Trackpoint")
				.createObject()
				.ofType(Trackpoint.class)
				.then()
				.setProperties()
				.then()
				.setNext("addPoint");
		
	}
}
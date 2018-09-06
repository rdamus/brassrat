package com.brassratdev.io.fb;

import java.io.IOException;
import java.nio.file.Path;

import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.xml.sax.SAXException;

import com.brassratdev.XMLDigester;
import com.brassratdev.data.fb.FbData;
import com.brassratdev.data.fb.FbResponse;
import com.brassratdev.data.fb.IPAddress;
import com.brassratdev.data.fb.IPAddresses;

public class FbXmlDigester extends XMLDigester {

	public FbXmlDigester() {
		init();
	}

	protected void init() {
		digester = DigesterLoader.newLoader( new FbRulesModule() ).newDigester();
	}

	public FbResponse parse(Path path) {
		try {
			return digester.parse(path.toFile());
		} catch (IOException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

class FbRulesModule extends AbstractRulesModule{
	@Override
	protected void configure(){
		forPattern("response")
				.createObject()
				.ofType(FbResponse.class);
		
		forPattern("response/data")
				.createObject()
				.ofType(FbData.class)
				.then()
				.setProperties()
				.then()
				.setNext("setData");
		//does not matter where we put this, all that matters is proper treatment of 
		//the capitalization of the property name -i.e., minLatitude *not* MinLatitude
	
		forPattern("response/data/ip_addresses")
				.createObject()
				.ofType(IPAddresses.class)
				.then()
				.setProperties()
				.then()
				.setNext("setIPAddresses");
				
		forPattern("response/data/ip_addresses/ip_address")
				.createObject()
				.ofType(IPAddress.class)
				.then()
				.setProperties()
				.then()
				.setNext("addIPAddress");
		
	}
}
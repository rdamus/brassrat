package core;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.brassratdev.data.fb.FbResponse;
import com.brassratdev.data.fb.IPAddress;
import com.brassratdev.io.fb.FbXmlDigester;


public class TestFbXmlDigester {
	static Logger logger = Logger.getLogger(TestFbXmlDigester.class);
	FbXmlDigester digester;
	Path path;
	
	@Before
	public void setUp() throws Exception {
		digester = new FbXmlDigester();
		path = Paths.get("c:\\", "Users", "rob", "Downloads",
				"4845788786427071529810711.xml");

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFbXML() {
		FbResponse response = digester.parse(path);
		print(response);
	}

	@Test
	public void testMapConversion() {
		FbResponse response = digester.parse(path);
		convertToMap(response);
	}
	

	void print(FbResponse response) {
		System.out.println("# ip addresses: " + response.getData().getIPAddresses().getAddresses().size());
		for(IPAddress ip:response.getData().getIPAddresses().getAddresses()){
			long time = Long.parseLong(ip.getTime()) * 1000L;//need ms
			System.out.println("ip address: " + ip.getIp() + ", time: " + ip.getTime()
			+ " - as date: " + new Date(time));
		}
	}
	
	void convertToMap(FbResponse response){
		Map<String,TreeSet<Date>> m = new TreeMap<>();
		for(IPAddress ip:response.getData().getIPAddresses().getAddresses()){
			long time = Long.parseLong(ip.getTime()) * 1000L;//need ms
			String addr = ip.getIp();
			if(!m.containsKey(addr)){ m.put(addr, new TreeSet<Date>());}
			m.get(addr).add(new Date(time));
		}
		print(m);
	}

	private void print(Map<String, TreeSet<Date>> m) {
		System.out.println("# unique ip addresses: " + m.size() + " and the times associated with each ip");
		m.forEach((k, v) -> System.out.println((k + ":" + v)));
	}

	
}


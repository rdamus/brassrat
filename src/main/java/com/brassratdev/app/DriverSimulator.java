package com.brassratdev.app;
import java.util.List;

import com.brassratdev.beeline.model.DefaultDriver;
import com.brassratdev.beeline.model.Driver;
import com.brassratdev.beeline.model.Job;
import com.brassratdev.beeline.model.Location;
import com.brassratdev.dispatch.Context;

/**
 * XXX:WIP
 * 
 * @author rdamus
 *
 */
public class DriverSimulator{
	Context context = new Context();

	public static void main(String[] args){
	    DriverSimulator a = new DriverSimulator();
	    a.run();
    }

	public void run() {
		// start the drivers looking
		context.addDriver(new DefaultDriver("joe", 1L));
		context.addDriver(new DefaultDriver("rob", 2L));
		context.addDriver(new DefaultDriver("tom", 3L));

		Job job = retrieve();

		for (Driver driver : context.getDrivers()) {
			driver.addJob(job);
		}
	}
   
    private Job retrieve(){
    	return new Job( new Location(), new Location() );
    }
}

class Directions{
	List<Location> locations;
}









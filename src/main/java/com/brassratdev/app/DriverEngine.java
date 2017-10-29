package com.brassratdev.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;

import com.brassratdev.AbstractEngine;
import com.brassratdev.beeline.model.DefaultDriver;
import com.brassratdev.beeline.model.Driver;
import com.brassratdev.beeline.model.DriverStatus;
import com.brassratdev.beeline.model.Job;
import com.brassratdev.beeline.model.Location;
import com.brassratdev.beeline.model.Job.JobStatus;
import com.brassratdev.dispatch.io.ActiveMQPublisher;
import com.brassratdev.dispatch.io.DestinationFactory;
import com.brassratdev.dispatch.io.DestinationFactory.QUEUE;

public class DriverEngine extends AbstractEngine {

    private int noDrivers;
    List<Driver> drivers = new ArrayList<>();
    DriverRegistration driverRegistration = new MockDriverRegistration();
    DriverPublisher driverPublisher = new MockDriverPublisher();

    public DriverEngine() {
	super("DriverEngine");
    }

    public void init() {
	noDrivers = 10;
	// create N drivers
	createDrivers();
	// register by calling in
	registerDrivers();
    }

    @Override
    protected void runEngine() {
	// iterate over the drivers
	// move them towards their destination
	updateDriverLocations();
	// update their status based on the state machine
	publishDriverInfo();
    }

    protected void createDrivers() {
	for (int i = 0; i < noDrivers; i++) {
	    drivers.add(DriverFactory.createDriver());
	}
    }

    protected void registerDrivers() {
	driverRegistration.register(drivers);
    }

    private void publishDriverInfo() {
	driverPublisher.publish(drivers);
    }

    private void updateDriverLocations() {
	for (Driver d : drivers) {
	    DriverStatus s = d.getStatus();
	    switch (s) {
	    case AVAILABLE:
		doAvailable(d);
		break;
	    case UNAVAILABLE:
		doUnavailable(d);
	    }
	}
    }

    private void doAvailable(Driver driver) {
	
    }

    private void doUnavailable(Driver driver) {
	
	Queue<Job> jobs = driver.getJobQueue();
	Job j = jobs.peek();
	//if he has no jobs, we should set his status to available
	//xxx: this could be UNITNIALIZED for a more generic "driver"
	if( j == null ){
	    if( driver instanceof DefaultDriver)
		((DefaultDriver)driver).setStatus( DriverStatus.AVAILABLE );
	}else{
	    
	    Location l = driver.getLocation();
	    Location dest = j.getDropoff();
	    //calculate distance to go, bearing
	    //see if we are within the goal radius
	    
	    //if we are close enough, set job status to complete
	    j.setStatus( JobStatus.Complete );
	    if( !jobs.remove( j ) ){//throw}
		
	    }
	    if( jobs.isEmpty() ){
	    //ask the order service for close jobs?
	    }else{
		//iterate fwd...
		driver.setLocation(null);
	    }
	}
    }

}

class DriverFactory {
    private static Long driverId = 0L;

    static public Driver createDriver() {
	long id = driverId++;
	System.out.println("creating driver id: " + id);
	return new DefaultDriver("driver-" + id, id, createLocation());
    }

    private static Location createLocation() {
	double lat = 40.0 + Math.random() * 5.0;
	double lon = -120 + Math.random() * 5.0;
	return new Location(new Float(lat), new Float(lon));
    }
}

abstract class DriverPublisher {
    abstract void publish(List<Driver> drivers);
}

class MockDriverPublisher extends DriverPublisher {
    void publish(List<Driver> drivers) {
	for (Driver d : drivers) {
	    // print location
	    System.out.println("driver update: " + d.toString());
	}
    }
}

abstract class DriverRegistration {
    abstract void register(List<Driver> drivers);
}

class MockDriverRegistration extends DriverRegistration {
    void register(List<Driver> drivers) {
	for (Driver d : drivers) {
	    // call in
	    System.out.println("registering " + d.toString());
	}
    }
}

class JMSDriverRegistration extends DriverRegistration {
    final String brokerURL = "tcp://localhost:61616";
    ActiveMQPublisher publisher;

    void register(List<Driver> drivers) {
	for (Driver d : drivers) {
	    try {
		notify(createRegistrationMsg(d), QUEUE.DRIVERS, "register");
	    } catch (JMSException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	}
    }

    MapMessage createRegistrationMsg(Driver driver) throws JMSException {
	MapMessage msg = publisher.getSession().createMapMessage();
	msg.setString("name", driver.getName());
	msg.setLong("id", driver.getId());
	msg.setBoolean("register", true);
	return msg;
    }

    MapMessage createLocationMsg(Driver driver) throws JMSException {
	MapMessage msg = publisher.getSession().createMapMessage();
	msg.setString("name", driver.getName());
	msg.setLong("id", driver.getId());
	msg.setFloat("lat", driver.getLocation().getLatitude());
	msg.setFloat("lon", driver.getLocation().getLongitude());
	return msg;
    }

    MapMessage createDriverStatusMsg(String status, Driver driver) throws JMSException {
	MapMessage msg = publisher.getSession().createMapMessage();
	msg.setString("name", driver.getName());
	msg.setLong("id", driver.getId());
	msg.setString("status", status);
	return msg;
    }

    protected void notify(Message msg, QUEUE queue, String category)
	    throws JMSException {
	Destination dest = DestinationFactory.createQueue(publisher, queue,
		category);
	publisher.sendMessage(msg, dest);
    }
}

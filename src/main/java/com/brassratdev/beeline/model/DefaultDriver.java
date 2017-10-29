package com.brassratdev.beeline.model;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Queue;

/**
 * A default implementation of the {@link Driver} interface.
 * 
 * This class maintains a queue of {@link Job}s and exposes the
 * {@link #addJob(Job)} and {@link #deliver(Job)} (TODO:deprecate) methods which
 * are the primary means of interacting with a Driver.
 * 
 * @author rdamus
 * 
 */
public class DefaultDriver implements Driver {
    ///unique id of the driver
    Long id;
    ///current location of the driver
    Location location;
    ///driver name - is a unique string containing the driver id
    String name;
    ///the queue of jobs that the driver is operating on
    Queue<Job> jobs = new ArrayDeque<Job>(MAX_QUEUE_SIZE);
    ///status
    DriverStatus status = DriverStatus.AVAILABLE;

    public DefaultDriver(String name, Long id) {
	this.location = new Location();
	this.name = name;
	this.id = id;
    }
    
    public DefaultDriver(String name, Long id, Location loc) {
	this.location = loc;
	this.name = name;
	this.id = id;
    }

    public void addJob(Job job) {
	jobs.offer(job);
    }

    public String getName() {
	return name;
    }

    public Long getId() {
	return id;
    }

    public void setLocation(Location loc) {
	this.location = loc;
    }

    public Location getLocation() {
	return location;
    }

    public Queue<Job> getJobQueue() {
	return jobs;
    }

    public DriverStatus getStatus() {
        return status;
    }

    public void setStatus(DriverStatus status) {
        this.status = status;
    }

    public String toString(){
	return getName() + ", loc: " + getLocation().toString();
    }
}

package com.brassratdev.beeline.model;

import java.util.Date;

/**
 * A Job is a unit of work that must be accomplished, such as an order from a
 * restaurant for delivery.
 * 
 * @author rdamus
 * 
 */
public class Job {

    // /unique id for this Job
    Long id;
    // /reference to an external order, may be null
    Long orderId = null;
    // /the location to pickup the payload
    Location pickup;
    // /the location to drop off the payload
    Location dropoff;
    // /the time that the dropoff must occur by
    Date deliveryTime;
    // /driver assigned to do the job
    Driver driver;
    // /the directions from the pickup to the dropoff
    Directions directions;

    public enum JobStatus {
	Available, InTransit, Exception, Complete
    }

    // /where we are in the job
    JobStatus status;

    public Job(Location pickup, Location dropoff) {
	this.pickup = pickup;
	this.dropoff = dropoff;
    }

    ///XXX:why do we need available here instead of just in DriverStatus?
    boolean isJobAvailable() {
	return status.equals(JobStatus.Available);
    }

    public Location getPickup() {
	return pickup;
    }

    public void setPickup(Location pickup) {
	this.pickup = pickup;
    }

    public Location getDropoff() {
	return dropoff;
    }

    public void setDropoff(Location dropoff) {
	this.dropoff = dropoff;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Driver getDriver() {
	return driver;
    }

    public void setDriver(Driver driver) {
	this.driver = driver;
    }

    public JobStatus getStatus() {
	return status;
    }

    public void setStatus(JobStatus status) {
	this.status = status;
    }

    public void setDeliveryTime(Date deliveryTime) {
	this.deliveryTime = deliveryTime;
    }

    public Date getDeliveryTime() {
	return deliveryTime;
    }

    public Directions getDirections() {
	return directions;
    }

    public void setDirections(Directions directions) {
	this.directions = directions;
    }

    public Long getOrderId() {
	return orderId;
    }

    public void setOrderId(Long orderId) {
	this.orderId = orderId;
    }

}

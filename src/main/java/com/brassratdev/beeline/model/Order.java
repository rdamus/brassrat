package com.brassratdev.beeline.model;

public class Order {
    ///unique id of the order
    Long id;
    ///location of the order
    Location location;
    
    public Order(Long id) {
	this.id = id;
    }
    
    public Order(Long id, Location location) {
	super();
	this.id = id;
	this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

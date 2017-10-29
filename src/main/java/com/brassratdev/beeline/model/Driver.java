package com.brassratdev.beeline.model;

import java.util.Queue;


/**
 * An interface to a Driver.  A Driver is an entity that picks up and drops off orders for a {@link JobV1}.
 * @author rdamus
 *
 */
public interface Driver{
	public static int MAX_QUEUE_SIZE = 3;
	
    public void addJob(Job job);
    
    String getName();
    Long getId();
    void setLocation(Location loc);
    Location getLocation();
    Queue<Job> getJobQueue();
    DriverStatus getStatus();
}

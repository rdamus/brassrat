package com.brassratdev.app;

/**
 * interface to running a simulated portion of the system.
 * 
 * @author rob
 *
 */
public interface Engine {
    /**
     * pass in the current simulator time
     * @param time the current simulator time
     */
    public void run(double time);
    
    /**
     * the context for engine is a simulator
     * @param sim
     */
    public void setSimulator(Simulator sim);
    /**
     * setup the engine
     */
    public void init();
}

package com.brassratdev.sim;

import com.brassratdev.app.Engine;
import com.brassratdev.app.Simulator;

/**
 * An abstract engine that manages time of the {@link Simulator}
 * @author rob
 *
 */
public abstract class AbstractEngine implements Engine{
    protected double deltaTime;
    protected double lastTime;
    protected String name;
    protected Simulator simulator;
    
    public AbstractEngine(String name) {
	this.name = name;
    }

    public void run(double timeNow) {
	//calc dt
	deltaTime = timeNow - lastTime;
	//run the engine
	runEngine();
	//keep track
	lastTime = timeNow;
    }
    
    abstract protected void runEngine();

    public double getDeltaTime() {
        return deltaTime;
    }

    public String getName() {
        return name;
    }

    public Simulator getSimulator() {
        return simulator;
    }

    public void setSimulator(Simulator simulator) {
        this.simulator = simulator;
        this.lastTime = simulator.getCurrentTime();
    }
}

package com.brassratdev.sim.model.impl;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.sim.model.Axis;
import com.brassratdev.sim.model.Velocity;

public class VelocityImpl implements Velocity {
	RealMatrix body;
	RealMatrix earth;
	double speed;
	boolean manualSpeed = false;

	public VelocityImpl() {
		body = MatrixUtils.createRealMatrix(6, 1);
		earth = MatrixUtils.createRealMatrix(6, 1);
	}
	
	public VelocityImpl(double speed) {
		this();
		setSpeed( speed ); 
	}

	@Override
	public double getVelX() {
		return earth.getEntry(Axis.X.ordinal(), 0);
	}

	@Override
	public double getVelY() {
		return earth.getEntry(Axis.Y.ordinal(), 0);
	}

	@Override
	public double getVelZ() {
		return earth.getEntry(Axis.Z.ordinal(), 0);
	}

	@Override
	public double getBodyVelX() {
		return body.getEntry(Axis.X.ordinal(), 0);
	}

	@Override
	public double getBodyVelY() {
		return body.getEntry(Axis.Y.ordinal(), 0);
	}

	@Override
	public double getBodyVelZ() {
		return body.getEntry(Axis.Z.ordinal(), 0);
	}

	@Override
	public double getRollRate() {
		return body.getEntry(Axis.Roll.ordinal(), 0);
	}

	@Override
	public double getPitchRate() {
		return body.getEntry(Axis.Pitch.ordinal(), 0);
	}

	@Override
	public double getYawRate() {
		return body.getEntry(Axis.Yaw.ordinal(), 0);
	}

	@Override
	public double getSpeed() {
		if( manualSpeed )
			return speed;
		
		speed = Math.sqrt(Math.pow(getBodyVelX(), 2)
				+ Math.pow(getBodyVelY(), 2) + Math.pow(getBodyVelZ(), 2));
		return speed;
	}

	public void setSpeed(double speed){
		this.speed = speed;
		manualSpeed = true;
	}
	
	public RealMatrix getBodyVelVector() {
		return this.body;
	}

	public RealMatrix getEarthVelVector() {
		return this.earth;
	}

	@Override
	public void setBodyVelVector(RealMatrix vel) {
		this.body = vel;
	}

	@Override
	public void setEarthVelVector(RealMatrix vel) {
		this.earth = vel;
	}
}

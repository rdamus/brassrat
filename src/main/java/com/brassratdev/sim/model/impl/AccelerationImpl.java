package com.brassratdev.sim.model.impl;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.sim.model.Acceleration;
import com.brassratdev.sim.model.Axis;

public class AccelerationImpl implements Acceleration {
	RealMatrix body;
	double magnitude;

	public AccelerationImpl() {
		body = MatrixUtils.createRealMatrix(6, 1);
	}

	@Override
	public double getBodyAccelX() {
		return body.getEntry(Axis.X.ordinal(), 0);
	}

	@Override
	public double getBodyAccelY() {
		return body.getEntry(Axis.Y.ordinal(), 0);
	}

	@Override
	public double getBodyAccelZ() {
		return body.getEntry(Axis.Z.ordinal(), 0);
	}

	@Override
	public double getMagnitude() {
		magnitude = Math.sqrt(Math.pow(getBodyAccelX(), 2)
				+ Math.pow(getBodyAccelY(), 2) + Math.pow(getBodyAccelZ(), 2));
		return magnitude;
	}

	@Override
	public RealMatrix getBodyAccelVector() {
		return this.body;
	}

	@Override
	public void setBodyAccelVector(RealMatrix accel) {
		this.body = accel;
	}

}

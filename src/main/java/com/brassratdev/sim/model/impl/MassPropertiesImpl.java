package com.brassratdev.sim.model.impl;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.sim.model.MassProperties;

public class MassPropertiesImpl implements MassProperties {
	RealMatrix inertia;
	double mass;

	public MassPropertiesImpl() {
		inertia = MatrixUtils.createRealMatrix(3, 3);
	}

	@Override
	public double getIxx() {
		return inertia.getEntry(0, 0);
	}

	@Override
	public double getIyy() {
		return inertia.getEntry(1, 1);
	}

	@Override
	public double getIzz() {
		return inertia.getEntry(2, 2);
	}

	@Override
	public double getMass() {
		return mass;
	}

}
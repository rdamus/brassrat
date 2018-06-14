package com.brassratdev.sim.model.impl;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.sim.model.Location;
import com.brassratdev.sim.model.Axis;
import com.brassratdev.sim.model.Position;

public class PositionImpl implements Position {
	RealMatrix pos;
	Location location;

	public PositionImpl() {
		pos = MatrixUtils.createRealMatrix(6, 1);
		location = new Location();
	}

	public RealMatrix getPosVector() {
		return pos;
	}

	@Override
	public double getX() {
		return pos.getEntry(Axis.X.ordinal(), 0);
	}

	@Override
	public double getY() {
		return pos.getEntry(Axis.Y.ordinal(), 0);
	}

	@Override
	public double getZ() {
		return pos.getEntry(Axis.Z.ordinal(), 0);
	}

	@Override
	public double getRoll() {
		return pos.getEntry(Axis.Roll.ordinal(), 0);
	}

	@Override
	public double getPitch() {
		return pos.getEntry(Axis.Pitch.ordinal(), 0);
	}

	@Override
	public double getYaw() {
		return pos.getEntry(Axis.Yaw.ordinal(), 0);
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void setPosVector(RealMatrix pos) {
		this.pos = pos;
	}

	@Override
	public void setLocation(Location l) {
		this.location = l;
	}
}
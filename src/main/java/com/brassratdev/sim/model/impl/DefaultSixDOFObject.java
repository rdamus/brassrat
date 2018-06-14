package com.brassratdev.sim.model.impl;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.sim.model.Acceleration;
import com.brassratdev.sim.model.MassProperties;
import com.brassratdev.sim.model.Position;
import com.brassratdev.sim.model.SixDOFObject;
import com.brassratdev.sim.model.Velocity;
import com.brassratdev.util.SimUtils;

public class DefaultSixDOFObject implements SixDOFObject {
	String name;
	double timeNow;
	double heading;
	double speed;

	RealMatrix jacobian;
	Acceleration accel = new AccelerationImpl();
	Position position = new PositionImpl();
	Velocity velocity = new VelocityImpl();
	MassProperties mass = new MassPropertiesImpl();

	public DefaultSixDOFObject(String name) {
		this.name = name;
		this.timeNow = 0;
		this.heading = 0;
		this.speed = 0;
		this.jacobian = MatrixUtils.createRealMatrix(6, 6);
	}

	@Override
	public Position getPosition() {
		return position;
	}

	@Override
	public Velocity getVelocity() {
		return velocity;
	}

	@Override
	public MassProperties getMassProperties() {
		return mass;
	}

	@Override
	public Acceleration getAcceleration() {
		return accel;
	}

	@Override
	public void iterate(double time, double dt) {
		this.timeNow = time;
		System.out.println("iteration at: " + time + " with dt: " + dt);
		doModel(dt);
	}

	protected void doJacobian() {
		double phi = position.getRoll();
		double cPhi = Math.cos(phi);
		double sPhi = Math.sin(phi);
		double theta = position.getPitch();
		double cTheta = Math.cos(theta);
		double sTheta = Math.sin(theta);
		double psi = position.getYaw();
		double cPsi = Math.cos(psi);
		double sPsi = Math.sin(psi);

		jacobian.setRow(0, new double[] { cPsi * cTheta,
				-sPsi * cPhi + cPsi * sTheta * sPhi,
				sPsi * sPhi + cPsi * sTheta * cPhi, 0d, 0d, 0d });
		jacobian.setRow(1, new double[] { sPsi * cTheta,
				cPsi * cPhi + sPsi * sTheta * sPhi,
				-cPsi * sPhi + sPsi * sTheta * cPhi, 0d, 0d, 0d });
		jacobian.setRow(2, new double[] { -sTheta, cTheta * sPhi,
				cTheta * cPhi, 0d, 0d, 0d });
		jacobian.setRow(3,
				new double[] { 0d, 0d, 0d, 1.0, sPhi * Math.tan(theta),
						cPhi * Math.tan(theta) });
		jacobian.setRow(4, new double[] { 0d, 0d, 0d, 0d, cPhi, -sPhi });
		jacobian.setRow(4, new double[] { 0d, 0d, 0d, 0d, sPhi / cTheta,
				cPhi / cTheta });

	}

	protected void doModel(double dt) {
		doJacobian();

		RealMatrix accB = accel.getBodyAccelVector();
		RealMatrix posE = position.getPosVector();
		RealMatrix velE = velocity.getEarthVelVector();
		RealMatrix velB = velocity.getBodyVelVector();

		// constant accel
		// XXX: replace with F=ma
		// this.accel.setColumn(0, new double[]{1,0,0,0,0,0});
		// integrate accel to get vel
		velB = velB.add(accB.scalarMultiply(dt));
		// transform body vel to earth frame
		velE = jacobian.multiply(velB);
		// integrate earth vel for position
		posE = posE.add(velE.scalarMultiply(dt));

		position.setPosVector(posE);
		velocity.setBodyVelVector(velB);
		velocity.setEarthVelVector(velE);

		// manage angles for the object
		double yaw = SimUtils.angleWrap(position.getYaw());
		this.heading = SimUtils.headingFromYaw(yaw);
		this.speed = velocity.getSpeed();
	}

}
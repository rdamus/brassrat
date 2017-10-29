package com.brassratdev.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.beeline.model.Location;
import com.brassratdev.beeline.model.Order;
import com.brassratdev.util.TimeUtil;

/**
 * the simulator is the master context for simulating the system. it is
 * responsible for managing the time of the simulation and domain objects for
 * the Engines to operate on.
 * 
 * @author rob
 * 
 */
public class Simulator {
    List<Engine> engines = new ArrayList<>();
    Timer simulator;
    Double startTime;
    Double currentTime = TimeUtil.timeNow();

    public void addOrder(Order order) {
	// TODO flesh out
    }

    public void init() {
	engines.add(new OrderEngine());
	engines.add(new DriverEngine());
	// init the engines
	System.out.println("initializaing engines");
	for (Engine e : engines) {
	    e.setSimulator(this);
	    e.init();
	}
    }

    public void start() {
	// now run them
	simulator = new Timer();
	simulator.scheduleAtFixedRate(new SimulatorTask(), 500L, 2000L);
	startTime = TimeUtil.timeNow();
	currentTime = startTime;
    }

    public void stop() {
	simulator.cancel();
    }

    class SimulatorTask extends TimerTask {
	@Override
	public void run() {
	    currentTime = TimeUtil.timeNow();
	    System.out.println("Simulator - time:" + currentTime
		    + " duration: " + (currentTime - startTime) + "sec");
	    for (Engine e : engines) {
		e.run(currentTime);
	    }
	}

    }

    public Double getStartTime() {
	return startTime;
    }

    public Double getCurrentTime() {
	return currentTime;
    }
}

interface SixDOFObject {
    Position getPosition();

    Velocity getVelocity();

    MassProperties getMassProperties();
}

enum Axis {
    X, Y, Z, Roll, Pitch, Yaw;
}

interface Position {
    /** earth relative X */
    double getX();

    /** earth relative Y */
    double getY();

    /** earth relative Z */
    double getZ();

    /** earth relative Roll */
    double getRoll();

    /** earth relative Pitch */
    double getPitch();

    /** earth relative Yaw */
    double getYaw();

    Location getLocation();
    RealMatrix getPosVector();
}

interface Velocity {
    /** earth relative velocity in X */
    double getVelX();

    /** earth relative velocity in Y */
    double getVelY();

    /** earth relative velocity in Z */
    double getVelZ();

    /** body relative velocity in X */
    double getBodyVelX();

    /** body relative velocity in Y */
    double getBodyVelY();

    /** body relative velocity in Z */
    double getBodyVelZ();

    /** body relative roll rate*/
    double getRollRate();
    /** body relative pitch rate*/
    double getPitchRate();
    /** body relative yaw rate*/
    double getYawRate();

    double getSpeed();
    RealMatrix getBodyVelVector();
    RealMatrix getEarthVelVector();
}

interface MassProperties {
    double getIxx();

    double getIyy();

    double getIzz();

    double getMass();

}

class MassPropertiesImpl implements MassProperties {
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

class PositionImpl implements Position {
    RealMatrix pos;
    Location location;

    public PositionImpl() {
	//pos = MatrixUtils.createColumnRealMatrix(new double[] { 0d, 0d, 0d, 0d,
	//	0d, 0d });
	pos = MatrixUtils.createRealMatrix(6, 1);
	location = new Location();
    }
    
    public RealMatrix getPosVector(){
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
}

class VelocityImpl implements Velocity {
    RealMatrix body;
    RealMatrix earth;
    double speed;

    public VelocityImpl() {
	body = MatrixUtils.createRealMatrix(6, 1);
	earth = MatrixUtils.createRealMatrix(6, 1);
	
	//earth = MatrixUtils.createColumnRealMatrix(new double[] { 0d, 0d, 0d });
	//body = MatrixUtils.createColumnRealMatrix(new double[] { 0d, 0d, 0d,
	//	0d, 0d, 0d });
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
	speed = Math.sqrt(Math.pow(getBodyVelX(), 2)
		+ Math.pow(getBodyVelY(), 2) + Math.pow(getBodyVelZ(), 2));
	return speed;
    }

    public RealMatrix getBodyVelVector(){
	return this.body;
    }
    
    public RealMatrix getEarthVelVector(){
	return this.earth;
    }
}

class DefaultSixDOFObject implements SixDOFObject {
    String name;
    double timeNow;
    double heading;
    double speed;

    RealMatrix jacobian;
    RealMatrix accel;
    Position position = new PositionImpl();
    Velocity velocity = new VelocityImpl();
    MassProperties mass = new MassPropertiesImpl();

    DefaultSixDOFObject(String name){
	this.name = name;
	this.timeNow = 0;
	this.heading = 0;
	this.speed = 0;
	this.jacobian = MatrixUtils.createRealMatrix(6, 6);
	this.accel    = MatrixUtils.createRealMatrix(6, 1);
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

    public void iterate(double time, double dt) {
	this.timeNow = time;
	doModel(dt);
    }

    protected void doJacobian(){
	double phi = position.getRoll();    
	double cPhi  = Math.cos( phi );         
	double sPhi  = Math.sin( phi );
	double theta = position.getPitch();  
	double cTheta = Math.cos( theta );   
	double sTheta = Math.sin( theta );
	double psi = position.getYaw();    
	double cPsi= Math.cos( psi );         
	double sPsi = Math.sin( psi );

	jacobian.setRow(0, new double[]{cPsi*cTheta,-sPsi*cPhi+cPsi*sTheta*sPhi, sPsi*sPhi+cPsi*sTheta*cPhi, 0d, 0d, 0d});
	jacobian.setRow(1, new double[]{sPsi*cTheta,cPsi*cPhi+sPsi*sTheta*sPhi,-cPsi*sPhi+sPsi*sTheta*cPhi,0d,0d,0d});
	jacobian.setRow(2, new double[]{-sTheta,cTheta*sPhi,cTheta*cPhi,0d,0d,0d});
	jacobian.setRow(3, new double[]{0d,0d,0d,1.0,sPhi*Math.tan(theta),cPhi*Math.tan(theta)});   
	jacobian.setRow(4, new double[]{0d,0d,0d,0d,cPhi,-sPhi});          
	jacobian.setRow(4, new double[]{0d,0d,0d,0d,sPhi/cTheta,cPhi/cTheta});

    }

    protected void doModel(double dt) {
	doJacobian();
	
	RealMatrix posE = position.getPosVector();
	RealMatrix velE = velocity.getEarthVelVector();
	RealMatrix velB = velocity.getBodyVelVector();
	
	//constant accel
	//XXX: replace with F=ma
	this.accel.setColumn(0, new double[]{1,0,0,0,0,0});
	//integrate accel to get vel
	velB.add( accel.scalarMultiply( dt ) );
	//transform body vel to earth frame
	velE = jacobian.multiply( velB );
	//integrate earth vel for position
	posE.add( velE.scalarMultiply( dt ) );
	
	//manage angles for the object
	double yaw = SimUtils.angleWrap( position.getYaw() );
	this.heading = SimUtils.headingFromYaw( yaw );
	this.speed = velocity.getSpeed();
    }
    
    
}

class SimUtils{
    
    /**
     * 
     * @param yaw in radians
     * @return
     */
    static public double headingFromYaw(double yaw){
	double yawdeg = Math.toDegrees( yaw );
	return yawdeg > 0 ? (360.0 - yawdeg) : -yawdeg;
    }

    static public double angleWrap(double angle){
	if( angle < Math.PI && angle > -Math.PI )
	    return angle;
	
	angle += Math.PI;
	
	angle %= 2*Math.PI;
	
	return angle == 0.0 ? Math.PI : angle - Math.PI;
    }

}























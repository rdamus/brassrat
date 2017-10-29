package com.brassratdev.test.sim;

import static org.junit.Assert.*;

import java.util.List;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.brassratdev.beeline.model.Location;

public class TestSimulatorOps {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testMatrix() {
	RealMatrix row = MatrixUtils.createRealMatrix(6, 1);
	RealMatrix col = MatrixUtils.createRealMatrix(1, 6);
	
	print(row);
	print(col);

    }

    @Test
    public void testMatrixSet() {
	RealMatrix m = MatrixUtils.createRealMatrix(6, 1);
	m.setEntry(0, 0, 42);
	m.setEntry(1, 0, 21);
	m.setEntry(2, 0, 33);
	
	print(m);
    }
    
    @Test
    public void testMatrixAdd() {
	RealMatrix m = MatrixUtils.createRealMatrix(6, 1);
	RealMatrix t = MatrixUtils.createRealMatrix(6, 1);
	m.setEntry(0, 0, 12);
	t.setEntry(0, 0, 42);
	
	
	double val = 5.0;
	m = m.scalarAdd( val );
	System.out.println("m.scalarAdd("+val+"): ");
	print(m);
	
	System.out.println("adding " + t);
	m = m.add(t);
	
	print( m );

    }
    
    @Test
    public void testMatrixMultiply() {
	RealMatrix m = MatrixUtils.createRealMatrix(6, 1);
	RealMatrix t = MatrixUtils.createRealMatrix(6, 1);
	m.setColumn(0, new double[]{1,2,3,1,2,3});
	t.setColumn(0, new double[]{4,5,6,4,5,6});
	
	double val = 5.0;
	m = m.scalarMultiply( val );
	System.out.println("m.scalarMultiply("+val+"): ");
	print(m);
	
	System.out.println("multiplying " + t);
	m = m.multiply(t.transpose());
	
	print( m );

    }

    
    @Test
    public void testSixDOFObject() {
	DefaultSixDOFObject o = new DefaultSixDOFObject("test");
	System.out.println("SixDOFObject matrices");
	print( o );
	
    }
    
    @Test
    public void testSixDOFObjectIterate() {
	DefaultSixDOFObject o = new DefaultSixDOFObject("test");
	System.out.println("SixDOFObject matrices");
	print( o );
	System.out.println("After iteration");
	double dt = 0.1;
	double time = System.currentTimeMillis() / 1000.0;
	Acceleration a = new AccelerationImpl();
	a.getBodyAccelVector().setColumn(0, new double[]{1,0,0,0,0,0});
	for(int i = 0; i <= 10; i++){
	    o.iterate(time + (i*dt), dt);
	    assert( o.getVelocity().getBodyVelX() == ( ( i + 1 ) *dt) );    
	}
	
    }
    
    @Test
    public void testIntegration(){
	SixDOFObject o = new DefaultSixDOFObject("test");
	RealMatrix posE = o.getPosition().getPosVector();
	RealMatrix velE = o.getVelocity().getEarthVelVector();
	RealMatrix velB = o.getVelocity().getBodyVelVector();
	
	RealMatrix jacobian = MatrixUtils.createRealMatrix(6, 6);
	RealMatrix accel    = MatrixUtils.createRealMatrix(6, 1);
	//constant accel
	double dt = 0.1;
	for(int i = 0; i <= 10; i++){
	    //XXX: replace with F=ma
	    accel.setColumn(0, new double[] { 1, 0, 0, 0, 0, 0 });
	    // integrate accel to get vel
	    velB = velB.add(accel.scalarMultiply(dt));
	    //make the jacobian
	    doJacobian(jacobian, o.getPosition());
	    // transform body vel to earth frame
	    velE = jacobian.multiply(velB);
	    RealMatrix copy = o.getVelocity().getEarthVelVector();
	    assert( velE.getEntry(0, 0) == 0 );
	    assert( velB.getEntry(0, 0) == 0 );
	    assert( copy.getEntry(0, 0) == 0 );
	    
	}
	
    }

    void print(SixDOFObject o){
	System.out.print("pos:");
	print( o.getPosition().getPosVector() );
	System.out.print("body vel:");
	print( o.getVelocity().getBodyVelVector() );
	System.out.print("earth vel:");
	print( o.getVelocity().getEarthVelVector() );
    }
    void print(RealMatrix m){
	System.out.println("matrix is " + m.getRowDimension() + " x " + m.getColumnDimension());
	System.out.println("data: " + m);
    }
    
    protected void doJacobian(RealMatrix jacobian, Position position){
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
}

enum Axis {
    X, Y, Z, Roll, Pitch, Yaw;
}

interface SixDOFObject {
    Position getPosition();

    Acceleration getAcceleration();

    Velocity getVelocity();

    MassProperties getMassProperties();
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
    void setPosVector(RealMatrix pos);
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
    void setBodyVelVector(RealMatrix vel);
    void setEarthVelVector(RealMatrix vel);
    
    
}

interface Acceleration{
    /** body relative acceleration in X */
    double getBodyAccelX();

    /** body relative acceleration in Y */
    double getBodyAccelY();

    /** body relative acceleration in Z */
    double getBodyAccelZ();
    double getMagnitude();
    RealMatrix getBodyAccelVector();
    void setBodyAccelVector(RealMatrix accel);
    
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

    @Override
    public void setPosVector(RealMatrix pos) {
	this.pos = pos;
    }
}

class VelocityImpl implements Velocity {
    RealMatrix body;
    RealMatrix earth;
    double speed;

    public VelocityImpl() {
	body = MatrixUtils.createRealMatrix(6, 1);
	earth = MatrixUtils.createRealMatrix(6, 1);
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

    @Override
    public void setBodyVelVector(RealMatrix vel) {
	this.body = vel;
    }

    @Override
    public void setEarthVelVector(RealMatrix vel) {
	this.earth = vel;
    }
}

class AccelerationImpl implements Acceleration {
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
    public RealMatrix getBodyAccelVector(){
	return this.body;
    }
    
    @Override
    public void setBodyAccelVector(RealMatrix accel) {
	this.body = accel;
    }

}

class DefaultSixDOFObject implements SixDOFObject {
    String name;
    double timeNow;
    double heading;
    double speed;

    RealMatrix jacobian;
    Acceleration accel = new AccelerationImpl();
    Position position = new PositionImpl();
    Velocity velocity = new VelocityImpl();
    MassProperties mass = new MassPropertiesImpl();

    DefaultSixDOFObject(String name){
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

    public void iterate(double time, double dt) {
	this.timeNow = time;
	System.out.println("iteration at: " + time + " with dt: " + dt);
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
	
	RealMatrix accB = accel.getBodyAccelVector();
	RealMatrix posE = position.getPosVector();
	RealMatrix velE = velocity.getEarthVelVector();
	RealMatrix velB = velocity.getBodyVelVector();
	
	//constant accel
	//XXX: replace with F=ma
	//this.accel.setColumn(0, new double[]{1,0,0,0,0,0});
	//integrate accel to get vel
	velB = velB.add( accB.scalarMultiply( dt ) );
	//transform body vel to earth frame
	velE = jacobian.multiply( velB );
	//integrate earth vel for position
	posE = posE.add( velE.scalarMultiply( dt ) );
	
	position.setPosVector( posE );
	velocity.setBodyVelVector( velB );
	velocity.setEarthVelVector( velE );
	
	//manage angles for the object
	double yaw = SimUtils.angleWrap( position.getYaw() );
	this.heading = SimUtils.headingFromYaw( yaw );
	this.speed = velocity.getSpeed();
    }
    
    
}

class Route{
    Location start;
    Location finish;
    //heading, speed.  should parallel the vector between waypoints 
    RealMatrix velocityVector;
    //segments from a road?  what is the granularity on distance between each point?
    //XXX: what is the xte or capture radius for staying on a road?
    List<Location> waypoints;
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

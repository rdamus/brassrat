package com.brassratdev.sim.model;

import org.apache.commons.math3.linear.RealMatrix;

public interface Velocity {
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
    void setSpeed(double speed);
    RealMatrix getBodyVelVector();
    RealMatrix getEarthVelVector();
    void setBodyVelVector(RealMatrix vel);
    void setEarthVelVector(RealMatrix vel);
    
    
}
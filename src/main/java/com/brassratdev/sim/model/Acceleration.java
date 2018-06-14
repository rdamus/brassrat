package com.brassratdev.sim.model;

import org.apache.commons.math3.linear.RealMatrix;

public interface Acceleration{
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
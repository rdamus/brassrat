package com.brassratdev.sim.model;

import org.apache.commons.math3.linear.RealMatrix;

import com.brassratdev.sim.model.Location;

public interface Position {
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
    void setLocation(Location l);
}
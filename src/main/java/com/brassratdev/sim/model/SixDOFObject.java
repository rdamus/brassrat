package com.brassratdev.sim.model;


public interface SixDOFObject extends SimObject{
    Position getPosition();

    Acceleration getAcceleration();

    Velocity getVelocity();

    MassProperties getMassProperties();
}

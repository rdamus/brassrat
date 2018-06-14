package com.brassratdev.util.geo;
/**
 * represents an x,y coordinate in a grid
 * @author rob
 *
 */
public class GridFix {
	double y;
	double x;

	GridFix(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	@Override
	public String toString() {
		return "X: " + x + ",Y:" + y;
	}
}

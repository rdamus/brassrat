package com.brassratdev.data.vcc;

public class Coordinate {
	double value;

	public Coordinate() {
		super();
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
	@Override
	public String toString(){ return Double.toString( value ); }
	
}

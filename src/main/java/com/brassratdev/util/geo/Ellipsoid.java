package com.brassratdev.util.geo;

/*
 * Reference ellipsoids derived from Peter H. Dana's website-
 * http://www.utexas.edu/depts/grg/gcraft/notes/datum/elist.html Department of
 * Geography, University of Texas at Austin Internet: pdana@mail.utexas.edu
 * 3/22/95
 * 
 * Source Defense Mapping Agency. 1987b. DMA Technical Report: Supplement to
 * Department of Defense World Geodetic System 1984 Technical Report. Part I and
 * II. Washington, DC: Defense Mapping Agency
 */
public enum Ellipsoid {
	WGS84(6378137, 0.00669438);
	private final double radius;
	private final double ecc;

	Ellipsoid(double radius, double ecc) {
		this.radius = radius;
		this.ecc = ecc;
	}

	public double radius() {
		return radius;
	}

	public double ecc() {
		return ecc;
	}
}

package com.tortoaster.untangle;

public class Edge {
	
	private static final int PRECISION = 1000;
	
	private boolean valid;
	
	private Vertex v1, v2;
	
	public Edge(Vertex v1, Vertex v2) {
		this.v1 = v1;
		this.v2 = v2;
	}
	
	public boolean intersects(Edge e) {
		if(getV1() == e.getV1() || getV1() == e.getV2() || getV2() == e.getV1() || getV2() == e.getV2())
			return false;
		
		int x11 = (int) (getV1().getX() * PRECISION);
		int y11 = (int) (getV1().getY() * PRECISION);
		int x12 = (int) (getV2().getX() * PRECISION);
		int y12 = (int) (getV2().getY() * PRECISION);
		
		int x21 = (int) (e.getV1().getX() * PRECISION);
		int y21 = (int) (e.getV1().getY() * PRECISION);
		int x22 = (int) (e.getV2().getX() * PRECISION);
		int y22 = (int) (e.getV2().getY() * PRECISION);
		
		int bx = x12 - x11;
		int by = y12 - y11;
		int dx = x22 - x21;
		int dy = y22 - y21;
		int bd = bx * dy - by * dx;
		
		if(bd == 0) {
			return false;
		}
		
		int cx = x21 - x11;
		int cy = y21 - y11;
		float t = (float) (cx * dy - cy * dx) / bd;
		
		if(t < 0 || t > 1) {
			return false;
		}
		
		float u = (float) (cx * by - cy * bx) / bd;
		
		return !(u < 0 || u > 1);
	}
	
	public boolean equals(Object o) {
		if(o instanceof Edge) {
			Edge e = (Edge) o;
			return e.v1 == v1 && e.v2 == v2 || e.v1 == v2 && e.v2 == v1;
		}
		
		return true;
	}
	
	public void setValid(boolean valid) {
		if(!valid) {
			v1.setValid(false);
			v2.setValid(false);
		}
		
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public Vertex getV1() {
		return v1;
	}
	
	public Vertex getV2() {
		return v2;
	}
}

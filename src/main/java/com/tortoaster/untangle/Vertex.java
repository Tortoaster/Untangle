package com.tortoaster.untangle;

public class Vertex {
	
	private boolean valid;
	
	private int degree;
	
	private float x, y;
	
	public Vertex(float x, float y) {
		this.x = x;
		this.y = y;
		
		degree = 0;
	}
	
	public void addConnection() {
		degree++;
	}
	
	public void moveTo(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getDegree() {
		return degree;
	}
	
	public boolean equals(Object o) {
		if(o instanceof Vertex) {
			Vertex v = (Vertex) o;
			return v.x == x && v.y == y;
		}
		
		return false;
	}
}

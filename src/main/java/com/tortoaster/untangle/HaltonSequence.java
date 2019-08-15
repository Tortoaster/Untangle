package com.tortoaster.untangle;

public class HaltonSequence {
	
	private int base, index;
	
	public HaltonSequence(int base, int index) {
		this.base = base;
		this.index = index;
	}
	
	public float next() {
		float f = 1;
		float r = 0;
		int i = index;
		
		index++;
		
		while(i > 0) {
			f /= base;
			r += f * (i % base);
			i /= base;
		}
		
		return r;
	}
}

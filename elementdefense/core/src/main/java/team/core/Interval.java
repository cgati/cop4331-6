package team.core;

public class Interval {
	float left, right;
	
	public Interval(float l, float r) {
		if(l > r) {
			float t = r;
			r = l;
			l = t;
		}
		
		left = l;
		right = r;
	}
	
	public float getLeft() {
		return left;
	}
	
	public float getRight() {
		return right;
	}
	
	public float distance() {
		return right - left;
	}
	
	public boolean intersect(float v) {
		return v >= left && v <= right;
	}
}

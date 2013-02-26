package team.core;

public class Interval2D {
	Interval a, b;
	
	public Interval2D(Interval a, Interval b) {
		this.a = a;
		this.b = b;
	}
	
	public boolean intersect(Vector point) {
		return a.intersect(point.getX()) && b.intersect(point.getY());
	}
	
	public float x0() {
		return a.left;
	}
	
	public float x1() {
		return a.right;
	}
	
	public float y0() {
		return b.left;
	}
	
	public float y1() {
		return b.right;
	}

}

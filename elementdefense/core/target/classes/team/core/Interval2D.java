package team.core;

import pythagoras.f.Point;

public class Interval2D {
	Interval a, b;
	
	public Interval2D(Interval a, Interval b) {
		this.a = a;
		this.b = b;
	}
	
	public Interval2D(Point position, float width, float height) {
		this(new Interval(position.x(), position.x() + width), new Interval(position.y(), position.y() + height));
	}
	
	public boolean intersect(Point point) {
		return a.intersect(point.x()) && b.intersect(point.y());
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
	
	public float width() {
		return a.distance();
	}
	
	public float height() {
		return b.distance();
	}

}

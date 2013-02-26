package team.core;

public class Vector {
	float x, y;
	
	public Vector(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public float magnitude() {
		return (float)Math.sqrt(magnitude2());
	}
	
	public float magnitude2() {
		return x * x + y * y;
	}
	
	public float dot(Vector other) {
		return x * other.x + y * other.y;
	}
	
	public float cross(Vector other) {
		// TODO
		return 0;
	}
	
	public float angle(Vector other) {
		// TODO
		return 0;
	}
	
	public Vector scale(float scalar) {
		return new Vector(x * scalar, y * scalar);
	}
	
	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y);
	}
	
	public Vector substract(Vector other) {
		return add(other.scale(-1.0f));
	}
}

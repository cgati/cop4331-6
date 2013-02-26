package team.core;

public class Enhancer {
	public final int RANGE = 0;
	public final int DAMAGE = 1;
	public final int SPEED = 2;
	
	private int type;
	private float scalar;
	private float duration;
	
	public Enhancer(int type, float scalar, float duration) {
		this.type = type;
		this.scalar = scalar;
	}
	
	public int getType() {
		return type;
	}
	
	public float getScalar() {
		return scalar;
	}
	
	public void setScalar(float scalar) {
		this.scalar = scalar;
	}
	
	public float getDuration() {
		return duration;
	}
	
	public void setDuration(float duration) {
		this.duration = duration;
	}
}

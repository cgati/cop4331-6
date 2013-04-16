package team.core;

public class Enhancer {
	public static final int RANGE = 0;
	public static final int DAMAGE = 1;
	public static final int SPEED = 2;
	
	private int type;
	private float scalar;
	private float duration;
	private boolean permanent;
	
	public Enhancer(int type, float scalar, float duration) {
		this.type = type;
		this.scalar = scalar;
		this.duration = duration;
	}
	
	public int getType() {
		return type;
	}
	
	public void permanent() {
		permanent = true;
	}
	
	public boolean isPermanent() {
		return permanent;
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

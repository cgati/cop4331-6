package team.core;

public class Projectile extends ElementObject {
	private float speed;
	private Enemy target;
	private Tower source;
	
	private int type;
	private float damage;
	
	public Projectile(String name, Vector position, Enemy target, Tower source) {
		super(name, position);
		
		// TODO
	}
	
	public void update(float delta) {
		// TODO
	}
	
	public boolean hasHitTarget() {
		// TODO
		return false;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public float getDamage() {
		return damage;
	}
	
	public void setDamage(float damage) {
		this.damage = damage;
	}
	
	public Enemy getTarget() {
		return target;
	}
	
	public Tower getSource() {
		return source;
	}
}

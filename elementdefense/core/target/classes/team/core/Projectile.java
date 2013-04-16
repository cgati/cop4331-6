package team.core;

import pythagoras.f.Point;

public class Projectile extends ElementObject {
	private final float speed = 5.0f;
	
	private Enemy target;
	private Tower source;
	private float damage;
	
	public Projectile() {
		super("projectile", null);
	}
	
	public Projectile(Projectile template, Point position, Enemy target, Tower source, float damage) {
		super("projectile", position);
		
		this.target = target;
		this.source = source;
		this.damage = damage;
		
		this.sprite = template.sprite;
		this.spawnSound = template.spawnSound;
		this.despawnSound = template.despawnSound;
	}
	
	public void update(float delta) {
		Point u = target.getPosition().subtract(getPosition().x(), getPosition().y());
		
		u = u.mult(1.0f/u.distance(0,0));
		
		Point v = u.mult(speed).mult(delta / 1000.0f);
		
		Point r = position.add(v.x(),v.y());
		
		u = target.getPosition();
		
		if(Math.abs(position.subtract(r.x(),r.y()).distance(0,0) - (position.subtract(u.x(),u.y()).distance(0,0) + r.subtract(u.x(),u.y()).distance(0,0))) <= 0.03f) {
			position = u;
		} else {
			position = r;
		}
	}
	
	public boolean hasHitTarget() {		
		float d = target.getPosition().distance(getPosition());
		
		return Math.abs(d - 0.1f) <= 1e-2 || d < 0.1f;
	}
	
	public Enemy getTarget() {
		return target;
	}
	
	public Tower getSource() {
		return source;
	}
}

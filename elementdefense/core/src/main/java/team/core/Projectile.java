package team.core;

import pythagoras.f.Point;

public class Projectile extends ElementObject {
	private final float speed = 8.0f;
	
	private Enemy target;
	private Tower source;
	
	public Projectile(Point position, Enemy target, Tower source) {
		super("projectile", position);
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
		return target.getPosition().distance(getPosition()) <= 0.03;
	}
	
	public Enemy getTarget() {
		return target;
	}
	
	public Tower getSource() {
		return source;
	}
}

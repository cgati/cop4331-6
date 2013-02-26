package team.core;

import java.util.List;

public class Enemy extends Enhanceable {
	private float speed;
	private Vector[] path;
	private boolean hasReachedDestination;
	
	private int defense;
	private float health;
	
	public Enemy(String name, Vector position, Vector[] path) {
		super(name, position);
		// TODO Auto-generated constructor stub
		
		this.path = path;
	}
	
	public Vector[] getPath() {
		return path;
	}
	
	public boolean hasReachedDestination() {
		// TODO
		return false;
	}
	
	public boolean isDead() {
		return health <= 0.0f;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public int getDefense() {
		return defense;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

}

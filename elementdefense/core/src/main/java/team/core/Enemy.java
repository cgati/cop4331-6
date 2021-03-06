package team.core;

import pythagoras.f.Point;

public class Enemy extends Enhanceable {
	protected int index;
	protected float speed;
	protected float distance;
	protected Point[] path;
	protected boolean hasReachedDestination;
	
	protected boolean isCarrier;
	protected Enemy carriedEnemy;
	protected int carriedCount;
	
	protected int defense, bounty;
	protected float health, estHealth, maxHealth;
	
	public Enemy(String name) {
		super(name, null);
	}
	
	public Enemy(Enemy template, Point position, Point[] path) {
		super(template.getName(), position);
		
		this.speed = template.speed;
		this.defense = template.defense;
		this.bounty = template.bounty;
		this.health = this.estHealth = this.maxHealth = template.maxHealth;
		
		this.sprite = template.sprite;
		this.spawnSound = template.spawnSound;
		this.despawnSound = template.despawnSound;
		
		this.position = position;
		
		this.path = path;
	}
	
	public Point[] getPath() {
		return path;
	}
	
	public boolean hasReachedDestination() {
		return index >= path.length;
	}
	
	public boolean isDead() {
		return getHealth() <= 0.0f;
	}
	
	public boolean isLikelyDead() {
		return getEstHealth() <= 0.0f;
	}
	
	public int getBounty() {
		return bounty;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public float getEstHealth() {
		return estHealth;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health > maxHealth ? maxHealth : health;
	}
	
	public float healthPercentage() {
		return health / maxHealth;
	}
	
	public void takeDamage(float damage) {
		if(damage > 0) {
			this.health -= damage;
		}
	}
	
	public float takePreDamage(float damage) {
		if(damage > 0) {
			this.estHealth -= damage;
		}
		
		return damage;
	}
	
	public float getMaxHealth() {
		return maxHealth;
	}
	
	public int getDefense() {
		return defense;
	}
	
	public float getSpeed() {
		return speed * getSpeedMultiplier();
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		if(health <= 0 || index >= path.length) {
			return;
		}
		
		Point d = (World.centerPointGrid(path[index]));
		
		Point u = d.subtract(getPosition().x(), getPosition().y());
		
		if(u.distance(0,0) <= 3.0f) {
			if(index + 1 < path.length) {
				d = (World.centerPointGrid(path[++index]));
				
				u = d.subtract(getPosition().x(), getPosition().y());
			} else {
				position = World.centerPointGrid(path[path.length - 1]);
				
				++index;
				
				return;
			}
		}
		
		u = u.mult(1.0f/u.distance(0,0));
		
		Point v = u.mult(getSpeed()).mult(delta / 1000.0f);
		
		distance += v.distance(0,0);
				
		Point r = position.add(v.x(), v.y());
		
		if(Math.abs(position.subtract(r.x(),r.y()).distance(0,0) - (position.subtract(u.x(),u.y()).distance(0,0) + r.subtract(u.x(),u.y()).distance(0,0))) <= 1.0f) {
			position = path[index++];
		} else {
			position = r;
		}		
	}

}

 package team.core;

import playn.core.Sound;
import pythagoras.f.Point;

public class Tower extends Enhanceable {
	protected int killCount;
	
	protected float power;
	protected float range;
	protected float speed;
	protected float delay;
	
	protected int cost;
	protected int totalCost;
	
	protected boolean first;
	
	protected boolean upgraded;	
	protected int upgradeCost;
	protected String upgradeName;
	
	protected boolean powerUpgraded;	
	protected int powerUpgradeCost;
	
	protected boolean speedUpgraded;
	protected int speedUpgradeCost;
	
	protected boolean rangeUpgraded;
	protected int rangeUpgradeCost;
	
	protected Sound fireSound;
	
	protected String projectileName;
	private Tower template;
	
	public Tower(String name) {
		super(name, null);
	}
	
	public Tower(Tower template, Point position) {
		super(template.name, position);
		
		if(template.template == null) {
			this.template = template;
		} else {
			this.template = template.template;
		}
		
		this.killCount = template.killCount;
		this.power = template.power;
		this.range = template.range;
		this.speed = template.speed;
		this.delay = 0;
		this.cost = template.cost;
		this.totalCost = template.totalCost + template.cost;
		this.upgraded = template.upgraded;
		this.upgradeName = template.upgradeName;
		this.powerUpgraded = template.powerUpgraded;
		this.speedUpgraded = template.powerUpgraded;
		this.rangeUpgraded = template.rangeUpgraded;
		this.upgradeCost = template.upgradeCost;
		this.powerUpgradeCost = template.powerUpgradeCost;
		this.speedUpgradeCost = template.speedUpgradeCost;
		this.rangeUpgradeCost = template.rangeUpgradeCost;
		this.projectileName = template.projectileName;
		
		this.sprite = template.sprite;
		this.spawnSound = template.spawnSound;
		this.despawnSound = template.despawnSound;
		this.fireSound = template.fireSound;
	}
	
	public int getTotalCost() {
		return totalCost;
	}
	
	public int getKillCount() {
		return killCount;
	}
	
	public void setKillCount(int killCount) {
		this.killCount = killCount;
	}
	
	public float getPower() {
		return power * getPowerMultiplier();
	}
	
	public float getRange() {
		return range * getRangeMultiplier();
	}
	
	public float getSpeed() {
		return speed * getSpeedMultiplier();
	}
	
	public float getDelay() {
		return delay;
	}
	
	public void setDelay(float delay) {
		this.delay = delay;
	}
	
	public boolean isUpgraded() {
		return upgraded;
	}
	
	public int getCost() {
		return cost;
	}
	
	public int getUpgradeCost() {
		return upgradeCost;
	}
	
	public String getUpgradeName() {
		return upgradeName;
	}
	
	public void upgrade() {
		name = upgradeName;
		
		upgraded = true;
		
		totalCost += upgradeCost;
		
		addEnhancer(Enhancer.DAMAGE, 1.5f, 0.0f, true);
		addEnhancer(Enhancer.SPEED, 0.8f, 0.0f, true);
	}
	
	public boolean isPowerUpgraded() {
		return powerUpgraded;
	}
	
	public int getPowerUpgradeCost() {
		return powerUpgradeCost;
	}
	
	public void upgradePower() {
		powerUpgraded = true;
		
		totalCost += powerUpgradeCost;
		
		addEnhancer(Enhancer.DAMAGE, 1.5f, 0.0f, true);
	}
	
	public boolean isSpeedUpgraded() {
		return speedUpgraded;
	}
	
	public int getSpeedUpgradeCost() {
		return speedUpgradeCost;
	}
	
	public void upgradeSpeed() {
		speedUpgraded = true;
		
		totalCost += speedUpgradeCost;
		
		addEnhancer(Enhancer.SPEED, 0.8f, 0.0f, true);
	}
	
	public boolean isRangeUpgrade() {
		return rangeUpgraded;
	}
	
	public int getRangeUpgradeCost() {
		return rangeUpgradeCost;
	}
	
	public void upgradeRange() {
		rangeUpgraded = true;
		
		addEnhancer(Enhancer.RANGE, 2.0f, 0.0f, true);
	}
	
	public void move(Point position) {
		this.position = position;
		
		delay = getSpeed() * 3; 
	}
	
	public int getMoveCost() {
		return (int)(totalCost * 0.2f);
	}
	
	public int getSellCost() {
		return (int)(totalCost * 0.6f);
	}
	
	public String getProjectileName() {
		return projectileName;
	}

	@Override
	public void update(float delta) {
		delay -= delta;
		
		if(delay < 0.0f) {
			delay = 0.0f;
		}
	}
}

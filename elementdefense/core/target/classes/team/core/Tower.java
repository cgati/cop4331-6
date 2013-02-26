package team.core;

import java.util.List;

public class Tower extends Enhanceable {
	private int killCount;
	
	private float range;
	private float speed;
	private float delay;
	
	private int totalCost;
	
	private boolean upgraded;	
	private int upgradeCost;
	private String upgradeName;
	
	private boolean powerUpgraded;	
	private int powerUpgradeCost;
	
	private boolean speedUpgraded;
	private int speedUpgradeCost;
	
	private String projectileName;
	
	public Tower(String name, Vector position) {
		super(name, position);
		// TODO Auto-generated constructor stub
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
	
	public float getRange() {
		return range;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public float getDelay() {
		return delay;
	}
	
	public boolean isUpgrade() {
		return upgraded;
	}
	
	public int getUpgradeCost() {
		return upgradeCost;
	}
	
	public String getUpgradeName() {
		return upgradeName;
	}
	
	public void upgrade() {
		// TODO
	}
	
	public boolean isPowerUpgraded() {
		return powerUpgraded;
	}
	
	public int getPowerUpgradeCost() {
		return powerUpgradeCost;
	}
	
	public void upgradePower() {
		// TODO
	}
	
	public boolean isSpeedUpgraded() {
		return speedUpgraded;
	}
	
	public int getSpeedUpgradeCost() {
		return speedUpgradeCost;
	}
	
	public void upgradeSpeed() {
		// TODO
	}
	
	public String getProjectileName() {
		return projectileName;
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}
}

package team.core;

import java.util.List;

import playn.core.SurfaceLayer;

public class World {
	private int money;
	private int level;
	private int state;
	private float delay;
	private String towerName;
	private Tower selectedTower;
	private Level[] levels;
	private SurfaceLayer surface;
	
	private List<Projectile> projectiles;
	private List<Enemy> enemies;
	private List<Tower> towers;
	
	private boolean finished;
	
	public World(SurfaceLayer surface, Level[] levels) {
		this.levels = levels;
		
		// TODO
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void beginTowerPlacement(String name) {
		
	}
	
	public void attemptTowerPlacement(Point point) {
		
	}
	
	public void selectTower(Point point) {
		
	}
	
	public void upgradeTower() {
		
	}
	
	public void upgradeTowerPower() {
		
	}
	
	public void upgradeTowerSpeed() {
		
	}
	
	public void sellTower() {
		
	}
	
	public void speedUpGame() {
		
	}
	
	public void slowDownGame() {
		
	}
	
	public void showPauseMenu() {
		
	}
	
	public void showSettingsMenu() {
		
	}
	
	public void quit() {
		
	}
	
	public void paint(float alpha) {
		// TODO
	}
	
	public void update(float delta) {
		// TODO
	}
}

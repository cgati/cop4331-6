package team.core;

import playn.core.Image;
import pythagoras.f.Point;

public class Level {
	protected String name;
	protected String story;
	protected Image background;

	protected Point spawn;
	protected Point[] path;
	
	protected int lives;
	protected int waveCount;
	protected int initialMoney;
	protected Object[][] grid;
	protected float[] waveDelay;
	protected Enemy[][] enemies;
	protected int[][] enemyCount;
	
	private boolean endless;
	
	public Level(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public Image getImage() {
		return background;
	}
	
	public Object[][] getGrid() {
		return grid;
	}
	
	public int getWaveCount() {
		return waveCount;
	}
	
	public int getInitialMoney() {
		return initialMoney;
	}
		
	public float getWaveDelay(int wave) {
		return waveDelay[wave];
	}
	
	public int getEnemyCount(int wave, int enemy) {
		return enemyCount[wave][enemy];
	}
	
	public int getNumberOfEnemies(int wave, int enemy) {
		return enemyCount[wave][enemy];
	}
	
	public Enemy[] getWaveEnemies(int wave) {
		return enemies[wave];
	}
	
	public boolean isEndless() {
		return endless;
	}
}

package team.core;

import playn.core.Image;
import pythagoras.f.Point;

public class Level {
	protected String name;
	protected String story;
	protected Image background;

	protected Point[] spawn;
	protected Point[][] path;
	
	protected int lives;
	protected int waveCount;
	protected int initialMoney;
	protected Object[][] grid;
	protected float[] waveDelay;
	protected Enemy[][][] enemies;
	protected int[][][] enemyCount;
	
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
	
	public int getEnemyCount(int spawn, int wave) {
		return enemyCount[spawn][wave].length;
	}
	
	public int getNumberOfEnemies(int spawn, int wave, int enemy) {
		return enemyCount[spawn][wave][enemy];
	}
	
	public Enemy[] getWaveEnemies(int spawn, int wave) {
		return enemies[spawn][wave];
	}
	
	public boolean isEndless() {
		return endless;
	}
}

package team.core;

import playn.core.Image;

public class Level {
	private String name;
	private Image background;
	
	private int levelCount;
	private int[] waveCount;
	private int[][] grid;
	private Enemy[][][] enemies;
	private float[][] waveDelay;
	
	private boolean endless;
	
	public Level(String name) {
		this.name = name;
		
		// TODO
	}
	
	public String getName() {
		return name;
	}
	
	public Image getImage() {
		return background;
	}
	
	public int[][] getGrid() {
		return grid;
	}
	
	public int getLevelCount() {
		return levelCount;
	}
	
	public int getWaveCount(int level) {
		return waveCount[level];
	}
	
	public float getWaveDelay(int level, int wave) {
		return waveDelay[level][wave];
	}
	
	public int getEnemyCount(int level, int wave) {
		return getWaveEnemies(level, wave).length;
	}
	
	public Enemy[] getWaveEnemies(int level, int wave) {
		return enemies[level][wave];
	}
	
	public boolean isEndless() {
		return endless;
	}
}

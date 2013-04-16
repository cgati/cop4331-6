package team.core;

import static playn.core.PlayN.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import playn.core.Image;
import playn.core.PlayN;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import playn.core.Surface;
import playn.core.SurfaceLayer;
import pythagoras.f.Point;

public class World {
	private int state;
	private int money;
	private int level;
	private int wave;
	private int enemy;
	private int enemyIndex;
	private int lives;
	private float speed;
	private float delay;
	private Tower selectedTower;
	private Level[] levels;
	private SurfaceLayer layer;
	private Surface surface;
	private WorldGui gui;
	
	private Point cursor;
	private Interval2D boundingBox;
	
	private List<Projectile> projectiles;
	private List<Enemy> enemies;
	private List<Tower> towers;
	private List<SpawnPoint> spawns;
	
	private List<Enemy>[][] collision;
	
	private Enemy[] availableEnemies;
	private final int MARINE = 0, ARMORED_SOLDIER = 1, SWIFT_SOLDIER = 2, SWIFT_CARRIER = 3, ARMORED_CARRIER = 4;
	
	private Object[][] grid;
	
	public static final float SPAWN_DELAY = 250;
	public static final float EPS = 1e-3f;
	public static final Object BUILDABLE = new Object(), UNBUILDABLE = new Object();
	
	protected boolean endless;
	
	private Map<String, Tower> templateMap;
	private Map<String, Projectile> projectileMap;
	
	private Tower fireTemplate, heartTemplate, earthTemplate, waterTemplate, windTemplate;
	
	private Tower placementTower;
	private boolean isMovingTower;
	private boolean isPlacingTower;
	
	private Image map, placementGrid, levelComplete;
	
	private boolean finished;
	
	private class SpawnPoint {
		protected int index;
		protected float delay;
		protected Point spawn;		
		protected List<Enemy> enemies;
		
		public SpawnPoint(Point spawn, Point[] path, Enemy[][] enemies, int[][] enemyCount) {
			this.spawn = spawn;
			this.enemies = new ArrayList<Enemy>();
			
			for(int i = 0; i < enemies.length; ++i) {
				for(int j = 0; j < enemies[i].length; ++j) {
					for(int k = 0; k < enemyCount[i][j]; ++k) {
						this.enemies.add(new Enemy(enemies[i][j], null, path));
					}
				}
			}
		}
	}
	
	public World(SurfaceLayer layer, Level[] levels) {
		this.state = 0;
		this.levels = levels;
		this.layer = layer;
		this.surface = layer.surface();
		this.grid = new Object[20][29];
		this.towers = new ArrayList<Tower>();
		this.enemies = new ArrayList<Enemy>();
		this.projectiles = new ArrayList<Projectile>();
		this.spawns = new ArrayList<SpawnPoint>();
		this.money = 100;
		this.speed = 1;
		this.collision = new List[20][29];
		
		for(List<Enemy>[] A : collision) {
			for(int i = 0; i < A.length; ++i) {
				A[i] = new ArrayList<Enemy>();
			}
		}
		
		this.layer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				
				if(state == 5) {
					state = 1;
					
					return;
				}
				
				if(state < 2 || state == 6) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				if(isPlacingTower && !boundingBox.intersect(p)) {
					cancelTowerPlacement();
				} else if(isPlacingTower) {
					attemptTowerPlacement(p);
				}
				
				if(isMovingTower && !boundingBox.intersect(p)) {
					cancelTowerMove();
				} else if(isMovingTower) {
					attemptTowerMove(p);
				}
				
				if(!isPlacingTower && !isMovingTower) {
					selectTower(p);
				}
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerCancel(Event event) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		boundingBox = new Interval2D(new Point(0,0), 1044 -1 , 720 - 1);
		
		placementGrid = assets().getImage("images/maps/Grid.png");
		levelComplete = assets().getImage("images/Level Complete.png");
		
		gui = new WorldGui(layer);
		
		initialize();
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void beginTowerPlacement(String name) {
		if(isMovingTower) {
			return;
		}
		
		if(isPlacingTower && placementTower != null && name.equalsIgnoreCase(placementTower.getName())) {
			cancelTowerPlacement();
			
			return;
		}
		
		clearSelectedTower();
		
		isPlacingTower = true;
		
		placementTower = templateMap.get(name);
	}
	
	public void cancelTowerPlacement() {
		isPlacingTower = false;
		
		placementTower = null;
	}
	
	public void attemptTowerPlacement(Point point) {
		if(isMovingTower || !isPlacingTower || !boundingBox.intersect(point)) {
			return;
		}
		
		int x = (int)(point.x() / 36), y = (int)(point.y() / 36);
		
		if(grid[y][x] == BUILDABLE && money >= placementTower.getCost()) {
			grid[y][x] = new Tower(placementTower, new Point(x * 36, y * 36));
			
			towers.add((Tower)grid[y][x]);
			
			money -= placementTower.getCost();
		}
	}
	
	public void beginMoveTower(Tower tower) {
		if(isPlacingTower || isMovingTower || getSelectedTower() == null || money < getSelectedTower().getMoveCost()) {
			return;
		}
		
		isMovingTower = true;
	}
	
	public void attemptTowerMove(Point point) {
		if(!isMovingTower || isPlacingTower || !boundingBox.intersect(point)) {
			return;
		}
		
		Tower T = getSelectedTower();
		
		int x0 = (int)(T.position.x() / 36), y0 = (int)(T.position.y() / 36);
		int x = (int)(point.x() / 36), y = (int)(point.y() / 36);
		
		if(grid[y][x] == BUILDABLE && money >= T.getMoveCost()) {
			grid[y][x] = T;
			grid[y0][x0] = BUILDABLE;
			
			T.move(new Point(x * 36, y * 36));
			
			money -= T.getMoveCost();
			
			cancelTowerMove();
		}		
	}
	
	public void cancelTowerMove() {
		isMovingTower = false;
	}
	
	public void selectTower(Point point) {
		if(!boundingBox.intersect(point)) {
			return;
		}
		
		int x = (int)(point.x() / 36), y = (int)(point.y() / 36);
		
		if(grid[y][x] instanceof Tower) {
			selectedTower = (Tower)grid[y][x];
			
			gui.showSelectedMenu(selectedTower.getName());
		} else {
			clearSelectedTower();
		}
	}
	
	public void clearSelectedTower() {
		selectedTower = null;
		
		gui.hideSelectedMenu();
	}
	
	public Tower getSelectedTower() {
		return selectedTower;
	}
	
	public void upgradeTower() {
		if(getSelectedTower() == null) {
			return;
		}
		
		Tower T = getSelectedTower();
		
		if(!T.isUpgraded() && money >= T.getUpgradeCost()) {
			T.upgrade();
			
			money -= T.getUpgradeCost();
		}
	}
	
	public void upgradeTowerPower() {
		if(getSelectedTower() == null) {
			return;
		}
		
		Tower T = getSelectedTower();
		
		if(!T.isPowerUpgraded() && money >= T.getPowerUpgradeCost()) {
			T.upgradePower();
			
			money -= T.getPowerUpgradeCost();
		}
	}
	
	public void upgradeTowerSpeed() {
		if(getSelectedTower() == null) {
			return;
		}
		
		Tower T = getSelectedTower();
		
		if(!T.isSpeedUpgraded() && money >= T.getSpeedUpgradeCost()) {
			T.upgradePower();
			
			money -= T.getSpeedUpgradeCost();
		}		
	}
	
	public void upgradeTowerRange() {
		if(getSelectedTower() == null) {
			return;
		}
		
		Tower T = getSelectedTower();
		
		if(!T.isRangeUpgrade() && money >= T.getRangeUpgradeCost()) {
			T.upgradeRange();
			
			money -= T.getRangeUpgradeCost();
		}		
	}
	
	public void moveTower() {
		if(getSelectedTower() == null) {
			return;
		}
		
		beginMoveTower(getSelectedTower());
	}
	
	public void sellTower() {
		if(getSelectedTower() == null) {
			return;
		}
		
		Tower T = getSelectedTower();
		
		towers.remove(T);
		
		Point point = T.getPosition();
		
		int x = (int)(point.x() / 36), y = (int)(point.y() / 36);
		
		grid[y][x] = BUILDABLE;
		
		money += T.getSellCost();
		
		clearSelectedTower();
	}
	
	public void speedUpGame() {
		speed *= 2;
		
		if(speed > 2) {
			speed = 2;
		}
	}
	
	public void slowDownGame() {
		speed /= 2;
		
		if(speed < 0.5f) {
			speed = 0.5f;
		}
	}
	
	public String getSpeedText() {
		return "" + speed;
	}
	
	public void paint(float alpha) {
		if(state == 0) {
			return;
		}
		
		if(state == 1) {
			return;
		}
		
		if(state == 5) {
			surface.drawImage(levelComplete, 0, 0);
			
			return;
		}
		
		if(state == 7) {
			return;
		}
		
		gui.paint(alpha);
		
		surface.drawImage(map, 0, 0);
		
		if(isPlacingTower) {
			surface.drawImage(placementGrid, 0, 0);
			surface.drawImage(placementTower.getSprite(), cursor.x() - placementTower.getSprite().width() / 2, cursor.y() - placementTower.getSprite().height() / 2);
		}
		
		if(isMovingTower) {
			surface.drawImage(placementGrid, 0, 0);
			surface.drawImage(getSelectedTower().getSprite(), cursor.x() - getSelectedTower().getSprite().width() / 2, cursor.y() - getSelectedTower().getSprite().height() / 2);
		}
		
		for(Tower T : towers) {
			surface.drawImage(T.getSprite(), T.getPosition().x(), T.getPosition().y());
		}
		
		for(Enemy E : enemies) {			
			float s = E.healthPercentage();
			
			if(E.isDead()) continue;
			
			surface.drawImage(E.getSprite(), (E.getPosition()).x() - (s * E.getSprite().width()) / 2, (E.getPosition()).y() - (s * E.getSprite().height()) / 2, E.getSprite().width() * s, E.getSprite().height() * s);
		}
		
		for(Projectile P : projectiles) {
			surface.drawImage(P.getSprite(), (P.getPosition()).x() - P.getSprite().width() / 2, (P.getPosition()).y() - P.getSprite().height() / 2);
		}
	}
	
	public void reset() {
		state = 0;
		speed = 1;
	}
	
	public void update(float delta) {
		if(state == 7) {
			ElementDefense.getInstance().getGameOverGui().show();
			
			clearSelectedTower();
			cancelTowerMove();
			
			return;
		}
		
		delta *= speed;
		
		if(state == 0) {
			state = 1;
			
			delay = 0;
			
			level = -1;
			
			return;
		}
		
		if(state == 1) {
			if(level + 1 >= levels.length) {
				state = 7;
				
				return;
			}
			
			state = 2;
			
			++level;
			
			wave = enemy = enemyIndex = 0;
			
			map = levels[level].background;
			lives = levels[level].lives;
			money = levels[level].initialMoney;
			grid = levels[level].grid;
			delay = levels[level].getWaveDelay(wave);
			
			enemies.clear();
			projectiles.clear();
			towers.clear();
			
			spawns.clear();
			
			for(int i = 0; i < levels[level].spawn.length; ++i) {
				spawns.add(new SpawnPoint(levels[level].spawn[i], levels[level].path[i], levels[level].enemies[i], levels[level].enemyCount[i]));
			}
			
			return;
		}
		
		if(state == 2) {
			delay -= delta;
			
			if(delay <= 0) {
				gui.setSpawn(null);
				
				state = 3;
			} else {			
				gui.setSpawn(Math.round(delay / 1000.0f));
			}
		}
		
		if(state == 3) {			
			if(spawns.get(wave).index >= spawns.get(wave).enemies.size()) {
				state = 4;
			}
			
			if(spawns.get(wave).delay <= EPS) {
				int j = spawns.get(wave).index;
				
				Enemy e = spawns.get(wave).enemies.get(j);
				
				e.position = centerPointGrid(spawns.get(wave).spawn);
				
				enemies.add(e);
				
				spawns.get(wave).index = j + 1;
				
				spawns.get(wave).delay = SPAWN_DELAY;
			} else {
				spawns.get(wave).delay -= delta;
			}
		}
		
		if(enemies.size() == 0 && state == 4) {
			System.out.println("LEVEL COMPLETE");
			
			if(wave + 1 >= spawns.size()) {
				state = 5;
			}
		}
		
		if(state != 6) {
			for(List<Enemy>[] L : collision) {
				for(List<Enemy> list : L) {
					list.clear();
				}
			}
			
			Iterator<Enemy> enemyIterator = enemies.iterator();
			
			while(enemyIterator.hasNext()) {
				Enemy E = enemyIterator.next();
				
				if(E.isDead()) {
					enemyIterator.remove();
					
					money += E.getBounty();
					
					continue;
				}
				
				if(!E.hasReachedDestination()) {				
					E.update(delta);
					
					Point P = pointGrid(E.getPosition());
					
					collision[(int)P.y()][(int)P.x()].add(E);
				} else {
					--lives;
					
					E.setHealth(0);
					
					enemyIterator.remove();
				}
			}
			
			Iterator<Projectile> projectileIterator = projectiles.iterator();
			
			while(projectileIterator.hasNext()) {
				Projectile P = projectileIterator.next();
				
				if(P.getTarget().hasReachedDestination() && !P.hasHitTarget()) {
					projectileIterator.remove();
					
					continue;
				}
				
				if(!P.hasHitTarget()) {
					P.update(delta);
				} else {
					if(P.getSource().getProjectileName().equals("fireball")) {
						List<Enemy> enemies = getEnemiesInRange(pointGrid(P.getPosition()), 2);
						
						P.getTarget().takeDamage(P.getDamage() - P.getTarget().getDefense());
						
						enemyIterator = enemies.iterator();
						
						while(enemyIterator.hasNext()) {
							Enemy E = enemyIterator.next();
							
							if(E != P.getTarget()) {
								E.takeDamage(P.getDamage() * 0.2f - E.getDefense());
								E.takePreDamage(P.getDamage() * 0.2f - E.getDefense());
							}
							
							if(E.isDead()) {
								enemyIterator.remove();
								
								money += E.getBounty();
							}
						}
					} else if(P.getSource().getProjectileName().equals("rock")) {
						List<Enemy> enemies = getEnemiesInRange(pointGrid(P.getPosition()), 1);
						
						P.getTarget().takeDamage(P.getDamage() - P.getTarget().getDefense());
						
						enemyIterator = enemies.iterator();
						
						while(enemyIterator.hasNext()) {
							Enemy E = enemyIterator.next();
							
							if(E != P.getTarget()) {
								E.takeDamage(P.getDamage() * 0.65f - E.getDefense());
								E.takePreDamage(P.getDamage() * 0.65f - E.getDefense());
							}
							
							if(E.isDead()) {
								enemyIterator.remove();
								
								money += E.getBounty();
							}
						}
					} else if(P.getSource().getProjectileName().equals("bubble")) {
						List<Enemy> enemies = getEnemiesInRange(pointGrid(P.getPosition()), 3);
						
						P.getTarget().takeDamage(P.getDamage() - P.getTarget().getDefense());
						
						enemyIterator = enemies.iterator();
						
						while(enemyIterator.hasNext()) {
							Enemy E = enemyIterator.next();
							
							if(E != P.getTarget()) {
								E.takeDamage(P.getDamage() * 0.1f - E.getDefense());
								E.takePreDamage(P.getDamage() * 0.1f - E.getDefense());
							}
							
							if(E.isDead()) {
								enemyIterator.remove();
								
								money += E.getBounty();
							}
						}
					} else if(P.getSource().getProjectileName().equals("air")) {
						float s = 0.7f;
						
						if(P.getTarget().hasEnhancer(Enhancer.SPEED, s)) {
							P.getTarget().removeEnhancer(Enhancer.SPEED, s);
						}
						
						P.getTarget().addEnhancer(Enhancer.SPEED, s, 3000.0f);
					}
					
					projectileIterator.remove();
				}
			}
			
			for(Tower T : towers) {
				T.update(delta);
				
				if(T.getDelay() <= EPS) {
					List<Enemy> list = getEnemiesInRange(pointGrid(T.getPosition()), Math.round(T.getRange()));
					
					if(list.size() > 0 && T.getProjectileName() != null) {
						float d = 0.0f;
						
						Collections.sort(list, new Comparator<Enemy>() {
							@Override
							public int compare(Enemy a, Enemy b) {
								if(a.isLikelyDead() && b.isLikelyDead()) {
									return 0;
								}
								
								if(a.isLikelyDead()) {
									return 1;
								}
								
								if(b.isLikelyDead()) {
									return -1;
								}
								
								return Float.compare(b.getDistance(), a.getDistance());
							}
						});
						
						Enemy E = list.get(0);
						
						if(E.isLikelyDead()) {
							continue;
						}
						
						if(T.getProjectileName().equals("fireball")) {
							d = E.takePreDamage(T.getPower() - E.getDefense());
						} else if(T.getProjectileName().equals("rock")) {
							d = E.takePreDamage(T.getPower() - E.getDefense());
						} else if(T.getProjectileName().equals("bubble")) {							
							d = E.takePreDamage(T.getPower() - E.getDefense());
						} else if(T.getProjectileName().equals("air")) {							
							for(int i = 0; i < list.size(); ++i) {
								if(!list.get(i).hasEnhancer(Enhancer.SPEED, 0.7f)) {									
									E = list.get(i);
								}
							}
							
							E = list.get((int)(PlayN.random() * list.size()));
						}
						
						Projectile P = new Projectile(projectileMap.get(T.getProjectileName()), T.getPosition(), list.get(0), T, d);
						
						projectiles.add(P);
						
						T.setDelay(T.getSpeed());
					}
				}
			}
		}
		
		gui.show();
		
		gui.setMoney(money);
		gui.setLives(lives);
		gui.setSpeed(getSpeedText());
		
		if(getSelectedTower() != null) {
			gui.setName(getSelectedTower().getName());
		}
	}
	
	public void mouseMove(Point p) {
		gui.mouseMove(p);
		
		cursor = p;
	}
	
	private void initialize() {
		setupProjectiles();
		setupTowers();
		setupEnemies();
		setupLevels();
	}
	
	private void setupProjectiles() {
		Image fireball = assets().getImage("images/bullets/fireBullet.png");
		Image rock = assets().getImage("images/bullets/earthBullet.png");
		Image bubble = assets().getImage("images/bullets/waterBullet.png");
		Image air = assets().getImage("images/bullets/windBullet.png");
		
		Projectile fire = new Projectile();
		
		fire.sprite = fireball;
		fire.spawnSound = null;
		fire.despawnSound = null;
		
		Projectile earth = new Projectile();
		
		earth.sprite = rock;
		earth.spawnSound = null;
		earth.despawnSound = null;
		
		Projectile water = new Projectile();
		
		water.sprite = bubble;
		water.spawnSound = null;
		water.despawnSound = null;
		
		Projectile wind = new Projectile();
		
		wind.sprite = air;
		wind.spawnSound = null;
		wind.despawnSound = null;
		
		projectileMap = new TreeMap<String, Projectile>();
		
		projectileMap.put("fireball", fire);
		projectileMap.put("rock", earth);
		projectileMap.put("bubble", water);
		projectileMap.put("air", wind);
	}
	
	private void setupTowers() {
		Image fireImage = assets().getImage("images/towers/fireTower.png");
		Image heartImage = assets().getImage("images/towers/heartTower.png");
		Image earthImage = assets().getImage("images/towers/earthTower.png");
		Image windImage = assets().getImage("images/towers/windTower.png");
		Image waterImage = assets().getImage("images/towers/waterTower.png");
		
		fireTemplate = new Tower("Fire");
		
		fireTemplate.killCount = 0;
		fireTemplate.power = 4;
		fireTemplate.range = 2;
		fireTemplate.speed = 3000;
		fireTemplate.first = true;
		fireTemplate.cost = 5;
		fireTemplate.totalCost = 0;
		fireTemplate.upgraded = false;
		fireTemplate.upgradeName = "Inferno";
		fireTemplate.powerUpgraded = false;
		fireTemplate.speedUpgraded = false;
		fireTemplate.upgradeCost = 10;
		fireTemplate.powerUpgradeCost = 0;
		fireTemplate.speedUpgradeCost = 0;
		fireTemplate.projectileName = "fireball";
		
		fireTemplate.sprite = fireImage;
		fireTemplate.spawnSound = null;
		fireTemplate.despawnSound = null;
		fireTemplate.fireSound = null;
		
		earthTemplate = new Tower("Earth");
		
		earthTemplate.killCount = 0;
		earthTemplate.power = 4;
		earthTemplate.range = 1;
		earthTemplate.speed = 3000;
		earthTemplate.first = true;
		earthTemplate.cost = 5;
		earthTemplate.totalCost = 0;
		earthTemplate.upgraded = false;
		earthTemplate.upgradeName = "Earthquake";
		earthTemplate.powerUpgraded = false;
		earthTemplate.speedUpgraded = false;
		earthTemplate.upgradeCost = 10;
		earthTemplate.powerUpgradeCost = 0;
		earthTemplate.speedUpgradeCost = 0;
		earthTemplate.projectileName = "rock";
		
		earthTemplate.sprite = earthImage;
		earthTemplate.spawnSound = null;
		earthTemplate.despawnSound = null;
		earthTemplate.fireSound = null;
		
		waterTemplate = new Tower("Water");
		
		waterTemplate.killCount = 0;
		waterTemplate.power = 4;
		waterTemplate.range = 3;
		waterTemplate.speed = 2000;
		waterTemplate.first = true;
		waterTemplate.cost = 5;
		waterTemplate.totalCost = 0;
		waterTemplate.upgraded = false;
		waterTemplate.upgradeName = "Tsunami";
		waterTemplate.powerUpgraded = false;
		waterTemplate.speedUpgraded = false;
		waterTemplate.upgradeCost = 10;
		waterTemplate.powerUpgradeCost = 0;
		waterTemplate.speedUpgradeCost = 0;
		waterTemplate.projectileName = "bubble";
		
		waterTemplate.sprite = waterImage;
		waterTemplate.spawnSound = null;
		waterTemplate.despawnSound = null;
		waterTemplate.fireSound = null;
		
		windTemplate = new Tower("Wind");
		
		windTemplate.killCount = 0;
		windTemplate.power = 0;
		windTemplate.range = 2;
		windTemplate.speed = 1000;
		windTemplate.first = true;
		windTemplate.cost = 5;
		windTemplate.totalCost = 0;
		windTemplate.upgraded = false;
		windTemplate.upgradeName = "Cyclone";
		windTemplate.powerUpgraded = false;
		windTemplate.speedUpgraded = false;
		windTemplate.upgradeCost = 10;
		windTemplate.powerUpgradeCost = 0;
		windTemplate.speedUpgradeCost = 0;
		windTemplate.projectileName = "air";
		
		windTemplate.sprite = windImage;
		windTemplate.spawnSound = null;
		windTemplate.despawnSound = null;
		windTemplate.fireSound = null;
		
		heartTemplate = new Tower("Heart");
		
		heartTemplate.killCount = 0;
		heartTemplate.power = 0;
		heartTemplate.range = 0;
		heartTemplate.speed = 1000;
		heartTemplate.first = true;
		heartTemplate.cost = 25;
		heartTemplate.totalCost = 0;
		heartTemplate.upgraded = false;
		heartTemplate.upgradeName = null;
		heartTemplate.powerUpgraded = false;
		heartTemplate.speedUpgraded = false;
		heartTemplate.upgradeCost = 10;
		heartTemplate.powerUpgradeCost = 0;
		heartTemplate.speedUpgradeCost = 0;
		heartTemplate.projectileName = null;
		
		heartTemplate.sprite = heartImage;
		heartTemplate.spawnSound = null;
		heartTemplate.despawnSound = null;
		heartTemplate.fireSound = null;
		
		templateMap = new TreeMap<String, Tower>();
		
		templateMap.put("earth", earthTemplate);
		templateMap.put("fire", fireTemplate);
		templateMap.put("wind", windTemplate);
		templateMap.put("water", waterTemplate);
		templateMap.put("heart", heartTemplate);		
	}
	
	private void setupEnemies() {
		availableEnemies = new Enemy[5];
		
		availableEnemies[0] = new Enemy("Marine");
		
		availableEnemies[0].speed = 16.0f;
		availableEnemies[0].defense = 1;
		availableEnemies[0].bounty = 2;
		availableEnemies[0].maxHealth = 5;
		
		availableEnemies[0].sprite = assets().getImage("images/enemies/Enemy_Normal_Med.png");
		availableEnemies[0].spawnSound = null;
		availableEnemies[0].despawnSound = null;
		
		availableEnemies[1] = new Enemy("Armored Soldier");
		
		availableEnemies[1].speed = 10.0f;
		availableEnemies[1].defense = 2;
		availableEnemies[1].bounty = 3;
		availableEnemies[1].maxHealth = 10;
		
		availableEnemies[1].sprite = assets().getImage("images/enemies/Enemy_Normal_Slow.png");
		availableEnemies[1].spawnSound = null;
		availableEnemies[1].despawnSound = null;
		
		availableEnemies[2] = new Enemy("Swift Soldier");
		
		availableEnemies[2].speed = 80.0f;
		availableEnemies[2].defense = 0;
		availableEnemies[2].bounty = 1;
		availableEnemies[2].maxHealth = 5;
		
		availableEnemies[2].sprite = assets().getImage("images/enemies/Enemy_Normal_Fast.png");
		availableEnemies[2].spawnSound = null;
		availableEnemies[2].despawnSound = null;
		
		availableEnemies[3] = new Enemy("Swift Carrier");
		
		availableEnemies[3].speed = 24.0f;
		availableEnemies[3].defense = 3;
		availableEnemies[3].bounty = 4;
		availableEnemies[3].maxHealth = 15;
		
		availableEnemies[3].sprite = assets().getImage("images/enemies/Enemy_Carrier_Fast.png");
		availableEnemies[3].spawnSound = null;
		availableEnemies[3].despawnSound = null;
		
		availableEnemies[4] = new Enemy("Armored Carrier");
		
		availableEnemies[4].speed = 8.0f;
		availableEnemies[4].defense = 8;
		availableEnemies[4].bounty = 5;
		availableEnemies[4].maxHealth = 100;
		
		availableEnemies[4].sprite = assets().getImage("images/enemies/Enemy_Carrier_Slow.png");
		availableEnemies[4].spawnSound = null;
		availableEnemies[4].despawnSound = null;
	}
	
	private void setupLevels() {
		levels = new Level[1];
		
		levels[0] = new Level("The Attack");
		
		levels[0].story = "An unknown enemy is attempting to attack your northern front, defend it!";
		levels[0].waveCount = 1;
		levels[0].background = assets().getImage("images/maps/Map1.png");
		levels[0].initialMoney = 100;
		levels[0].grid = new Object[20][29];
		levels[0].waveDelay = new float[] { 2000 };
		levels[0].enemies = new Enemy[1][1][1];
		levels[0].enemyCount = new int[1][1][1];
		
		for(Object[] o : levels[0].grid) {
			for(int i = 0; i < o.length; ++i) {
				o[i] = BUILDABLE;
			}
		}
		
		levels[0].enemies[0][0][0] = availableEnemies[SWIFT_SOLDIER];
		levels[0].enemyCount[0][0][0] = 10;
		
		levels[0].spawn = new Point[] { new Point(3, 0) };
		
		levels[0].path = new Point[][] { new Point[40] };
		
		levels[0].path[0][0] = new Point(3, 3);
		levels[0].path[0][1] = new Point(6, 3);
		levels[0].path[0][2] = new Point(6, 5);
		levels[0].path[0][3] = new Point(9, 5);
		levels[0].path[0][4] = new Point(9, 7);
		levels[0].path[0][5] = new Point(12, 7);
		levels[0].path[0][6] = new Point(12, 9);
		levels[0].path[0][7] = new Point(15, 9);
		levels[0].path[0][8] = new Point(15, 11);
		levels[0].path[0][9] = new Point(18, 11);
		levels[0].path[0][10] = new Point(18, 15);
		levels[0].path[0][11] = new Point(14, 15);
		levels[0].path[0][12] = new Point(14, 13);
		levels[0].path[0][13] = new Point(11, 13);
		levels[0].path[0][14] = new Point(11, 11);
		levels[0].path[0][15] = new Point(8, 11);
		levels[0].path[0][16] = new Point(8, 9);
		levels[0].path[0][17] = new Point(5, 9);
		levels[0].path[0][18] = new Point(5, 7);
		levels[0].path[0][19] = new Point(1, 7);
		levels[0].path[0][20] = new Point(1, 12);
		levels[0].path[0][21] = new Point(4, 12);
		levels[0].path[0][22] = new Point(4, 14);
		levels[0].path[0][23] = new Point(7, 14);
		levels[0].path[0][24] = new Point(7, 16);
		levels[0].path[0][25] = new Point(10, 16);
		levels[0].path[0][26] = new Point(10, 18);
		levels[0].path[0][27] = new Point(13, 18);
		levels[0].path[0][28] = new Point(13, 20);
		levels[0].path[0][29] = new Point(16, 20);
		levels[0].path[0][30] = new Point(16, 24);
		levels[0].path[0][31] = new Point(12, 24);
		levels[0].path[0][32] = new Point(12, 22);
		levels[0].path[0][33] = new Point(9, 22);
		levels[0].path[0][34] = new Point(9, 20);
		levels[0].path[0][35] = new Point(6, 20);
		levels[0].path[0][36] = new Point(6, 18);
		levels[0].path[0][37] = new Point(3, 18);
		levels[0].path[0][38] = new Point(3, 16);
		
		// End
		levels[0].path[0][39] = new Point(0, 16);
		
		//Map 2
		/*
		levels[1] = new Level("The Attack");
		
		levels[1].story = "An unknown enemy is attempting to attack your northern front, defend it!";
		levels[1].waveCount = 1;
		levels[1].background = assets().getImage("images/maps/Map2.png");
		levels[1].initialMoney = 100;
		levels[1].grid = new Object[20][29];
		levels[1].waveDelay = new float[] { 30000 };
		levels[1].enemies = new Enemy[1][1][1];
		levels[1].enemyCount = new int[1][1][1];
		
		for(Object[] o : levels[1].grid) {
			for(int i = 0; i < o.length; ++i) {
				o[i] = BUILDABLE;
			}
		}
		
		levels[1].enemies[0][0][0] = availableEnemies[SWIFT_SOLDIER];
		levels[1].enemyCount[0][0][0] = 1;
		
		levels[1].spawn = new Point[] { new Point(8, 0) };
		
		levels[1].path = new Point[][] { new Point[11] };
		
		levels[1].path[0][0] = new Point(8, 4);
		levels[1].path[0][1] = new Point(17, 4);
		levels[1].path[0][2] = new Point(17, 24);
		levels[1].path[0][3] = new Point(4, 24);
		levels[1].path[0][4] = new Point(4, 11);
		levels[1].path[0][5] = new Point(10, 11);
		levels[1].path[0][6] = new Point(10, 20);	
		levels[1].path[0][7] = new Point(15, 20);	
		levels[1].path[0][8] = new Point(15, 8);
		levels[1].path[0][9] = new Point(1, 8);
		// End
		levels[1].path[0][10] = new Point(1, 28);
		
		//Map 3
		
		levels[2] = new Level("The Attack");
		
		levels[2].story = "An unknown enemy is attempting to attack your northern front, defend it!";
		levels[2].waveCount = 1;
		levels[2].background = assets().getImage("images/maps/Map3.png");
		levels[2].initialMoney = 100;
		levels[2].grid = new Object[20][29];
		levels[2].waveDelay = new float[] { 30000 };
		levels[2].enemies = new Enemy[1][1][1];
		levels[2].enemyCount = new int[1][1][1];
		
		for(Object[] o : levels[2].grid) {
			for(int i = 0; i < o.length; ++i) {
				o[i] = BUILDABLE;
			}
		}
		
		levels[2].enemies[0][0][0] = availableEnemies[SWIFT_SOLDIER];
		levels[2].enemyCount[0][0][0] = 10;
		
		levels[2].spawn = new Point[] { new Point(2, 0) };
		
		levels[2].path = new Point[][] { new Point[10] };
		
		levels[2].path[0][0] = new Point(2, 25);
		levels[2].path[0][1] = new Point(5, 25);
		levels[2].path[0][2] = new Point(5, 5);
		levels[2].path[0][3] = new Point(8, 5);
		levels[2].path[0][4] = new Point(8, 21);
		levels[2].path[0][5] = new Point(12, 21);
		levels[2].path[0][6] = new Point(12, 9);
		levels[2].path[0][7] = new Point(16, 9);
		levels[2].path[0][8] = new Point(16, 19);
		//end
		levels[2].path[0][9] = new Point(19, 19);
		
		//Map 4
		
		levels[3] = new Level("The Attack");
		
		levels[3].story = "An unknown enemy is attempting to attack your northern front, defend it!";
		levels[3].waveCount = 1;
		levels[3].background = assets().getImage("images/maps/Map4.png");
		levels[3].initialMoney = 100;
		levels[3].grid = new Object[20][29];
		levels[3].waveDelay = new float[] { 30000 };
		levels[3].enemies = new Enemy[1][1][1];
		levels[3].enemyCount = new int[1][1][1];
		
		for(Object[] o : levels[3].grid) {
			for(int i = 0; i < o.length; ++i) {
				o[i] = BUILDABLE;
			}
		}
		
		levels[3].enemies[0][0][0] = availableEnemies[SWIFT_SOLDIER];
		levels[3].enemyCount[0][0][0] = 10;
		
		levels[3].spawn = new Point[] { new Point(0, 0) };
		
		levels[3].path = new Point[][] { new Point[14] };
		
		levels[3].path[0][0] = new Point(0, 28);
		levels[3].path[0][1] = new Point(19, 28);
		levels[3].path[0][2] = new Point(19, 3);
		levels[3].path[0][3] = new Point(3, 3);
		levels[3].path[0][4] = new Point(3, 25);
		levels[3].path[0][5] = new Point(16, 25);
		levels[3].path[0][6] = new Point(16, 6);
		levels[3].path[0][7] = new Point(6, 6);
		levels[3].path[0][8] = new Point(6, 22);
		levels[3].path[0][9] = new Point(13, 22);
		levels[3].path[0][10] = new Point(13, 9);
		levels[3].path[0][11] = new Point(9, 9);
		levels[3].path[0][12] = new Point(9, 19);
		//END
		levels[3].path[0][13] = new Point(11, 19);

		//MAP 5

		levels[4] = new Level("The Attack");
		
		levels[4].story = "An unknown enemy is attempting to attack your northern front, defend it!";
		levels[4].waveCount = 1;
		levels[4].background = assets().getImage("images/maps/Map4.png");
		levels[4].initialMoney = 100;
		levels[4].grid = new Object[20][29];
		levels[4].waveDelay = new float[] { 30000 };
		levels[4].enemies = new Enemy[1][1][1];
		levels[4].enemyCount = new int[1][1][1];
		
		for(Object[] o : levels[4].grid) {
			for(int i = 0; i < o.length; ++i) {
				o[i] = BUILDABLE;
			}
		}
		
		levels[4].enemies[0][0][0] = availableEnemies[SWIFT_SOLDIER];
		levels[4].enemyCount[0][0][0] = 10;
		
		levels[4].spawn = new Point[] { new Point(2, 0) };
		
		levels[4].path = new Point[][] { new Point[11] };
		
		levels[4].path[0][0] = new Point(2, 28);
		levels[4].path[0][1] = new Point(15, 28);
		levels[4].path[0][2] = new Point(15, 22);
		levels[4].path[0][3] = new Point(9, 22);
		levels[4].path[0][4] = new Point(9, 18);
		levels[4].path[0][5] = new Point(15, 18);
		levels[4].path[0][6] = new Point(15, 7);
		levels[4].path[0][7] = new Point(9, 7);
		levels[4].path[0][8] = new Point(9, 2);
		levels[4].path[0][9] = new Point(15, 2);
		//end
		levels[4].path[0][10] = new Point(15, 0);
		*/
	}
	
	public List<Enemy> getEnemiesInRange(Point p, int r) {
		int x = (int)p.x();
		int y = (int)p.y();
		
		List<Enemy> list = new ArrayList<Enemy>();
		
		for(int i = y - r; i <= y + r; ++i) {
			if(i < 0 || i > 19) {
				continue;
			}
			
			for(int j = x - r; j <= x + r; ++j) {
				if(j < 0 || j > 28) {
					continue;
				}
				
				list.addAll(collision[i][j]);
			}
		}
		
		return list;
	}
	
	public static Point pointGrid(Point p) {
		int x = (int)(p.x() / 36), y = (int)(p.y() / 36);
		
		return new Point(x, y);
	}
	
	public static Point centerPointGrid(Point p) {
		return new Point(p.y() * 36 + 36 / 2, p.x() * 36 + 36 / 2);
	}
	
	public static Point reverse(Point p) {
		return new Point(p.y(), p.x());
	}
}

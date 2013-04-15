package team.core;

import static playn.core.PlayN.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;

import playn.core.Image;
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
	private int speed;
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
	
	private Map<String, Tower> templateMap;
	
	private Tower fireTemplate, heartTemplate, earthTemplate, waterTemplate, windTemplate;
	
	private Tower placementTower;
	private boolean isMovingTower;
	private boolean isPlacingTower;
	
	private Image map, placementGrid;
	
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
				
				if(state != 2) {
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
		++speed;
		
		if(speed > 2) {
			speed = 2;
		}		
	}
	
	public void slowDownGame() {
		--speed;
		
		if(speed < 0) {
			speed = 0;
		}		
	}
	
	public String getSpeedText() {
		if(speed == 0) {
			return "1/2";
		} else if(speed == 1) {
			return "1";
		} else {
			return "2";
		}
	}
	
	public void showPauseMenu() {
		
	}
	
	public void showSettingsMenu() {
		
	}
	
	public void quit() {
		
	}
	
	public void paint(float alpha) {
		if(state == 0) {
			return;
		}
		
		if(state == 1) {
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
			surface.drawImage(E.getSprite(), centerPointGrid(E.getPosition()).x() - E.getSprite().width() / 2, centerPointGrid(E.getPosition()).y() - E.getSprite().height() / 2);
		}
	}
	
	public void update(float delta) {
		if(state == 0) {
			state = 1;
			
			delay = 0;
			// delay = 15000;
			
			level = -1;
			
			return;
		}
		
		if(state == 1) {
			delay -= delta;
			
			if(delay <= EPS) {
				state = 2;
				
				++level;
				
				map = levels[level].background;
				lives = levels[level].lives;
				money = levels[level].initialMoney;
				grid = levels[level].grid;
				wave = enemy = enemyIndex = 0;
				
				enemies.clear();
				projectiles.clear();
				towers.clear();
				
				spawns.clear();
				
				for(int i = 0; i < levels[level].spawn.length; ++i) {
					spawns.add(new SpawnPoint(levels[level].spawn[i], levels[level].path[i], levels[level].enemies[i], levels[level].enemyCount[i]));
				}
			}
			
			return;
		}
		
		for(int i = 0; i < spawns.size(); ++i) {
			if(spawns.get(i).index >= spawns.get(i).enemies.size()) {
				spawns.remove(i);
				
				--i;
				
				continue;
			}
			
			if(spawns.get(i).delay <= EPS) {
				int j = spawns.get(i).index;
				
				Enemy e = spawns.get(i).enemies.get(j);
				
				e.position = spawns.get(i).spawn;
				
				enemies.add(e);
				
				spawns.get(i).index = j + 1;
				
				spawns.get(i).delay = SPAWN_DELAY;
			} else {
				spawns.get(i).delay -= delta;
			}
		}
		
		for(Tower T : towers) {
			T.update(delta);
			
			if(T.getDelay() <= EPS) {
				T.setDelay(T.getSpeed());
			}
		}
		
		Iterator<Projectile> projectileIterator = projectiles.iterator();
		
		while(projectileIterator.hasNext()) {
			Projectile P = projectileIterator.next();
			
			if(!P.hasHitTarget()) {
				P.update(delta);
			} else {
				enemies.remove(P.getTarget());
			}
		}
		
		Iterator<Enemy> enemyIterator = enemies.iterator();
		
		while(enemyIterator.hasNext()) {
			Enemy E = enemyIterator.next();
			
			if(!E.hasReachedDestination()) {				
				E.update(delta);
			} else {
				--lives;
				
				enemyIterator.remove();
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
		setupTowers();
		setupEnemies();
		setupLevels();
	}
	
	private void setupTowers() {	
		Image fireImage = assets().getImage("images/towers/fireTower.png");
		Image heartImage = assets().getImage("images/towers/heartTower.png");
		Image earthImage = assets().getImage("images/towers/earthTower.png");
		Image windImage = assets().getImage("images/towers/windTower.png");
		Image waterImage = assets().getImage("images/towers/waterTower.png");
		
		fireTemplate = new Tower("Fire");
		
		fireTemplate.killCount = 0;
		fireTemplate.range = 3;
		fireTemplate.speed = 10;
		fireTemplate.delay = 3000;
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
		earthTemplate.range = 1;
		earthTemplate.speed = 10;
		earthTemplate.delay = 3000;
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
		waterTemplate.range = 6;
		waterTemplate.speed = 10;
		waterTemplate.delay = 2000;
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
		windTemplate.range = 1;
		windTemplate.speed = 10;
		windTemplate.delay = 1000;
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
		heartTemplate.range = 1;
		heartTemplate.speed = 10;
		heartTemplate.delay = 1000;
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
		heartTemplate.projectileName = "love";
		
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
		
		availableEnemies[0].speed = 2.0f;
		availableEnemies[0].defense = 1;
		availableEnemies[0].bounty = 2;
		availableEnemies[0].maxHealth = 5;
		
		availableEnemies[0].sprite = assets().getImage("images/enemies/Enemy_Normal_Med.png");
		availableEnemies[0].spawnSound = null;
		availableEnemies[0].despawnSound = null;
		
		availableEnemies[1] = new Enemy("Armored Soldier");
		
		availableEnemies[1].speed = 1.0f;
		availableEnemies[1].defense = 2;
		availableEnemies[1].bounty = 3;
		availableEnemies[1].maxHealth = 10;
		
		availableEnemies[1].sprite = assets().getImage("images/enemies/Enemy_Normal_Slow.png");
		availableEnemies[1].spawnSound = null;
		availableEnemies[1].despawnSound = null;
		
		availableEnemies[2] = new Enemy("Swift Soldier");
		
		availableEnemies[2].speed = 4.0f;
		availableEnemies[2].defense = 0;
		availableEnemies[2].bounty = 1;
		availableEnemies[2].maxHealth = 5;
		
		availableEnemies[2].sprite = assets().getImage("images/enemies/Enemy_Normal_Fast.png");
		availableEnemies[2].spawnSound = null;
		availableEnemies[2].despawnSound = null;
		
		availableEnemies[3] = new Enemy("Swift Carrier");
		
		availableEnemies[3].speed = 3.0f;
		availableEnemies[3].defense = 3;
		availableEnemies[3].bounty = 4;
		availableEnemies[3].maxHealth = 15;
		
		availableEnemies[3].sprite = assets().getImage("images/enemies/Enemy_Carrier_Fast.png");
		availableEnemies[3].spawnSound = null;
		availableEnemies[3].despawnSound = null;
		
		availableEnemies[4] = new Enemy("Armored Carrier");
		
		availableEnemies[4].speed = 1.0f;
		availableEnemies[4].defense = 5;
		availableEnemies[4].bounty = 5;
		availableEnemies[4].maxHealth = 30;
		
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
		levels[0].waveDelay = new float[] { 30000 };
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
		
		levels[0].path = new Point[][] { new Point[4] };
		
		levels[0].path[0][0] = new Point(3, 3);
		levels[0].path[0][1] = new Point(6, 3);
		levels[0].path[0][2] = new Point(6, 5);
		levels[0].path[0][3] = new Point(0, 16);
	}
	
	private Point pointGrid(Point p) {
		int x = (int)(p.x() / 36), y = (int)(p.y() / 36);
		
		return new Point(x, y);
	}
	
	private Point centerPointGrid(Point p) {
		return new Point(p.y() * 36 + 36 / 2, p.x() * 36 + 36 / 2);
	}
}

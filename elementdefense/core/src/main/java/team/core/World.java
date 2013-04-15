package team.core;

import static playn.core.PlayN.*;

import java.util.List;

import playn.core.Image;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import playn.core.Surface;
import playn.core.SurfaceLayer;
import pythagoras.f.Point;

public class World {
	private int money;
	private int level;
	private float delay;
	private String towerName;
	private Tower selectedTower;
	private Level[] levels;
	private SurfaceLayer layer;
	private Surface surface;
	private WorldGui gui;
	
	private List<Projectile> projectiles;
	private List<Enemy> enemies;
	private List<Tower> towers;
	
	private boolean isPlacingTower;
	
	private Image image, grid;
	
	private boolean finished;
	
	public World(SurfaceLayer layer, Level[] levels) {
		this.levels = levels;
		this.layer = layer;
		this.surface = layer.surface();
		
		layer.addListener(new Listener() {

			@Override
			public void onPointerStart(Event event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
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
		
		image = assets().getImage("images/maps/Map1.png");
		grid = assets().getImage("images/maps/Grid.png");
		
		gui = new WorldGui(layer);
		
		gui.show();
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void beginTowerPlacement(String name) {
		isPlacingTower = true;
	}
	
	public void cancelTowerPlacement() {
		isPlacingTower = false;
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
	
	public void loadTowers() {
		
	}
	
	public void paint(float alpha) {
		surface.drawImage(image, 0, 0);
		
		gui.paint(alpha);
	}
	
	public void update(float delta) {
		// TODO
	}
	
	public void mouseMove(Point p) {
		gui.mouseMove(p);
	}
}

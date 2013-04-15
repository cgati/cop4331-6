package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.SurfaceLayer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;

public class WorldGui extends Gui {
	Point lastPosition;
	Button speedUp, speedDown, moveTower, sellTower, fireTower, heartTower, rockTower, waterTower, windTower, upgradeTower;
	Label speedLabel, fireLabel, heartLabel, rockLabel, waterLabel, windLabel, moneyLabel, killLabel, spawnLabel;
	Image fireImage, heartImage, rockImage, waterImage, windImage;
	
	public WorldGui(SurfaceLayer layer) {
		super(layer);
		
		layer.addListener(new Listener() {
			@Override
			public void onPointerStart(Event event) {
				// TODO Auto-generated method stub
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				sellTower.pointerStart(p);
				moveTower.pointerStart(p);
				speedUp.pointerStart(p);
				speedDown.pointerStart(p);
				
				fireTower.pointerStart(p);
				heartTower.pointerStart(p);
				rockTower.pointerStart(p);
				windTower.pointerStart(p);
				waterTower.pointerStart(p);
				upgradeTower.pointerStart(p);
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
			
				sellTower.pointerEnd(p);
				moveTower.pointerEnd(p);
				speedUp.pointerEnd(p);
				speedDown.pointerEnd(p);
				
				fireTower.pointerEnd(p);
				heartTower.pointerEnd(p);
				rockTower.pointerEnd(p);
				windTower.pointerEnd(p);
				waterTower.pointerEnd(p);
				upgradeTower.pointerEnd(p);
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
		
		sellTower = new Button(new Point(1044, 720 - 36 * 3 - 20), "Sell") {
			@Override
			public void pressEvent() {
				// TODO Auto-generated method stub
			}
		};
		
		moveTower = new Button(new Point(1044, 720 - 36 * 4 - 30), "Move") {
			@Override
			public void pressEvent() {
				// TODO Auto-generated method stub
				
			}
		};
		
		upgradeTower = new Button(new Point(1044, 720 - 36 * 2 - 10), "Upgrade") {
			@Override
			public void pressEvent() {
				// TODO Auto-generated method stub
				
			}
		};
		
		speedUp = new Button(new Point(1280 - 72, 720 - 36 * 1), ">>") {
			@Override
			public void pressEvent() {
				// TODO Auto-generated method stub
				
			}
		};
		
		speedDown = new Button(new Point(1044, 720 - 36 * 1), "<<") {
			@Override
			public void pressEvent() {
				// TODO Auto-generated method stub
				
			}
		};
		
		Interval2D i2d;
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 5), 36, 36);
		
		fireTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("fire");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 6), 36, 36);
		
		heartTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("heart");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 7), 36, 36);
		
		rockTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("rock");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 8), 36, 36);
		
		waterTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("water");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 9), 36, 36);
		
		windTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("wind");
			}
		};
		
		fireImage = assets().getImage("images/towers/fireTower.png");
		heartImage = assets().getImage("images/towers/heartTower.png");
		rockImage = assets().getImage("images/towers/rockTower.png");
		windImage = assets().getImage("images/towers/windTower.png");
		waterImage = assets().getImage("images/towers/waterTower.png");
		
		fireLabel = new Label(new Point(1044, 36 * 5 - 24 / 2 + 36 / 2), "Cost: 100", 24);
		heartLabel = new Label(new Point(1044, 36 * 6 - 24 / 2 + 36 / 2), "Cost: 100", 24);
		rockLabel = new Label(new Point(1044, 36 * 7 - 24 / 2 + 36 / 2), "Cost: 100", 24);
		windLabel = new Label(new Point(1044, 36 * 8 - 24 / 2 + 36 / 2), "Cost: 100", 24);
		waterLabel = new Label(new Point(1044, 36 * 9 - 24 / 2 + 36 / 2), "Cost: 100", 24);
				
		moneyLabel = new Label(new Point(1044, 36 * 1), "Money: 10000", 24);
		killLabel = new Label(new Point(1044, 36 * 2), "Kills: 10000", 24);
		spawnLabel = new Label(new Point(1044, 36 * 3), "Spawn In: 30", 24);
		
		speedLabel = new Label(new Point(1280 - 236 / 2 - 36, 720 - 36 * 1), "1x");
	}

	@Override
	public void paint(float alpha) {
		if(hidden || disabled) {
			return;
		}
		
		surface.drawImage(fireImage, 1044 + 236 / 2 + 36, 36 * 5);
		surface.drawImage(heartImage, 1044 + 236 / 2 + 36, 36 * 6);
		surface.drawImage(rockImage, 1044 + 236 / 2 + 36, 36 * 7);
		surface.drawImage(windImage, 1044 + 236 / 2 + 36, 36 * 8);
		surface.drawImage(waterImage, 1044 + 236 / 2 + 36, 36 * 9);
		
		sellTower.paint(alpha, surface);
		moveTower.paint(alpha, surface);
		speedUp.paint(alpha, surface);
		speedDown.paint(alpha, surface);
		upgradeTower.paint(alpha, surface);
		
		fireTower.paint(alpha, surface);
		heartTower.paint(alpha, surface);
		rockTower.paint(alpha, surface);
		windTower.paint(alpha, surface);
		waterTower.paint(alpha, surface);
		
		speedLabel.paint(alpha, surface);
		fireLabel.paint(alpha, surface);
		heartLabel.paint(alpha, surface);
		rockLabel.paint(alpha, surface);
		windLabel.paint(alpha, surface);
		waterLabel.paint(alpha, surface);
		
		moneyLabel.paint(alpha, surface);
		killLabel.paint(alpha, surface);
		spawnLabel.paint(alpha, surface);
	}
	
	public void mouseMove(Point p) {		
		if(hidden || disabled) {
			return;
		}
		
		lastPosition = p;
		
		sellTower.mouseMove(p);
		moveTower.mouseMove(p);
		speedUp.mouseMove(p);
		speedDown.mouseMove(p);
		upgradeTower.mouseMove(p);
		
		fireTower.mouseMove(p);
		heartTower.mouseMove(p);
		rockTower.mouseMove(p);
		windTower.mouseMove(p);
		waterTower.mouseMove(p);
	}
}

package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.SurfaceLayer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;

public class WorldGui extends Gui {
	protected Point lastPosition;
	protected Button speedUp, speedDown, moveTower, sellTower, fireTower, heartTower, rockTower, waterTower, windTower, upgradeTower, upgradePower, upgradeSpeed, upgradeRange;
	protected Label speedLabel, fireLabel, heartLabel, rockLabel, waterLabel, windLabel, moneyLabel, livesLabel, spawnLabel, costLabel, nameLabel;
	protected Image fireImage, heartImage, rockImage, waterImage, windImage;
	
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
				upgradePower.pointerStart(p);
				upgradeSpeed.pointerStart(p);
				upgradeRange.pointerStart(p);
				
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
				upgradePower.pointerEnd(p);
				upgradeSpeed.pointerEnd(p);
				upgradeRange.pointerEnd(p);
				
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
		
		sellTower = new Button(new Point(1044, 720 - 36 * 3 - 20), "Sell", 24.0f) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().sellTower();
			}
			
			public void hover() {
				if(hidden) {
					return;
				}
				
				Tower T = ElementDefense.getInstance().getWorld().getSelectedTower();
				
				if(T != null) {
					setCost(T.getSellCost(), "Refund");
				}
			}
		};
		
		moveTower = new Button(new Point(1044, 720 - 36 * 4 - 30), "Move", 24.0f) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().moveTower();
			}
			
			public void hover() {
				if(hidden) {
					return;
				}
				
				Tower T = ElementDefense.getInstance().getWorld().getSelectedTower();
				
				if(T != null) {
					setCost(T.getMoveCost());
				}
			}
		};
		
		upgradeTower = new Button(new Point(1044, 720 - 36 * 2 - 10), "Upgrade", 24.0f) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().upgradeTower();
			}
			
			public void hover() {
				if(hidden) {
					return;
				}
				
				Tower T = ElementDefense.getInstance().getWorld().getSelectedTower();
				
				if(T != null) {
					setCost(T.getUpgradeCost(), T.getUpgradeName());
				}
			}
		};
		
		speedUp = new Button(new Point(1280 - 72, 720 - 36 * 1), ">>") {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().speedUpGame();				
			}
		};
		
		speedDown = new Button(new Point(1044, 720 - 36 * 1), "<<") {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().slowDownGame();				
			}
		};
		
		upgradePower = new Button(new Point(1044, 720 - 36 * 7 - 60), "Upgrade Power", 24.0f) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().upgradeTowerPower();				
			}
			
			public void hover() {
				if(hidden) {
					return;
				}
				
				Tower T = ElementDefense.getInstance().getWorld().getSelectedTower();
				
				if(T != null) {
					setCost(T.getPowerUpgradeCost());
				}
			}
		};
		
		upgradeSpeed = new Button(new Point(1044, 720 - 36 * 6 - 50), "Upgrade Speed", 24.0f) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().upgradeTowerSpeed();				
			}
			
			public void hover() {
				if(hidden) {
					return;
				}
				
				Tower T = ElementDefense.getInstance().getWorld().getSelectedTower();
				
				if(T != null) {
					setCost(T.getSpeedUpgradeCost());
				}
			}
		};
		
		upgradeRange = new Button(new Point(1044, 720 - 36 * 5 - 40), "Upgrade Range", 24.0f) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().upgradeTowerRange();				
			}
			
			public void hover() {
				if(hidden) {
					return;
				}
				
				Tower T = ElementDefense.getInstance().getWorld().getSelectedTower();
				
				if(T != null) {
					setCost(T.getRangeUpgradeCost());
				}
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
		
		rockTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("earth");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 7), 36, 36);
		
		waterTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("water");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 8), 36, 36);
		
		windTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("wind");
			}
		};
		
		i2d = new Interval2D(new Point(1044 + 236 / 2 + 36, 36 * 9), 36, 36);
		
		heartTower = new Button(i2d) {
			@Override
			public void pressEvent() {
				ElementDefense.getInstance().getWorld().beginTowerPlacement("heart");
			}
		};
		
		fireImage = assets().getImage("images/towers/fireTower.png");
		rockImage = assets().getImage("images/towers/rockTower.png");
		windImage = assets().getImage("images/towers/waterTower.png");
		waterImage = assets().getImage("images/towers/windTower.png");
		heartImage = assets().getImage("images/towers/heartTower.png");
				
		fireLabel = new Label(new Point(1044, 36 * 5 - 24 / 2 + 36 / 2), "Cost: 5", 24);		
		rockLabel = new Label(new Point(1044, 36 * 6 - 24 / 2 + 36 / 2), "Cost: 5", 24);
		windLabel = new Label(new Point(1044, 36 * 7 - 24 / 2 + 36 / 2), "Cost: 5", 24);
		waterLabel = new Label(new Point(1044, 36 * 8 - 24 / 2 + 36 / 2), "Cost: 5", 24);
		heartLabel = new Label(new Point(1044, 36 * 9 - 24 / 2 + 36 / 2), "Cost: 25", 24);
		nameLabel = new Label(new Point(1044, 36 * 10 - 24 / 2 + 36 / 2), "Name: ", 24);
		
		moneyLabel = new Label(new Point(1044, 36 * 1), "Money: 10000", 24);
		livesLabel = new Label(new Point(1044, 36 * 2), "Kills: 10000", 24);
		spawnLabel = new Label(new Point(1044, 36 * 3), "Spawn In: 30", 24);
		costLabel = new Label(new Point(1044, 36 * 4), "Cost: -", 24);
		
		speedLabel = new Label(new Point(1280 - 236 + 72, 720 - 36 * 1), "1x");
		
		hideSelectedMenu();
	}
	
	public void setSpawn(Integer spawn) {
		if(spawn == null) {
			spawnLabel.setText("Spawn In: -");
		} else {
			spawnLabel.setText("Spawn In: " + spawn);
		}
	}
	
	public void setMoney(int money) {
		moneyLabel.setText("Money: " + money);
	}
	
	public void setLives(int lives) {
		livesLabel.setText("Lives: " + lives);
	}
	
	public void setCost(Integer cost) {
		if(cost == null) {
			costLabel.setText("Cost: -");
		} else {
			costLabel.setText("Cost: " + cost);
		}
	}
	
	public void setCost(Integer cost, String name) {
		if(cost == null || name == null) {
			costLabel.setText("Cost: -");
		} else {
			costLabel.setText(name + ": " + cost);
		}
	}
	
	public void setName(String name) {
		nameLabel.setText(name + " Tower");
	}
	
	public void setSpeed(String speed) {
		speedLabel.setText(speed + "x");
	}
	
	public void showSelectedMenu(String name) {
		setName(name);
		
		nameLabel.show();
		sellTower.show();
		moveTower.show();
		upgradePower.show();
		upgradeSpeed.show();
		upgradeRange.show();
		upgradeTower.show();
	}
	
	public void hideSelectedMenu() {
		nameLabel.hide();
		sellTower.hide();
		moveTower.hide();
		upgradePower.hide();
		upgradeSpeed.hide();
		upgradeRange.hide();
		upgradeTower.hide();		
	}
	
	@Override
	public void paint(float alpha) {
		if(hidden || disabled) {
			return;
		}
		
		surface.drawImage(fireImage, 1044 + 236 / 2 + 36, 36 * 5);
		surface.drawImage(rockImage, 1044 + 236 / 2 + 36, 36 * 6);
		surface.drawImage(windImage, 1044 + 236 / 2 + 36, 36 * 7);
		surface.drawImage(waterImage, 1044 + 236 / 2 + 36, 36 * 8);
		surface.drawImage(heartImage, 1044 + 236 / 2 + 36, 36 * 9);
		
		sellTower.paint(alpha, surface);
		moveTower.paint(alpha, surface);
		speedUp.paint(alpha, surface);
		speedDown.paint(alpha, surface);
		upgradeTower.paint(alpha, surface);
		upgradePower.paint(alpha, surface);
		upgradeSpeed.paint(alpha, surface);
		upgradeRange.paint(alpha, surface);
		
		fireTower.paint(alpha, surface);
		heartTower.paint(alpha, surface);
		rockTower.paint(alpha, surface);
		windTower.paint(alpha, surface);
		waterTower.paint(alpha, surface);
		
		nameLabel.paint(alpha, surface);
		speedLabel.paint(alpha, surface);
		fireLabel.paint(alpha, surface);
		heartLabel.paint(alpha, surface);
		rockLabel.paint(alpha, surface);
		windLabel.paint(alpha, surface);
		waterLabel.paint(alpha, surface);
		
		moneyLabel.paint(alpha, surface);
		livesLabel.paint(alpha, surface);
		spawnLabel.paint(alpha, surface);
		costLabel.paint(alpha, surface);
	}
	
	public void mouseMove(Point p) {		
		if(hidden || disabled) {
			return;
		}
		
		setCost(null);
		
		lastPosition = p;
		
		sellTower.mouseMove(p);
		moveTower.mouseMove(p);
		speedUp.mouseMove(p);
		speedDown.mouseMove(p);
		upgradeTower.mouseMove(p);
		upgradePower.mouseMove(p);
		upgradeSpeed.mouseMove(p);
		upgradeRange.mouseMove(p);
		
		fireTower.mouseMove(p);
		heartTower.mouseMove(p);
		rockTower.mouseMove(p);
		windTower.mouseMove(p);
		waterTower.mouseMove(p);
	}
}

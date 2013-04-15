package team.core;

import java.util.HashMap;
import java.util.Map;

import playn.core.Image;
import playn.core.Sound;
import pythagoras.f.Point;

public abstract class ElementObject {
	protected String name;
	protected Image sprite;
	protected Sound spawnSound;
	protected Sound despawnSound;

	protected Point position;
	
	protected Map<String, String> state;
	
	public ElementObject(String name, Point position) {
		this.name = name;
		this.position = position;
		
		state = new HashMap<String, String>();
	}
	
	public String getName() {
		return name;
	}
	
	public Image getSprite() {
		return sprite;
	}
	
	public Sound getSpawnSound() {
		return spawnSound;
	}
	
	public Sound getDespawnSound() {
		return despawnSound;
	}
	
	public Point getPosition() {
		return position;
	}
	
	public abstract void update(float delta);
}

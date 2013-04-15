package team.core;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import pythagoras.f.Point;

public abstract class Enhanceable extends ElementObject {
	protected List<Enhancer> enhancers;

	public Enhanceable(String name, Point position) {
		super(name, position);
		
		enhancers = new ArrayList<Enhancer>();
	}
	
	public List<Enhancer> getEnhancers() {
		return enhancers;
	}
	
	public void update(float delta) {
		Iterator<Enhancer> iterator = enhancers.iterator();
		
		while(iterator.hasNext()) {
			Enhancer E = iterator.next();
			
			if(E.isPermanent()) {
				continue;
			}
			
			E.setDuration(E.getDuration() - delta);
			
			if(E.getDuration() <= World.EPS) {
				iterator.remove();
			}
		}
	}
	
	public void addEnhancer(int type, float scalar, float duration) {
		enhancers.add(new Enhancer(type, scalar, duration));
	}
	
	public float getPowerMultiplier() {
		float multiplier = 1.0f;
		
		for(Enhancer E : enhancers) {
			if(E.getType() == Enhancer.DAMAGE) {
				multiplier *= E.getScalar();
			}
		}
		
		return multiplier;
	}
	
	public float getSpeedMultiplier() {
		float multiplier = 1.0f;
		
		for(Enhancer E : enhancers) {
			if(E.getType() == Enhancer.SPEED) {
				multiplier *= E.getScalar();
			}
		}
		
		return multiplier;
	}
	
	public float getRangeMultiplier() {
		float multiplier = 1.0f;
		
		for(Enhancer E : enhancers) {
			if(E.getType() == Enhancer.RANGE) {
				multiplier *= E.getScalar();
			}
		}
		
		return multiplier;
	}
}

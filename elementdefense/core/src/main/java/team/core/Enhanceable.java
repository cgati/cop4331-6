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
			
			if(E.getDuration() < 0) {
				iterator.remove();
			}
		}
	}
	
	public void addEnhancer(int type, float scalar, float duration) {
		enhancers.add(new Enhancer(type, scalar, duration));
	}
	
	public void addEnhancer(int type, float scalar, float duration, boolean permanent) {
		Enhancer E = new Enhancer(type, scalar, duration);
		
		if(permanent) {
			E.permanent();
		}
		
		enhancers.add(E);
	}
	
	public void removeEnhancer(int type, float scalar) {
		Iterator<Enhancer> iterator = enhancers.iterator();
		
		while(iterator.hasNext()) {
			Enhancer E = iterator.next();
			
			if(E.getType() == type && Math.abs(E.getScalar() - scalar) <= World.EPS) {
				iterator.remove();
				
				break;
			}
		}
	}
	
	public boolean hasEnhancer(int type, float scalar) {
		Iterator<Enhancer> iterator = enhancers.iterator();
		
		while(iterator.hasNext()) {
			Enhancer E = iterator.next();
			
			if(E.getType() == type && Math.abs(E.getScalar() - scalar) <= World.EPS) {
				return true;
			}
		}
		
		return false;
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

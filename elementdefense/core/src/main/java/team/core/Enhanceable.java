package team.core;

import java.util.List;

public abstract class Enhanceable extends ElementObject {
	protected List<Enhancer> enhancers;

	public Enhanceable(String name, Vector position) {
		super(name, position);
		// TODO Auto-generated constructor stub
	}
	
	public List<Enhancer> getEnhancers() {
		return enhancers;
	}
	
	public void addEnhancer(int type, float scalar, float duration) {
		enhancers.add(new Enhancer(type, scalar, duration));
	}
	
	public void removeEnhancer(int type, float scalar) {
		// TODO
	}
}

package team.core;

import playn.core.Surface;
import playn.core.SurfaceLayer;
import pythagoras.f.Point;

public abstract class Gui {
	protected SurfaceLayer layer;
	protected Surface surface;
	
	protected boolean hidden, disabled;
	protected float alpha;
	
	public Gui(SurfaceLayer layer) {
		this.layer = layer;
		this.surface = layer.surface();
		
		hidden = true;
		disabled = false;
	}
	
	public void show() {
		hidden = false;
	}
	
	public void hide() {
		hidden = true;
	}
	
	public void disable() {
		disabled = true;
	}
	
	public void mouseMove(Point p) {
		
	}
	
	public abstract void paint(float alpha);
}

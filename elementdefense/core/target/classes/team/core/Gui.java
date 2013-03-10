package team.core;

import playn.core.SurfaceLayer;

public abstract class Gui {
	protected SurfaceLayer surface;
	
	public Gui(SurfaceLayer surface) {
		this.surface = surface;
	}
	
	public abstract void paint(float alpha);
}

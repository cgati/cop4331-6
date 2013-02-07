package team.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Surface;
import playn.core.SurfaceLayer;

public class ElementDefense implements Game {
	Surfacelayer layer;
	
	@Override
	public void init() {
		// create and add background image layer
		layer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
		graphics().rootLayer().add(layer);
	}

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything here!
		
		layer.surface().clear();
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public int updateRate() {
		return 25;
	}
}

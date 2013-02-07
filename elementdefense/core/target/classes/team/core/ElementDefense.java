package team.core;

import static playn.core.PlayN.*;

import playn.core.Game;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.SurfaceLayer;

public class ElementDefense implements Game {
  @Override
  public void init() {
    // create and add background image layer
	  SurfaceLayer layer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
	  layer.surface().setFillColor(0xFF00FF00);
	  layer.surface().drawLine(0, 0, graphics().width(), graphics().height(), 10);
	  layer.surface().setFillColor(0xFF00FFFF);
	  layer.surface().drawLine(graphics().width(), 0, 0, graphics().height(), 10);
	  graphics().rootLayer().add(layer);
  }

  @Override
  public void paint(float alpha) {
    // the background automatically paints itself, so no need to do anything here!
  }

  @Override
  public void update(float delta) {
  }

  @Override
  public int updateRate() {
    return 25;
  }
}

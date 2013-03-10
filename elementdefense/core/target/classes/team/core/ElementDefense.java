package team.core;

import static playn.core.PlayN.*;

import java.util.List;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Game;
import playn.core.Layer;
import playn.core.SurfaceLayer;
import playn.core.TextFormat;
import playn.core.TextLayout;

public class ElementDefense implements Game {
	private int state;
	private World currentWorld;
	private SurfaceLayer layer;
	
	private SelectBox selectResolutionBox;
	private List<Button> buttons;
	
	@Override
	public void init() {
		// create and add background image layer
		layer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
		
		graphics().rootLayer().add(layer);
	}
	
	public void startLevel(String name) {
		
	}
	
	public void showSelectLeveMenu() {
		
	}
	
	public void showSettingsMenu() {
		
	}
	
	public void quit() {
		
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
	
	private void loadAssets() {
		
	}
}

package com.cgati.cop4331.towerdefense.core;

import static playn.core.PlayN.*;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Game;
import playn.core.GroupLayer;
import playn.core.Image;
import playn.core.ImageLayer;
import playn.core.Key;
import playn.core.Keyboard;
import playn.core.Keyboard.Event;
import playn.core.Keyboard.TypedEvent;
import playn.core.Layer;
import playn.core.Log;
import playn.core.SurfaceLayer;
import playn.core.TextFormat;
import playn.core.TextLayout;

public class TowerDefense implements Game {
	private static class KeyboardListener implements Keyboard.Listener {
		public static int down = 0;
		public static final boolean[] state = new boolean[Key.values().length]; 

		@Override
		public void onKeyDown(Event event) {
			// TODO Auto-generated method stub
			state[event.key().ordinal()] = true;
			++down;
		}

		@Override
		public void onKeyTyped(TypedEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onKeyUp(Event event) {
			// TODO Auto-generated method stub
			state[event.key().ordinal()] = false;
			--down;
		}
		
	}
	
	SurfaceLayer layer;
	
  @Override
  public void init() {
    // create and add background image layer
    // Image bgImage = assets().getImage("images/bg.png");
    // ImageLayer bgLayer = graphics().createImageLayer(bgImage);
    // graphics().rootLayer().add(bgLayer);
	  keyboard().setListener(new KeyboardListener());
    Font F = graphics().createFont("Monospaced", Font.Style.PLAIN, 32);
    TextLayout layout = graphics().layoutText("This is TEXT!", new TextFormat(F, 300, TextFormat.Alignment.CENTER));
    CanvasImage image = graphics().createImage(layout.width(), layout.height());
    image.canvas().setFillColor(0xFF00FF00);
    image.canvas().fillText(layout, 0, 0);
    layer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
    layer.surface().drawImage(image, 0, 0);
    graphics().rootLayer().add(layer);
  }
  
  int i = 0, MAX = 1000;
  long sum = 0;
  long[] d = new long[MAX];
  double fps = 0.0;
  java.util.Date last;

  @Override
  public void paint(float alpha) {
	  java.util.Date date = new java.util.Date();
	  if(last==null) last=date;
	  else {
		  long diff = date.getTime()-last.getTime();
		  
		  sum -= d[i];
		  d[i] = diff;
		  sum += d[i];
		  
		  ++i;
		  
		  if(i >= MAX) i = 0;
		  
		  fps = 1000.0 / (sum / (double)MAX);
		  
		  last = date;
	  }
	  
    // the background automatically paints itself, so no need to do anything here!
	  layer.surface().clear();
	    Font F = graphics().createFont("Monospaced", Font.Style.PLAIN, 32);
	    TextLayout layout = graphics().layoutText(String.format("Down: %d Fps: %d", KeyboardListener.down, Math.round(fps)), new TextFormat(F, 600, TextFormat.Alignment.CENTER));
	    CanvasImage image = graphics().createImage(layout.width(), layout.height());
	    image.canvas().setFillColor(0xFF00FF00);
	    image.canvas().fillText(layout, 0, 0);
	    ImageLayer imageLayer = graphics().createImageLayer(image);
	    layer.surface().drawImage(image, 0, 0);
  }

  @Override
  public void update(float delta) {
  }

  @Override
  public int updateRate() {
    return 25;
  }
}

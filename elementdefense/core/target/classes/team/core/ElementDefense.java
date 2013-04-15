package team.core;

import static playn.core.PlayN.*;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Game;
import playn.core.Image;
import playn.core.Layer;
import playn.core.TextFormat;
import playn.core.TextLayout;
import playn.core.Mouse.ButtonEvent;
import playn.core.Mouse.Listener;
import playn.core.Mouse.MotionEvent;
import playn.core.Mouse.WheelEvent;
import playn.core.SurfaceLayer;
import pythagoras.f.Point;

public class ElementDefense implements Game {
	private float volume;
	private boolean inGame;
	private World currentWorld;
	private SurfaceLayer layer;
	
	private static final TextFormat textFormat = new TextFormat(graphics().createFont("Courier New", Font.Style.PLAIN, 36.0f), 1280, TextFormat.Alignment.LEFT);
	
	private MainMenuGui mainMenu;
	private OptionsMenuGui optionsMenu;
	
	private final float WIDTH = 1280.0f, HEIGHT = 720.0f;
	
	public static Point mousePosition;
	
	private static ElementDefense defense;
	public static ElementDefense getInstance() {
		return defense;
	}
	
	@Override
	public void init() {
		if(defense != null) {
			throw new RuntimeException("You're trying to run multiple games...");
		}
		
		volume = 1.0f;
		
		defense = this;
		
		mouse().setListener(new Listener() {

			@Override
			public void onMouseDown(ButtonEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseUp(ButtonEvent event) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMouseMove(MotionEvent event) {
				// TODO Auto-generated method stub
				
				Point p = Layer.Util.screenToLayer(graphics().rootLayer(), event.x(), event.y());
				
				mainMenu.mouseMove(p);
				optionsMenu.mouseMove(p);
				currentWorld.mouseMove(p);
			}

			@Override
			public void onMouseWheelScroll(WheelEvent event) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		// create and add background image layer		
		graphics().ctx().setSize((int)WIDTH, (int)HEIGHT);		
		
		layer = graphics().createSurfaceLayer(WIDTH, HEIGHT);
	
		graphics().rootLayer().add(layer);
		
		mainMenu = new MainMenuGui(layer);
		optionsMenu = new OptionsMenuGui(layer);
		
		mainMenu.show();
		
		resize(1280,720);
		
		currentWorld = new World(layer, null);
	}
	
	public void startGame() {
		mainMenu.hide();
		
		inGame = true;
	}
	
	public World getWorld() {
		return currentWorld;
	}
	
	public OptionsMenuGui getOptionsGui() {
		return optionsMenu;
	}
	
	public void quit() {
		
	}
	
	public void resize(float width, float height) {
		graphics().ctx().setSize((int)width, (int)height);
		
		graphics().rootLayer().setScale(width/WIDTH, height/HEIGHT);
	}
	
	@Override
	public void paint(float alpha) {
		layer.surface().clear();

		mainMenu.paint(alpha);
		optionsMenu.paint(alpha);
		
		if(inGame) {
			currentWorld.paint(alpha);
		}
	}
	
	@Override
	public void update(float delta) {
	}
	
	@Override
	public int updateRate() {
		return 25;
	}
	
	public float getVolume() {
		return volume;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
	}
	
	public static Image getTextImage(String text) {
		TextLayout textLayout = graphics().layoutText(text, textFormat);
		CanvasImage image = graphics().createImage(textLayout.width(), textLayout.height());
		image.canvas().setFillColor(0xFFFFFFFF);
		image.canvas().fillText(textLayout, 0, 0);
		
		return image;		
	}
	
	public static Image getTextImage(String text, float wrap) {
		TextFormat textFormat = new TextFormat(graphics().createFont("Courier New", Font.Style.PLAIN, 36.0f), wrap, TextFormat.Alignment.LEFT);
		TextLayout textLayout = graphics().layoutText(text, textFormat);
		CanvasImage image = graphics().createImage(textLayout.width(), textLayout.height());
		image.canvas().setFillColor(0xFFFFFFFF);
		image.canvas().fillText(textLayout, 0, 0);
		
		return image;		
	}
}

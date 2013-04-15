package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.SurfaceLayer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;

public class MainMenuGui extends Gui {
	private Image mainMenuImage;
	private Button playButton, optionsButton;

	public MainMenuGui(SurfaceLayer layer) {
		super(layer);
		
		layer.addListener(new Listener() {
			@Override
			public void onPointerStart(Event event) {
				// TODO Auto-generated method stub
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				playButton.pointerStart(p);
				optionsButton.pointerStart(p);
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				playButton.pointerEnd(p);
				optionsButton.pointerEnd(p);
			}

			@Override
			public void onPointerDrag(Event event) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onPointerCancel(Event event) {
				// TODO Auto-generated method stub
			}
			
		});
		
		Interval2D i2d;
		
		i2d = new Interval2D(new Interval(540,740), new Interval(400,470));
		playButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				ElementDefense.getInstance().startGame();				
			}
			
		};
		
		i2d = new Interval2D(new Interval(540,740), new Interval(512,582));
		optionsButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				hide();
				
				ElementDefense.getInstance().getOptionsGui().show();
				
				ElementDefense.getInstance().getOptionsGui().alert(MainMenuGui.this);
			}
			
		};
		
		mainMenuImage = assets().getImage("images/MainMenu.png");
	}
	
	public void mouseMove(Point p) {
		if(hidden || disabled) {
			return;
		}
		
		playButton.mouseMove(p);
		optionsButton.mouseMove(p);
	}

	@Override
	public void paint(float alpha) {
		if(hidden) {
			return;
		}
		
		surface.drawImage(mainMenuImage, 0, 0);
		
		playButton.paint(alpha, surface);
		optionsButton.paint(alpha, surface);
	}

}

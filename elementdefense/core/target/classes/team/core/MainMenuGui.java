package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.SurfaceLayer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;

public class MainMenuGui extends Gui {
	private Image mainMenuImage;
	private Button playButton, endlessButton, optionsButton;

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
				endlessButton.pointerStart(p);
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
				endlessButton.pointerEnd(p);
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
		
		i2d = new Interval2D(new Point(280, 430), 200, 75);
		playButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				ElementDefense.getInstance().startGame();				
			}
			
		};

		i2d = new Interval2D(new Point(800, 430), 200, 75);
		optionsButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				MainMenuGui.this.hide();
				
				ElementDefense.getInstance().getOptionsGui().show();
				
				ElementDefense.getInstance().getOptionsGui().alert(MainMenuGui.this);
			}
			
		};
		
		i2d = new Interval2D(new Point(540, 430), 200, 75);
		endlessButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				ElementDefense.getInstance().startEndless();
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
		endlessButton.mouseMove(p);
	}

	@Override
	public void paint(float alpha) {
		if(hidden) {
			return;
		}
		
		surface.drawImage(mainMenuImage, 0, 0);
		
		playButton.paint(alpha, surface);
		optionsButton.paint(alpha, surface);
		endlessButton.paint(alpha, surface);
	}

}

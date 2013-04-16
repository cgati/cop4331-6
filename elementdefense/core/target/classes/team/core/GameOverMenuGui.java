package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.SurfaceLayer;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import pythagoras.f.Point;

public class GameOverMenuGui extends Gui {
	private Image gameOverImage;
	private Button replayButton, menuButton;

	public GameOverMenuGui(SurfaceLayer layer) {
		super(layer);
		
		layer.addListener(new Listener() {
			@Override
			public void onPointerStart(Event event) {
				// TODO Auto-generated method stub
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				replayButton.pointerStart(p);
				menuButton.pointerStart(p);
			}

			@Override
			public void onPointerEnd(Event event) {
				// TODO Auto-generated method stub
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				replayButton.pointerEnd(p);
				menuButton.pointerEnd(p);
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
		replayButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				GameOverMenuGui.this.hide();
				
				ElementDefense.getInstance().quit();
				ElementDefense.getInstance().startGame();				
			}
			
		};

		i2d = new Interval2D(new Point(800, 430), 200, 75);
		menuButton = new Button(i2d) {

			@Override
			public void pressEvent() {
				GameOverMenuGui.this.hide();
				
				ElementDefense.getInstance().quit();
			}
			
		};
		
		gameOverImage = assets().getImage("images/GameOver.png");
	}
	
	public void mouseMove(Point p) {
		if(hidden || disabled) {
			return;
		}
		
		replayButton.mouseMove(p);
		menuButton.mouseMove(p);
	}

	@Override
	public void paint(float alpha) {
		if(hidden) {
			return;
		}
		
		surface.drawImage(gameOverImage, 0, 0);
		
		replayButton.paint(alpha, surface);
		menuButton.paint(alpha, surface);
	}

}

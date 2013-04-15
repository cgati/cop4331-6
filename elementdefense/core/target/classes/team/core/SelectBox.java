package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.Surface;
import pythagoras.f.Point;

public class SelectBox {
	private String[] options;
	private Image[] background;
	private Button upButton, downButton;
	private Point position;
	
	private static Image upImage, downImage;
	
	public static void getAssets() {
		if(upImage == null) {
			upImage = assets().getImage("images/up.png");
			downImage = assets().getImage("images/down.png");
		}
	}
	
	private int selected;
	
	public final int NORMAL = 0;
	public final int PRESSED = 1;
	public final int HOVER = 2;
	
	public SelectBox(Point position, String[] options) {
		this.options = options;
		this.position = position;
		
		generateBackground();
		
		initialize();
	}
	
	public SelectBox(Point position, String[] options, Image[] background) {
		this.options = options;
		this.background = background;		
		this.position = position;
		
		initialize();
	}
	
	public void initialize() {
		getAssets();
		
		Interval2D i2d;
		
		i2d = new Interval2D(new Interval(position.x() + 180, position.x() + 180 + 15), new Interval(position.y(), position.y() + 14));
		
		upButton = new Button(i2d) {
			@Override
			public void pressEvent() {
				incrementSelected();				
			}			
		};
		
		i2d = new Interval2D(new Interval(position.x() + 180, position.x() + 180 + 15), new Interval(position.y() + 14, position.y() + 28));
		
		downButton = new Button(i2d) {
			@Override
			public void pressEvent() {
				decrementSelected();				
			}			
		};
	}
	
	public void paint(float alpha, Surface surface) {
		surface.drawImage(getBackground(), position.x(), position.y() + (14 - getBackground().height() / 2));
		surface.drawImage(upImage, position.x() + 180, position.y());
		surface.drawImage(downImage, position.x() + 180, position.y() + upImage.height());
		
		upButton.paint(alpha, surface);
		downButton.paint(alpha, surface);
	}
	
	public void pointerStart(Point p) {
		upButton.pointerStart(p);
		downButton.pointerStart(p);
	}
	
	public void pointerEnd(Point p) {
		upButton.pointerEnd(p);
		downButton.pointerEnd(p);
	}
	
	public void mouseMove(Point p) {
		upButton.mouseMove(p);
		downButton.mouseMove(p);		
	}
	
	public String getSelected() {
		return options[selected];
	}
	
	public void incrementSelected() {		
		if(selected + 1 >= options.length) {
			selected = 0;
		} else {
			++selected;
		}
	}
	
	public void decrementSelected() {		
		if(selected - 1 < 0) {
			selected = options.length - 1;
		} else {
			--selected;
		}
	}
	
	public void setSelected(int index) {
		selected = index;
	}
	
	public String[] getOptions() {
		return options;
	}
	
	public Image getBackground() {
		return background[selected];
	}
	
	private void generateBackground() {
		background = new Image[options.length];
		
		for(int i = 0; i < options.length; ++i) {
			background[i] = ElementDefense.getTextImage(options[i]);
		}
	}
}

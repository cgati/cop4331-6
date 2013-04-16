package team.core;

import static playn.core.PlayN.*;

import playn.core.CanvasImage;
import playn.core.Image;
import playn.core.Surface;
import pythagoras.f.Point;

public abstract class Button {
	private String text;
	private Image[] background;
	private Interval2D boundingBox;
	
	private boolean hidden;
	
	private int state;
	
	public static final int NORMAL = 0;
	public static final int PRESSED = 1;
	public static final int HOVER = 2;
	
	public Button(Interval2D boundingBox) {
		this.boundingBox = boundingBox;
		
		generateBackground();
	}
	
	public Button(Point position, String text) {
		this(position, text, 36.0f);
	}
	
	public Button(Point position, String text, float size) {
		this.text = text;
		
		generateBackground(text, size);

		this.boundingBox = new Interval2D(new Interval(position.x(), position.x() + background[0].width()), new Interval(position.y(), position.y() + background[0].height()));
	}
	
	public Button(Interval2D boundingBox, String text, Image[] background) {
		this.text = text;
		this.background = background;
		this.boundingBox = boundingBox;
	}
	
	public void paint(float alpha, Surface surface) {
		if(hidden) {
			return;
		}
		
		surface.drawImage(background[state], boundingBox.x0(), boundingBox.y0());
	}
	
	public String getText() {
		return text;
	}
	
	public Image getBackground() {
		return background[state];
	}
	
	public Interval2D getBoundingBox() {
		return boundingBox;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		if(state < 0 || state > 2) {
			state = 0;
		}
		
		this.state = state;
	}
	
	public void hide() {
		hidden = true;
	}
	
	public void show() {
		hidden = false;
	}
	
	public void mouseMove(Point p) {
		if(hidden) {
			return;
		}
		
		if(getBoundingBox().intersect(p)) {
			if(getState() != PRESSED) {
				setState(HOVER);
				
				hover();
			}
		} else {
			if(getState() != PRESSED) {
				setState(NORMAL);
			}
		}
	}
	
	public void pointerStart(Point p) {
		if(hidden) {
			return;
		}
		
		if(getBoundingBox().intersect(p) || getState() == PRESSED) {
			setState(PRESSED);
		} else {
			setState(NORMAL);
		}
	}
	
	public void pointerEnd(Point p) {
		if(hidden) {
			return;
		}
		
		if(getBoundingBox().intersect(p)) {
			setState(HOVER);
			
			pressEvent();
			
			hover();
		} else {
			setState(NORMAL);
		}
	}
	
	public void hover() {
	}
	
	private void generateBackground() {
		background = new Image[3];
		
		float width = boundingBox.width();
		float height = boundingBox.height();
		
		background[0] = graphics().createImage(width, height);
		background[1] = graphics().createImage(width, height);
		((CanvasImage)background[1]).canvas().setFillColor(0x88111111);
		((CanvasImage)background[1]).canvas().fillRect(0, 0, width, height);
		background[2] = graphics().createImage(width, height);
		((CanvasImage)background[2]).canvas().setFillColor(0x33111111);
		((CanvasImage)background[2]).canvas().fillRect(0, 0, width, height);
	}
	
	private void generateBackground(String text, float size) {
		background = new Image[3];
		
		background[0] = ElementDefense.getTextImage(text, 236, size);
		background[1] = ElementDefense.getTextImage(text, 236, size);
		background[2] = ElementDefense.getTextImage(text, 236, size);
		
		float width = background[0].width();
		float height = background[0].height();
				
		((CanvasImage)background[1]).canvas().setFillColor(0x88111111);
		((CanvasImage)background[1]).canvas().fillRect(0, 0, width, height);
		
		((CanvasImage)background[2]).canvas().setFillColor(0x33111111);
		((CanvasImage)background[2]).canvas().fillRect(0, 0, width, height);
	}
	
	public abstract void pressEvent();
}


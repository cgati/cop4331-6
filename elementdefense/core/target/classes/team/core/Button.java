package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.Surface;

public class Button {
	private int index;
	private String text;
	private Image[] background;
	private Interval2D boundingBox;
	
	private int state;
	
	public final int NORMAL = 0;
	public final int PRESSED = 1;
	public final int HOVER = 2;
	
	public Button(Interval2D boundingBox, String text, Image[] background) {
		this.text = text;
		this.background = background;
		this.boundingBox = boundingBox;
	}
	
	public void paint(float alpha, Surface surface) {
		surface.drawImage(background[index], boundingBox.x0(), boundingBox.y0());
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
		this.state = state;
	}
}

package team.core;

import playn.core.Image;

public class Button {
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

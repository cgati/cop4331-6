package team.core;

import playn.core.Image;

public class SelectBox {
	private String[] options;
	private Image[] background;
	private Interval2D boundingBox;
	
	private int state;
	private int selected;
	
	public final int NORMAL = 0;
	public final int PRESSED = 1;
	public final int HOVER = 2;
	
	public SelectBox(Interval2D boundingBox, String[] options, Image[] background) {
		this.options = options;
		this.background = background;		
		this.boundingBox = boundingBox;
	}
	
	public void pressUp() {
		// TODO
	}
	
	public void pressDown() {
		// TODO
	}
	
	public String getSelected() {
		return options[selected];
	}
	
	public String[] getOptions() {
		return options;
	}
	
	public Image getBackground() {
		return background[state];
	}
	
	public Image getUpArrow() {
		return background[3];
	}
	
	public Image getDownArrow() {
		return background[4];
	}
}

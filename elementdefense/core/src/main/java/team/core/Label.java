package team.core;

import static playn.core.PlayN.graphics;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Surface;
import playn.core.TextFormat;
import playn.core.TextLayout;
import pythagoras.f.Point;

public class Label {
	private String text;
	private Point position;
	private CanvasImage image;
	private TextFormat textFormat;
	
	public Label(Point position) {
		this(position, "");
	}
	
	public Label(Point position, String text) {
		this(position, text, 36.0f);
	}
	
	public Label(Point position, String text, float size) {
		this.position = position;
		this.textFormat = new TextFormat(graphics().createFont("Courier New", Font.Style.PLAIN, size), 236, TextFormat.Alignment.LEFT);
		
		setText(text);		
	}
	
	public void setText(String text) {		
		TextLayout textLayout = graphics().layoutText(text, textFormat);
		image = graphics().createImage(236, 36);
		image.canvas().setFillColor(0xFFFFFFFF);
		image.canvas().fillText(textLayout, 0, 0);
		
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public void paint(float alpha, Surface surface) {
		surface.drawImage(image, position.x(), position.y());
	}
}

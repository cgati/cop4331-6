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
	
	private boolean hidden;
	
	public Label(Point position) {
		this(position, "");
	}
	
	public Label(Point position, String text) {
		this(position, text, 36.0f);
	}
	
	public Label(Point position, String text, float size) {
		this.position = position;
		this.textFormat = new TextFormat(graphics().createFont("Courier New", Font.Style.PLAIN, size), 236, TextFormat.Alignment.LEFT);
		this.image = graphics().createImage(236, 36);
		
		setText(text);		
	}
	
	public void setText(String text) {
		if(text == null || (this.text != null && this.text.equals(text))) {
			return;
		}
		
		TextLayout textLayout = graphics().layoutText(text, textFormat);
		image.canvas().clear();
		image.canvas().setFillColor(0xFFFFFFFF);
		image.canvas().fillText(textLayout, 0, 0);
		
		this.text = text;
	}
	
	public void hide() {
		hidden = true;
	}
	
	public void show() {
		hidden = false;
	}
	
	public String getText() {
		return text;
	}
	
	public void paint(float alpha, Surface surface) {
		if(hidden) {
			return;
		}
		
		surface.drawImage(image, position.x(), position.y());
	}
}

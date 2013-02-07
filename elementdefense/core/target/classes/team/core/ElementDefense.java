package team.core;

import static playn.core.PlayN.*;

import java.util.Date;

import playn.core.CanvasImage;
import playn.core.Font;
import playn.core.Game;
import playn.core.SurfaceLayer;
import playn.core.TextFormat;
import playn.core.TextLayout;

public class ElementDefense implements Game {
	SurfaceLayer layer;
	
	@Override
	public void init() {
		// create and add background image layer
		layer = graphics().createSurfaceLayer(graphics().width(), graphics().height());
		graphics().rootLayer().add(layer);
	}
	
	int i, MAX = 180;
	long[] d = new long[MAX];
	long sum = 0;
	Date last;

	@Override
	public void paint(float alpha) {
		// the background automatically paints itself, so no need to do anything here!
		
		double fps = 0.0;
		Date date = new Date();
		
		if(last == null) {
			last = date;
		} else {
			sum -= d[i];
			d[i] = date.getTime() - last.getTime();
			sum += d[i];
			
			if(++i >= MAX) {
				i = 0;
			}
			
			last = date;
			
			fps = 1000.0 / ((double)sum / MAX);
		}
		
		layer.surface().clear();
		
		String text = "FPS " + Math.round(fps); // String.format("FPS: %d", Math.round(fps));
		TextFormat format = new TextFormat(graphics().createFont("Monospaced", Font.Style.PLAIN, 32.0f), 600, TextFormat.Alignment.CENTER);
		TextLayout textLayout = graphics().layoutText(text, format);
		CanvasImage image = graphics().createImage(textLayout.width(), textLayout.height());
		image.canvas().setFillColor(0xFF00FFFF);
		image.canvas().fillText(textLayout, 0, 0);
		
		layer.surface().drawImage(image, graphics().width() - image.width(), 0);
	}

	@Override
	public void update(float delta) {
	}

	@Override
	public int updateRate() {
		return 25;
	}
}

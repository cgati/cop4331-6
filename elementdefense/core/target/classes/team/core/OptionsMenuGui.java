package team.core;

import static playn.core.PlayN.*;

import playn.core.Image;
import playn.core.Pointer.Event;
import playn.core.Pointer.Listener;
import playn.core.SurfaceLayer;
import pythagoras.f.Point;

import java.util.List;
import java.util.ArrayList;

public class OptionsMenuGui extends Gui {
	private Image optionsMenuImage;
	private Button okButton, cancelButton;
	private SelectBox resolutionBox, volumeBox;
	private List<Gui> alertList;
	
	public OptionsMenuGui(SurfaceLayer layer) {
		super(layer);
		
		alertList = new ArrayList<Gui>();
		
		layer.addListener(new Listener() {
			@Override
			public void onPointerStart(Event event) {
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				resolutionBox.pointerStart(p);
				volumeBox.pointerStart(p);
				okButton.pointerStart(p);
				cancelButton.pointerStart(p);
			}

			@Override
			public void onPointerEnd(Event event) {
				if(hidden || disabled) {
					return;
				}
				
				Point p = new Point(event.localX(), event.localY());
				
				resolutionBox.pointerEnd(p);
				volumeBox.pointerEnd(p);
				okButton.pointerEnd(p);
				cancelButton.pointerEnd(p);
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
		 
		optionsMenuImage = assets().getImage("images/Options.png");
		
		resolutionBox = new SelectBox(new Point(650, 300), new String[] { "640x480", "800x600", "960x480", "800x600", "1024x768", "1280x720", });
		volumeBox = new SelectBox(new Point(650, 477), new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" });
		
		resolutionBox.setSelected(5);
		
		okButton = new Button(new Point(480, 585), "Confirm") {
			@Override
			public void pressEvent() {
				String[] split = resolutionBox.getSelected().split("x");
				
				int width = Integer.parseInt(split[0]);
				int height = Integer.parseInt(split[1]);
				
				if(graphics().width() != width || graphics().height() != height) {
					ElementDefense.getInstance().resize(width, height);
				}
				
				float volume = Float.parseFloat(volumeBox.getSelected()) / 10.0f;
				
				if(volume != ElementDefense.getInstance().getVolume()) {
					ElementDefense.getInstance().setVolume(volume);
				}
				
				OptionsMenuGui.this.hide();
			}
		};
		
		cancelButton = new Button(new Point(680, 585), "Cancel") {
			@Override
			public void pressEvent() {				
				OptionsMenuGui.this.hide();
			}
		};
	}
	
	@Override
	public void paint(float alpha) {
		if(hidden) {
			return;
		}
		
		surface.drawImage(optionsMenuImage, 0, 0);
		
		resolutionBox.paint(alpha, surface);		
		volumeBox.paint(alpha, surface);
		okButton.paint(alpha, surface);
		cancelButton.paint(alpha, surface);
	}
	
	public void hide() {
		for(Gui g : alertList) {
			g.show();
		}
		
		hidden = true;
		
		alertList.clear();
	}
	
	public void alert(Gui gui) {
		if(gui != null) {
			alertList.add(gui);
		}
	}
	
	public void mouseMove(Point p) {
		if(hidden || disabled) {
			return;
		}
		
		resolutionBox.mouseMove(p);
		volumeBox.mouseMove(p);
		okButton.mouseMove(p);
		cancelButton.mouseMove(p);
	}
}

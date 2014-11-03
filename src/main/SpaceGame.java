package main;
import player.DesktopListener;
import player.ProtagonistTD;

import test.testColor;
import world.SimpleMap;
import world.SimpleWorld;

import control.DesktopControl;
import control.DeviceControl;
import desktopView.ColorImg;
import desktopView.DesktopCanvas;

public class SpaceGame {
	public static DeviceControl dc = DesktopControl.getInstance();
	
	public static void main(String[] args)
	{
		SimpleMap m = new SimpleMap(24, 20, 16, 16);
		DesktopCanvas canvas = new DesktopCanvas(384, 320, "W00t");
		ProtagonistTD p = new ProtagonistTD();
		DesktopListener dl = new DesktopListener(p);
		dc.setCanvas(canvas);
		m.addSimpleObject(p, 0, 0);
		m.addSimpleObject(new testColor(), 20, 20);
		canvas.addKeyListener(dl);
		SimpleWorld w = new SimpleWorld(m, dc);
		w.start(false);
		p.setImage(new ColorImg(0xFF000000, 16,16));
	}
}

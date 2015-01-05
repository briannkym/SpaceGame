package main;

import objects.BlackSolid;
import objects.NPC;
import player.DesktopListener;
import player.ProtagonistSS;
import player.ProtagonistTD;
import world.SimpleMap;
import world.SimpleMapIO;
import world.SimpleWorld;
import world.SimpleWorldFactory;

import control.DesktopControl;
import control.DeviceControl;
import desktopView.DesktopCanvas;

public class SpaceGame {
	public static DesktopCanvas canvas = new DesktopCanvas(320, 320, "W00t");
	public static int Orientation = 0;
	static int[][] map = { { 0, 0, 0, 0, 0, 0, 0, 1 }, { 0, 0, 0, 0, 0, 1, 0, 1 },
			{ 0, 0, 1, 0, 1, 0, 0, 0 }, { 1, 1, 1, 0, 1, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 0 }, { 1, 1, 1, 0, 1, 1, 1, 1 }, };

	public static DeviceControl dc = DesktopControl.getInstance();

	public static void main(String[] args) {
		ProtagonistSS p = new ProtagonistSS();
		DesktopListener dl = new DesktopListener(p);
		dc.setCanvas(canvas);
		
		
		SimpleWorldFactory swf = new SimpleWorldFactory();
		swf.register(new BlackSolid());
		SimpleMapIO mIO = new SimpleMapIO("test2.map", swf);
		//mIO.openMap(false);
		//mIO.writeMap(m);
		mIO.openMap(true);
		SimpleMap m = mIO.readMap();
		mIO.closeMap();
		m.addSimpleObject(p, 0, 0);
		

		canvas.addKeyListener(dl);
		SimpleWorld w = new SimpleWorld(m, dc);
		w.start(true);
	}
}

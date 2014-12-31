package main;

import objects.BlackSolid;
import objects.NPC;
import player.DesktopListener;
import player.ProtagonistSS;
import player.ProtagonistTD;
import world.SimpleMap;
import world.SimpleWorld;

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
		SimpleMap m = new SimpleMap(20, 20, 16, 16);
		ProtagonistSS p = new ProtagonistSS();
		DesktopListener dl = new DesktopListener(p);
		dc.setCanvas(canvas);
		m.addSimpleObject(p, 0, 0);
		for (int y = 0; y < map.length; y++) {
			for (int x = 0; x < map[y].length; x++) {
				if (map[y][x] == 1){
					m.addSimpleObject(new BlackSolid(), x*16, y*16);
				}
			}
		}

		canvas.addKeyListener(dl);
		SimpleWorld w = new SimpleWorld(m, dc);
		w.start(true);
	}
}

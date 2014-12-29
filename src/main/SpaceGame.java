package main;

import objects.BlackSolid;
import objects.NPC;
import player.DesktopListener;
import player.ProtagonistSS;
import player.ProtagonistTD;

import test.testColor;
import world.SimpleMap;
import world.SimpleObject;
import world.SimpleSolid;
import world.SimpleWorld;

import control.DesktopControl;
import control.DeviceControl;
import desktopView.ColorImg;
import desktopView.DesktopCanvas;

public class SpaceGame {
	static int[][] map = { { 0, 0, 0, 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 0, 0, 0, 0, 0, 0, 0, 0 }, { 1, 1, 1, 0, 1, 0, 0, 1 },
			{ 0, 0, 0, 0, 0, 0, 1, 0 }, { 0, 0, 0, 0, 0, 1, 0, 0 },
			{ 0, 0, 0, 1, 0, 0, 0, 0 }, { 1, 1, 1, 0, 1, 1, 1, 1 }, };

	public static DeviceControl dc = DesktopControl.getInstance();

	public static void main(String[] args) {
		SimpleMap m = new SimpleMap(24, 20, 16, 16);
		DesktopCanvas canvas = new DesktopCanvas(384, 320, "W00t");
		ProtagonistTD p = new ProtagonistTD();
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
		w.start(false);
	}
}

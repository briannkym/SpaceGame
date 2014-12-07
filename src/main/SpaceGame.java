package main;
import objects.NPC;
import player.DesktopListener;
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

		SimpleSolid Obstruction = new SimpleSolid(){

			@Override
			public void collision(SimpleObject s) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void update() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public int id() {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
		
		Obstruction.setImage(new ColorImg(0xFF000000, 16, 16));
		
		m.addSimpleObject(Obstruction, 40, 50);
		m.addSimpleObject(new NPC(), 100, 100);
		canvas.addKeyListener(dl);
		SimpleWorld w = new SimpleWorld(m, dc);
		w.start(true);
		p.setImage(new ColorImg(0xFF000000, 16,16));
	}
}

package levelBuilder;

import objects.*;
import world.SimpleMap;
import world.SimpleWorld;

public class Test{
	//cell size in pixels
	private static int cellWidth = 20;
	private static int cellHeight = 20;
	//map dimensions in cells
	private static int mapHeight = 30;
	private static int mapWidth = 40;
	//window dimensions in pixels
	private static int windowHeight = 900;
	private static int windowWidth = 800;
	
	private static SimpleMap m;
	private static SimpleWorld w;
	private static Cursor cursor = new Cursor();

	public static void main(String[] args){
		m = new SimpleMap(mapWidth, mapHeight, cellWidth, cellHeight);
		m.addSimpleObject(cursor, 20, 20);
		w = new SimpleWorld(m, windowWidth, windowHeight, "Test");
		w.addKeyListener(cursor);
		w.setCameraStalk(cursor);
		w.start(false);
		
		//visual markers at edges of map to see where the cursor is in comparison to something
		m.addSimpleObject(new Solid1(), 0, 0);
		m.addSimpleObject(new MLSolid(), 0, mapHeight * cellHeight-cellHeight);
		m.addSimpleObject(new Solid1(), mapWidth * cellWidth-cellWidth, mapHeight * cellHeight-cellHeight);
		m.addSimpleObject(new MLSolid(), mapWidth * cellWidth-cellWidth, 0);
	}
}

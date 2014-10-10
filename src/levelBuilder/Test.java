package levelBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import objects.*;
import world.SimpleMap;
import world.SimpleMapIO;
import world.SimpleWorld;
import world.SimpleWorldFactory;

public class Test{
	//cell size in pixels
	private static int cellWidth = 20;
	private static int cellHeight = 20;
	//map dimensions in cells
	private static int mapHeight = 30;
	private static int mapWidth = 40;
	//window dimensions in pixels
	private static int windowHeight = 600;
	private static int windowWidth = 800;
	
	private static SimpleMap m;
	private static SimpleWorld w;
	private static Cursor cursor = new Cursor();

	public static void main(String[] args){		
		m = new SimpleMap(mapWidth, mapHeight, cellWidth, cellHeight);
//		m.addSimpleObject(cursor, 20, 20);
		w = new SimpleWorld(m, windowWidth, windowHeight, "Test");
//		w.addKeyListener(cursor);
//		w.setCameraStalk(cursor);
		w.start(false);
		
		//visual markers at edges of map to see where the cursor is in comparison to something
		m.addSimpleObject(new BlackSolid(), 0, 0);
		m.addSimpleObject(new MLearnerSolid(), 0, mapHeight * cellHeight-cellHeight);
		m.addSimpleObject(new BlackSolid(), mapWidth * cellWidth-cellWidth, mapHeight * cellHeight-cellHeight);
		m.addSimpleObject(new MLearnerSolid(), mapWidth * cellWidth-cellWidth, 0);
	}
}

package levelBuilder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import objects.*;

import world.SimpleMap;
import world.SimpleWorld;

//TODO why were block size and cell size different in the test program, shouldn't they be the same?
public class LevelBuilder {
	//position of cursor in pixels
	static int CursorY = 20;
	static int CursorX = 20;
	//cell size in pixels
	static int cellWidth = 20;
	static int cellHeight = 20;
	//block size in pixels
	static int blockWidth = 20;
	static int blockHeight = 20;
	//map dimensions in cells
	static int mapCellHeight = 30;
	static int mapCellWidth = 40;
	//map dimensions in pixels
	static int mapHeight = mapCellHeight * cellHeight;
	static int mapWidth = mapCellWidth * cellWidth;
	//window dimensions in pixels
	static int windowHeight = 400;
	static int windowWidth = 600;
	//TODO list of available objects
		
	//has init been run already
	boolean initRun = false;//TODO save this in the level file somewhere (are key-value pairs available?)
	
			
	static SimpleMap m = new SimpleMap(mapCellWidth, mapCellHeight, cellWidth, cellHeight);//TODO map dimensions should be user selectable at init
	static Cursor cam = new Cursor();
	
	public static void main(String[] args){
		m.addSimpleObject(cam, CursorX, CursorY);
		SimpleWorld w = new SimpleWorld(m, windowWidth, windowHeight, "Level Builder"); //TODO might need window dimensions to be created at time of level creation in the future
		
		//I don't know what is going on here, it seems to do what I want it to though, which is making the game listen for key presses
		LevelBuilder LB = new LevelBuilder();
		w.addKeyListener(LB.new keyEventHandler());
		
		w.setCameraStalk(cam);
		w.start(false);
		
		//visual markers to see where the cursor is in comparison to something TODO this is test code
		m.addSimpleObject(new Cursor(), 0, 0);
		m.addSimpleObject(new Solid1(), 0, mapHeight-blockHeight);
		m.addSimpleObject(new Solid1(), mapWidth-blockWidth, mapHeight-blockHeight);
		m.addSimpleObject(new Solid1(), mapWidth-blockWidth, 0);
	}
	
	//loads map files
	public void load(){
		//init will have been run if we are loading a previously made map 
		initRun = true;
		//TODO should make sure currently open map is closed first, and maybe ask to save
	}
	
	//saves map files
	public void save(){
		
	}
	
	//initiates new map
	public void init(){
		//has init already been run for this map?
		if (initRun == false){
			initRun = true;
			//TODO routine for selecting map dimensions, and other stuff
			
			
	
			
		}
		else{
			//TODO routine for changing properties of existing map
		}
	}
	
	//method for moving cursor, called by keyEventHandler when an arrow key is pressed
	//moves cursor by deleting old one and creating a new one at a new position
	//XDirection and YDirection are the directions of movement for cursor
	private void moveCursor(int XDirection, int YDirection){
		m.removeSimpleObject(cam);
		//new position called for by key input
		int NewY = CursorY - blockHeight * YDirection;
		int NewX = CursorX + blockWidth * XDirection;
		//checks if new position is within bounds
		if (NewY < 0 || NewX < 0 || NewY > mapHeight - blockHeight || NewX > mapWidth - blockWidth){
			//out of bounds, no more moving in that direction
		}
		else {
			//in bounds, can move in that direction
			CursorY = NewY;
			CursorX = NewX;
		}
		m.addSimpleObject(cam, CursorX, CursorY);
	}
	
	//places object on map
	public void place(){
		m.addSimpleObject(new Solid1(), CursorX, CursorY);//TODO need to be able to add other types of things
	}
	
	//removes object from map
	public void remove(){
		m.removeSimpleSolid(CursorX / cellWidth, CursorY / cellHeight);//TODO still need to be able to remove simpleObjects
	}
	
	//TODO selects object type to place
	public void selectObjectType(){
		
	}
	
	//this class gets called whenever a key is pressed
	//it then redirects each key to the relevant method or does other relevant stuff
	private class keyEventHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();

			//move cursor in a direction
			if (key == KeyEvent.VK_LEFT) {
				moveCursor(-1, 0);
			}
			if (key == KeyEvent.VK_UP) {
				moveCursor(0, 1);
			}
			if (key == KeyEvent.VK_RIGHT) {
				moveCursor(1, 0);
			}
			if (key == KeyEvent.VK_DOWN) {
				moveCursor(0, -1);
			}
			//place currently selected object at cursor position
			if (key == KeyEvent.VK_SPACE) {
				place();
			}
			//load map dialogue
			if (key == KeyEvent.VK_L) {
				load();
			}
			//save map dialogue
			if (key == KeyEvent.VK_S) {
				save();
			}
			//remove object at cursor position
			if (key == KeyEvent.VK_R) {
				remove();
			}
			//changes selected object for placement
			if (key == KeyEvent.VK_O) {
				selectObjectType();
			}
			//initiates new map
			if (key == KeyEvent.VK_I) {
				init();
			}
		}
	}
}

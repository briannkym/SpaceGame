package levelBuilder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;

import objects.*;
import world.SimpleMap;
import world.SimpleWorld;

//TODO why were block size and cell size different in the test program, shouldn't they be the same?
public class LevelBuilder {
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
	static int windowHeight = 600;
	static int windowWidth = 600;
	//TODO list of available objects
			
	static SimpleMap m = new SimpleMap(mapCellWidth, mapCellHeight, cellWidth, cellHeight);//TODO map dimensions should be user selectable at init
	static Cursor cam = new Cursor();
	
	public static void main(String[] args){
		m.addSimpleObject(cam, 20, 20);
		SimpleWorld w = new SimpleWorld(m, windowWidth, windowHeight, "Level Builder"); //TODO might need window dimensions to be created at time of level creation in the future
		
		//I don't know what is going on here, it seems to do what I want it to though, which is making the game listen for key presses
		LevelBuilder LB = new LevelBuilder();
		w.addKeyListener(LB.new keyEventHandler());
		w.addKeyListener(cam);
		w.setCameraStalk(cam);
		w.start(false);
		
		//visual markers to see where the cursor is in comparison to something TODO this is test code
		m.addSimpleObject(new Solid1(), 0, 0);
		m.addSimpleObject(new Solid1(), 0, mapHeight-cellHeight);
		m.addSimpleObject(new Solid1(), mapWidth-cellWidth, mapHeight-cellHeight);
		m.addSimpleObject(new Solid1(), mapWidth-cellWidth, 0);
		
		//Splash screen
		//list key bindings
	}
	
	//loads map file
	public void load(){	
		//TODO make sure currently open map is closed first, and maybe ask to save before opening a new one
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".sgs");
		    }
		};
		
		File savesFolder = new File("saves");
		File[] saves = savesFolder.listFiles(filter);
		if (savesFolder.exists()){
			if (saves.length > 0){
				String[] saveNames = savesFolder.list(filter);
				for (int i = 0; i < saves.length; i++){
					System.out.println(i + " " + saveNames[i]); //TODO list saves on screen
				}
				//TODO have user select file they want
				//TODO load file
			}
			else {
				System.out.println("No existing save files"); //TODO change to onscreen message, find out how to do that
			}
		}
		else {
			savesFolder.mkdir();
			System.out.println("No existing save files"); //TODO change to onscreen message, find out how to do that
		}
	}
	
	//saves map file
	public void save(){
		FilenameFilter filter = new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".sgs");
		    }
		};
		
		File savesFolder = new File("saves");
		if (!savesFolder.exists())
			savesFolder.mkdir();
		File[] saves = savesFolder.listFiles(filter);
		if (saves.length > 0){
			String[] saveNames = savesFolder.list(filter);
			//lists existing files
			for (int i = 0; i < saves.length; i++){
				System.out.println(i + " " + saveNames[i]); //TODO list saves on screen
			}	
		}
		//TODO have user select file they want to save over, or a new file
//			SimpleMapIO IOObj = new SimpleMapIO(path, );
//			IOObj.writeMap(m);
	}
	
	//initiates new map
	public void init(){
		//TODO routine for selecting map dimensions, and other stuff
	}
	
	//places object on map
	public void place(){
		m.addSimpleObject(new Solid1(), cam.getX(), cam.getY());//TODO need to be able to add other types of things
	}
	
	//removes object from map
	public void remove(){
		m.removeSimpleSolid(cam.getX() / cellWidth, cam.getY() / cellHeight);//TODO still need to be able to remove simpleObjects
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
			if (key == KeyEvent.VK_N) {
				init();
			}
		}
	}
}

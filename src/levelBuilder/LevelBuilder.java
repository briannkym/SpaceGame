package levelBuilder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.*;

import objects.*;
import world.SimpleMap;
import world.SimpleWorld;

public class LevelBuilder {
	//cell size in pixels
	private static int cellWidth = 20;
	private static int cellHeight = 20;
	//map dimensions in cells
	private static int mapCellHeight;
	private static int mapCellWidth;
	//map dimensions in pixels
	private static int mapHeight;
	private static int mapWidth;
	//window dimensions in pixels
	private static int windowHeight;
	private static int windowWidth;
	
	private static String levelName;
	private static SimpleMap m;
	private static SimpleWorld w;
	private static Cursor cam = new Cursor();
	private static Splash splash = new Splash();
	
	public static void main(String[] args){
		loadResources();
		splashWindow();
	}
	
	private static void loadResources(){
		//TODO load images and sounds
	}
		
	private static void splashWindow(){
		//TODO need background image
		int result = JOptionPane.showOptionDialog(null, null, "Choose!", JOptionPane.DEFAULT_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, new Object[]{"New Map", "Load Existing Map"}, null);
		if (result == 0) {
			newMap();
		}
		else if (result == 1) {
			loadMap();
		}
		else {
			System.exit(0);
		}
	}
	
	private static void loadMap(){	
		FilenameFilter saveFilter = new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".sgs");
		    }
		};
		
		File savesFolder = new File("saves");
		File[] saves = savesFolder.listFiles(saveFilter);
		if (savesFolder.exists()){
			if (saves.length > 0){
//				String[] saveNames = savesFolder.list(saveFilter);
				String result = (String) JOptionPane.showInputDialog(null, "Which save file would you like to load?",
						"Choose!", JOptionPane.QUESTION_MESSAGE, null, saves, saves[0]);
				if (result == null) {
					splashWindow();
				}
				else {
					File saveFile = new File(result);
					//TODO load file
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No existing save files");
				splashWindow();
			}
		}
		else {
			savesFolder.mkdir();
			JOptionPane.showMessageDialog(null, "No existing save files");
			splashWindow();
		}
		
	}
	
	private void saveMap(){
		FilenameFilter saveFilter = new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".sgs");
		    }
		};
		
		File savesFolder = new File("saves");
		if (!savesFolder.exists())
			savesFolder.mkdir();
		File[] saves = savesFolder.listFiles(saveFilter);
		if (saves.length > 0){
			String[] saveNames = savesFolder.list(saveFilter);
			//lists existing files
			for (int i = 0; i < saves.length; i++){
				System.out.println(i + " " + saveNames[i]); //TODO list saves on screen
			}	
		}
		//TODO have user select file they want to save over, or a new file
//		SimpleMapIO IOObj = new SimpleMapIO(path, );
//		IOObj.writeMap(m);
		
	}
	
	private static void newMap(){
		JPanel panel = new JPanel();
		JTextField ww = new JTextField("800", 5);
		JTextField wh = new JTextField("600", 5);
		JTextField mcw = new JTextField("40", 5);
		JTextField mch = new JTextField("30", 5);
		JTextField ln = new JTextField("blah", 5);
		
		panel.add(new JLabel("Window width in pixels"));
		panel.add(ww);
		panel.add(new JLabel("Window height in pixels"));
		panel.add(wh);
		panel.add(new JLabel("Map width in cells"));
		panel.add(mcw);
		panel.add(new JLabel("Map height in cells"));
		panel.add(mch);
		panel.add(new JLabel("Level Name"));
		panel.add(ln);
		panel.add(Box.createHorizontalStrut(15));
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Map Initialization", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			//TODO could add nice stuff to make it impossible for people to enter non-integers
			mapCellHeight = Integer.parseInt(mch.getText());
			mapCellWidth = Integer.parseInt(mcw.getText());
			windowHeight = Integer.parseInt(wh.getText());
			windowWidth = Integer.parseInt(ww.getText());
			levelName = ln.getText();
			
			mapWidth = mapCellWidth * cellWidth;
			mapHeight = mapCellHeight * cellHeight;
			
			m = new SimpleMap(mapCellWidth, mapCellHeight, cellWidth, cellHeight);
			m.addSimpleObject(cam, 20, 20);
			w = new SimpleWorld(m, windowWidth, windowHeight, "Space Game Level Builder");
			//I don't know what is going on in the next two lines, it seems to do what I want it to though, which is making the game listen for key presses
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
		}
		else {
			splashWindow();
		}
	}
	
	private void placeObject(){
		m.addSimpleObject(new Solid1(), cam.getX(), cam.getY());//TODO need to be able to add other types of things
	}
	
	private void removeObject(){
		m.removeSimpleSolid(cam.getX() / cellWidth, cam.getY() / cellHeight);//TODO still need to be able to remove simpleObjects
	}
	
	//TODO selects object type to place
	private void selectObjectType(){
		
	}
	
	//this class gets called whenever a key is pressed
	//it then redirects each key to the relevant method
	private class keyEventHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();
			
			if (key == KeyEvent.VK_SPACE) {
				placeObject();
			}
			if (key == KeyEvent.VK_R) {
				removeObject();
			}
			if (key == KeyEvent.VK_O) {
				selectObjectType();
			}
			if (key == KeyEvent.VK_S) {
				saveMap();
			}
		}
	}
}

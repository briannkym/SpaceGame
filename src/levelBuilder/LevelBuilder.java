package levelBuilder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
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
	private static int cellWidth;
	private static int cellHeight;
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
	private static File saveFile = new File("New File");
	
	public static void main(String[] args){
		loadResources();
		splashWindow();
	}
	
	private static void loadResources(){
		//TODO load images and sounds
	}
		
	private static void splashWindow(){
		//TODO a background image would be cool
		int result = JOptionPane.showOptionDialog(null, null, "Choose!", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[]{"New Map", "Load Existing Map"}, null);
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
				String[] saveNames = savesFolder.list(saveFilter);
				String result = (String) JOptionPane.showInputDialog(null, "Which save file would you like to load?",
						"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveNames[0]);
				if (result == null) {
					splashWindow();
				}
				else {
					saveFile = new File(result);
					//TODO load map
				}
			}
			else {
				JOptionPane.showMessageDialog(null, "No existing save files", null, JOptionPane.PLAIN_MESSAGE);
				splashWindow();
			}
		}
		else {
			savesFolder.mkdir();
			JOptionPane.showMessageDialog(null, "No existing save files", null, JOptionPane.PLAIN_MESSAGE);
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
		String[] saveNames = new String[savesFolder.list(saveFilter).length+1];
		for (int i = 0; i < saveNames.length-1; i++) {
			saveNames[i] = savesFolder.list(saveFilter)[i];
		}
		saveNames[saveNames.length-1] = "New File";

		if (saveNames.length > 0){		
			String result = (String) JOptionPane.showInputDialog(w, "Choose file to overwrite, or a new file",
					"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveFile.getName());
			if (result == "New File") {
				String fileName = JOptionPane.showInputDialog(w, "New file name", "Write!", JOptionPane.PLAIN_MESSAGE);
				fileName = fileName + ".sgs";
				saveFile = new File(fileName);
			}
			if (result == null) {
			}
			else {
				saveFile = new File(result);
				//TODO save map
			}
		}	
	}
	
	private static void newMap(){
		//TODO add some stuff for user selection of cell size
		JPanel panel = new JPanel();
		JPanel labels = new JPanel(new GridLayout(0,1,0,12));
		JPanel input = new JPanel(new GridLayout(0,1,0,0));
		
		JTextField ww = new JTextField("800", 3);
		JTextField wh = new JTextField("600", 3);
		JTextField mcw = new JTextField("40", 3);
		JTextField mch = new JTextField("30", 3);
		JTextField cw = new JTextField("20", 3);
		JTextField ch = new JTextField("20", 3);
		JTextField ln = new JTextField("blah", 5);
		
		labels.add(new JLabel("Window width in pixels", SwingConstants.RIGHT));
		input.add(ww, BorderLayout.NORTH);
		labels.add(new JLabel("Window height in pixels", SwingConstants.RIGHT));
		input.add(wh, BorderLayout.NORTH);
		labels.add(new JLabel("Map width in cells", SwingConstants.RIGHT));
		input.add(mcw, BorderLayout.NORTH);
		labels.add(new JLabel("Map height in cells", SwingConstants.RIGHT));
		input.add(mch, BorderLayout.NORTH);
		labels.add(new JLabel("Cell width in pixels", SwingConstants.RIGHT));
		input.add(cw, BorderLayout.SOUTH);
		labels.add(new JLabel("Cell height in pixels", SwingConstants.RIGHT));
		input.add(ch, BorderLayout.SOUTH);
		labels.add(new JLabel("Level name", SwingConstants.RIGHT));
		input.add(ln, BorderLayout.SOUTH);
//		panel.add(Box.createHorizontalStrut(15));
		
		panel.add(labels, BorderLayout.WEST);
		panel.add(input, BorderLayout.EAST);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Map Initialization", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			//TODO could add nice stuff to make it impossible for people to enter non-integers
			mapCellHeight = Integer.parseInt(mch.getText());
			mapCellWidth = Integer.parseInt(mcw.getText());
			windowHeight = Integer.parseInt(wh.getText());
			windowWidth = Integer.parseInt(ww.getText());
			cellWidth = Integer.parseInt(cw.getText());
			cellHeight = Integer.parseInt(ch.getText());
			levelName = ln.getText();
			
			mapWidth = mapCellWidth * cellWidth;
			mapHeight = mapCellHeight * cellHeight;
			
			m = new SimpleMap(mapCellWidth, mapCellHeight, cellWidth, cellHeight);
			m.addSimpleObject(cam, 20, 20);
			w = new SimpleWorld(m, windowWidth, windowHeight, "Space Game Level Builder: " + levelName);
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

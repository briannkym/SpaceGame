package levelBuilder;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

import javax.swing.*;

import objects.*;
import world.SimpleMap;
import world.SimpleMapIO;
import world.SimpleObject;
import world.SimpleWorld;
import world.SimpleWorldFactory;

//TODO What does openMap() do?
//TODO why aren't my maps saving even though writeMap() doesn't return an error.
//TODO what function does SimpleWorldFactory serve?
//TODO fix saving and loading of cursor
//TODO after loading a map, how do I fix camera on cursor?
//TODO why doesn't writeMap() save solids?
public class LevelBuilder{
	//cell size in pixels
	private static int cellWidth;
	private static int cellHeight;
	//map dimensions in cells
	private static int mapHeight;
	private static int mapWidth;
	//window dimensions in pixels
	private static int windowHeight;
	private static int windowWidth;
	
	private static int objectType = 0;
	private static String levelName;
	private static SimpleMap m;
	private static SimpleWorld w;
	private static Cursor cursor = new Cursor(); 
	private static String saveName = "New File";
	private static Constructor<?>[] constructors;
	
	private static FilenameFilter saveFilter = new FilenameFilter() {
	    public boolean accept(File directory, String fileName) {
	        return fileName.endsWith(".map");
	    }
	};
	
	private static FilenameFilter objectFilter = new FilenameFilter() {
	    public boolean accept(File directory, String fileName) {
	        return fileName.endsWith(".java");
	    }
	};
	
	public static void main(String[] args){
		loadResources();
		splashWindow();
	}
	
	private static void loadResources(){		
		//TODO load images and sounds
		
		//load objects in objects package and put objects in array
		File objectsFolder = new File("src/objects");
		File[] objectFiles = objectsFolder.listFiles(objectFilter);
		constructors = new Constructor<?>[objectFiles.length];
		String fileName;
		for (int i = 0; i < objectFiles.length; i++) {
			Class<?> c = null;
			try {
				fileName = objectFiles[i].getName();
				c = Class.forName("objects." + fileName.substring(0, fileName.lastIndexOf('.')));
			} catch (ClassNotFoundException e2) {
				e2.printStackTrace();
			}
			
			constructors[i] = null;
			try {
				constructors[i] = c.getConstructor();
			} catch (NoSuchMethodException e1) {
				e1.printStackTrace();
			} catch (SecurityException e1) {
				e1.printStackTrace();
			}
		}
	}
		
	private static void splashWindow(){
		//TODO a background image would be cool
		int result = JOptionPane.showOptionDialog(null, null, "Choose!", JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, new Object[]{"New Map", "Load Existing Map"}, null);
		if (result == 0) {
			newMap();
		}
		else if (result == 1) {
			load();
		}
		else {
			System.exit(0);
		}
	}
	
	//loads extra-map configuration options like window size
	private static void readConfigFile(String configPath){
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(configPath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		scanner.next();
		windowWidth = scanner.nextInt();
		scanner.next();
		windowHeight = scanner.nextInt();
		scanner.next();
		levelName = scanner.next();
		scanner.close();
	}
	
	//saves extra-map configuration options like window size
	private void writeConfigFile(String configPath) {
		File configFile = new File(configPath);
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(configFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.println("windowWidth " + windowWidth);
		writer.println("windowHeight " + windowHeight);
		writer.println("levelName " + levelName);
		writer.close();
	}
	
	private static void load(){	
		File savesFolder = new File("saves");
		File[] saves = savesFolder.listFiles(saveFilter);
		if (savesFolder.exists()){
			if (saves.length > 0){
				String[] saveNames = savesFolder.list(saveFilter);
				String saveName = (String) JOptionPane.showInputDialog(null, "Which save file would you like to load?",
						"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveNames[0]);
				if (saveName == null) {
					splashWindow();
				}
				else {					
					String mapPath = "saves/" + saveName;
					//TODO fix load map
					SimpleWorldFactory swf = new SimpleWorldFactory();
					SimpleMapIO IOObj = new SimpleMapIO(mapPath, swf);
					IOObj.openMap();
					m = IOObj.readMap();
					IOObj.closeMap();
					if (m != null) {
						//loads extra-map configuration options like window size
						readConfigFile(mapPath.substring(0, mapPath.lastIndexOf('.') + 1) + "cfg");
					}
					else {
						JOptionPane.showMessageDialog(null, "Error, map not loaded :-(", null, JOptionPane.PLAIN_MESSAGE);
					}
					startWorldAndMap();
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
	
	private void save(){
		File savesFolder = new File("saves");
		if (!savesFolder.exists())
			savesFolder.mkdir();
		String[] saveNames = new String[savesFolder.list(saveFilter).length+1];
		for (int i = 0; i < saveNames.length-1; i++) {
			saveNames[i] = savesFolder.list(saveFilter)[i];
		}
		saveNames[saveNames.length-1] = "New File";

		if (saveNames.length > 0){		
			String mapPath;
			String result = (String) JOptionPane.showInputDialog(w, "Choose file to overwrite, or a new file",
					"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveName);
			if (result == "New File") {
				mapPath = JOptionPane.showInputDialog(w, "New file name", "Write!", JOptionPane.PLAIN_MESSAGE);
				mapPath = "saves/" + mapPath + ".map";
			}
			else if (result == null) {
				return;
			}
			else {
				mapPath = "saves/" + result;
			}
			
			//TODO fix saving maps
			SimpleWorldFactory swf = new SimpleWorldFactory();
			SimpleMapIO IOObj = new SimpleMapIO(mapPath, swf);
			IOObj.openMap();
			boolean mapSaved = IOObj.writeMap(m);
			IOObj.closeMap();
			if (mapSaved) {
				JOptionPane.showMessageDialog(null, "Map Saved!", null, JOptionPane.PLAIN_MESSAGE);
				//save extra-map configuration like window size
				writeConfigFile(mapPath.substring(0, mapPath.lastIndexOf('.') + 1) + "cfg");
				
			}
			else {
				JOptionPane.showMessageDialog(null, "Error, map not saved :-(", null, JOptionPane.PLAIN_MESSAGE);
			}
		}	
	}
	
	private static void newMap(){
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
		input.add(ww);
		labels.add(new JLabel("Window height in pixels", SwingConstants.RIGHT));
		input.add(wh);
		labels.add(new JLabel("Map width in cells", SwingConstants.RIGHT));
		input.add(mcw);
		labels.add(new JLabel("Map height in cells", SwingConstants.RIGHT));
		input.add(mch);
		labels.add(new JLabel("Cell width in pixels", SwingConstants.RIGHT));
		input.add(cw);
		labels.add(new JLabel("Cell height in pixels", SwingConstants.RIGHT));
		input.add(ch);
		labels.add(new JLabel("Level name", SwingConstants.RIGHT));
		input.add(ln);
		
		panel.add(labels, BorderLayout.WEST);
		panel.add(input, BorderLayout.EAST);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Map Initialization", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			//TODO could add nice stuff to make it impossible for people to enter non-integers
			mapHeight = Integer.parseInt(mch.getText());
			mapWidth = Integer.parseInt(mcw.getText());
			windowHeight = Integer.parseInt(wh.getText());
			windowWidth = Integer.parseInt(ww.getText());
			cellWidth = Integer.parseInt(cw.getText());
			cellHeight = Integer.parseInt(ch.getText());
			levelName = ln.getText();
			
			m = new SimpleMap(mapWidth, mapHeight, cellWidth, cellHeight);
			startWorldAndMap();
			
			//visual markers to see where the cursor is in comparison to something
			//TODO this is test code
			m.addSimpleObject(new Solid1(), 0, 0);
			m.addSimpleObject(new Solid1(), 0, mapHeight * cellHeight-cellHeight);
			m.addSimpleObject(new Solid1(), mapWidth * cellWidth-cellWidth, mapHeight * cellHeight-cellHeight);
			m.addSimpleObject(new Solid1(), mapWidth * cellWidth-cellWidth, 0);
		}
		else {
			splashWindow();
		}
	}
	
	//instantiates map, world, cursor, keylisteners, camera, and starts game loop
	private static void startWorldAndMap(){
		m.addSimpleObject(cursor, cellWidth, cellHeight);
		w = new SimpleWorld(m, windowWidth, windowHeight, "Space Game Level Builder: " + levelName);
		//I don't know what is going on in the next two lines, it seems to do what I want it to though, which is making the game listen for key presses
		LevelBuilder LB = new LevelBuilder();
		w.addKeyListener(LB.new keyEventHandler());
		w.addKeyListener(cursor);
		w.setCameraStalk(cursor);
		w.start(false);
		selectObjectTypeWindow();
	}
	
	private void placeObject(){
		try {
			m.addSimpleObject((SimpleObject) constructors[objectType].newInstance(), cursor.getX(), cursor.getY());
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}
	
	//TODO still need to be able to remove simpleObjects
	private void removeObject(){
		m.removeSimpleSolid(cursor.getX() / cellWidth, cursor.getY() / cellHeight);
	}
	
	//selects object type to place
	private static void selectObjectTypeWindow(){
		JPanel panel = new JPanel();
		JPanel labels = new JPanel(new GridLayout(0,1,0,13));
		JPanel input = new JPanel(new GridLayout(0,1,0,0));
		JButton[] button = new JButton[constructors.length];
		
		String name;
		LevelBuilder LB = new LevelBuilder();
		for (int i = 0; i < constructors.length; i++) {
			name = constructors[i].getName();
			labels.add(new JLabel(name.substring(name.lastIndexOf('.') + 1, name.length()), SwingConstants.LEFT));
			button[i] = new JButton("<--");
			button[i].setActionCommand(Integer.toString(i));
			button[i].addActionListener(LB.new actionHandler());
			input.add(button[i]);
		}
		
		panel.add(labels, BorderLayout.WEST);
		panel.add(input, BorderLayout.EAST);
		
		JFrame frame = new JFrame("Available Objects");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocation(windowWidth, 0);
		frame.setVisible(true);
	}
	
	//TODO maybe put these in cursor.java?
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
			if (key == KeyEvent.VK_S) {
				save();
			}
		}
	}
	
	private class actionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			objectType = Integer.parseInt(e.getActionCommand());
		    System.out.println(objectType);
		}
		
	}
}

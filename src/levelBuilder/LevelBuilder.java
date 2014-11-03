//Copyright (c) 2014 Mark Groeneveld

package levelBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

import desktopView.DesktopImgUpload;

import objects.*;
import world.SimpleMap;
import world.SimpleMapIO;
import world.SimpleObject;
import world.SimpleSolid;
import world.SimpleWorld;
import world.SimpleWorldFactory;

/**
 * Builds game levels based on SSEngine
 * 
 * @author Mark Groeneveld
 * @author Brian Nakayama
 * @version 1.01
 */

//TODO expand object selection window to deal with many objects
//TODO make option so updates don't happen, so moving objects stay where you put them
public class LevelBuilder{
	private static int cellWidth; //pixels
	private static int cellHeight;
	private static int mapHeight; //cells
	private static int mapWidth;
	private static int windowHeight; //pixels
	private static int windowWidth;
	private static int verticalWindowPlacement = 0;
	private static String levelName;
	private static String backgroundFileName;
	private static String saveName = "New File";
	private static int objectType = 1;
	private static SimpleMap m;
	private static SimpleWorld w;
	private static Cursor cursor; 
	private static Constructor<SimpleObject>[] constructors;
	private static SimpleWorldFactory swf = new SimpleWorldFactory();
	private static int[] id;
	private static JTextField argField;
	private static SimpleObject objectToRemove = null;
	
	public static void main(String[] args){
		loadResources();
		splashWindow();
	}
	
	@SuppressWarnings("unchecked")
	private static void loadResources(){
		FilenameFilter objectFilter = new FilenameFilter() {
		    public boolean accept(File directory, String fileName) {
		        return fileName.endsWith(".java");
		    }
		};
		
		File objectsFolder = new File("src/objects");
		File[] objectFiles = objectsFolder.listFiles(objectFilter);
		constructors = new Constructor[objectFiles.length];
		
		//sorts objects alphabetically but puts cursor first
		File[] temp = objectsFolder.listFiles(objectFilter);
		Arrays.sort(objectFiles);
		boolean cursorFound = false;
		for (int i = 0; i < objectFiles.length; i++) {
			if (objectFiles[i].getName().equals("Cursor.java")){
				temp[0] = objectFiles[i];
				cursorFound = true;
			}
			else if (cursorFound == false) {
				temp[i+1] = objectFiles[i];
			}
			else if (cursorFound == true) {
				temp[i] = objectFiles[i];
			}
		}
		objectFiles = temp;
		
		//get constructors for objects in objects folder
		String fileName;
		for (int i = 0; i < objectFiles.length; i++) {
			Class c = null;
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
		
		//register objects in SimpleWorldFactory
		for (int i = 0; i < constructors.length; i++){
			try {
				swf.register((SimpleObject) constructors[i].newInstance());
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
		
		//gets id for each object and puts in id array
		id = new int[constructors.length];
		for (int i  = 0; i < constructors.length; i++) {
			try {
				id[i] = ((SimpleObject) constructors[i].newInstance()).id();
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
			
			//checks for duplicate id's
			int index = Arrays.binarySearch(id, 0, i, id[i]);
			if (index >= 0) {
				JOptionPane.showMessageDialog(null, "Duplicate object ID's between " + objectFiles[i].getName() + " and "
						+ objectFiles[index].getName() + ". Change one to continue.", null, JOptionPane.PLAIN_MESSAGE);
				System.exit(1);
			}
		}
	}

	//loads extra-map configuration options like window size
	private static void readConfig(String configPath){
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
		mapWidth = scanner.nextInt();
		scanner.next();
		mapHeight = scanner.nextInt();
		scanner.next();
		cellWidth = scanner.nextInt();
		scanner.next();
		cellHeight = scanner.nextInt();
		scanner.next();
		levelName = scanner.nextLine();
		scanner.next();
		backgroundFileName = scanner.next();
		scanner.close();
	}
	
	//saves extra-map configuration options like window size
	private static void writeConfig(String configPath) {
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
		writer.println("mapWidth " + mapWidth);
		writer.println("mapHeight " + mapHeight);
		writer.println("cellWidth " + cellWidth);
		writer.println("cellHeight " + cellHeight);
		writer.println("levelName " + levelName);
		writer.println("backgroundFileName " + backgroundFileName);
		writer.close();
	}
	
	private static void loadLevel(){	
		//load dialog and loading mechanism
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
					SimpleMapIO IOObj = new SimpleMapIO(mapPath, swf);
					IOObj.openMap(true);
					m = IOObj.readMap();
					IOObj.closeMap();
					if (m != null) {
						//loads extra-map configuration options like window size
						readConfig(mapPath.substring(0, mapPath.lastIndexOf('.') + 1) + "cfg");
						startMapAndWorld();
					}
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
	
	public static void saveLevel(){
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
			saveName = (String) JOptionPane.showInputDialog(w, "Choose file to overwrite, or a new file",
					"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveName);
			if (saveName == "New File") {
				mapPath = JOptionPane.showInputDialog(w, "New file name", "Write!", JOptionPane.PLAIN_MESSAGE);
				mapPath = "saves/" + mapPath + ".map";
			}
			else if (saveName == null) {
				return;
			}
			else {
				mapPath = "saves/" + saveName;
			}
			
			//temporarily removes cursor so it isn't in the save file
			m.removeSimpleObject(cursor);
			SimpleMapIO IOObj = new SimpleMapIO(mapPath, swf);
			IOObj.openMap(false);
			if (IOObj.writeMap(m)) {
				JOptionPane.showMessageDialog(null, "Map Saved!", null, JOptionPane.PLAIN_MESSAGE);
				//save extra-map configuration like window size
				writeConfig(mapPath.substring(0, mapPath.lastIndexOf('.') + 1) + "cfg");
			}
			else {
				JOptionPane.showMessageDialog(null, "Error, map not saved :-(", null, JOptionPane.PLAIN_MESSAGE);
			}
			m.addSimpleObject(cursor, cursor.getX(), cursor.getY());
			IOObj.closeMap();
		}	
	}
			
	private static FilenameFilter saveFilter = new FilenameFilter() {
	    public boolean accept(File directory, String fileName) {
	        return fileName.endsWith(".map");
	    }
	};

	private static void newLevel(){		
		JPanel panel = new JPanel();
		JPanel labels = new JPanel(new GridLayout(0,1,0,12));
		JPanel input = new JPanel(new GridLayout(0,1,0,0));

		labels.add(new JLabel("Window width in pixels", SwingConstants.RIGHT));
		JTextField ww = new JTextField("800", 3);
		input.add(ww);
		labels.add(new JLabel("Window height in pixels", SwingConstants.RIGHT));
		JTextField wh = new JTextField("600", 3);
		input.add(wh);
		labels.add(new JLabel("Map width in cells", SwingConstants.RIGHT));
		JTextField mcw = new JTextField("40", 3);
		input.add(mcw);
		labels.add(new JLabel("Map height in cells", SwingConstants.RIGHT));
		JTextField mch = new JTextField("30", 3);
		input.add(mch);
		labels.add(new JLabel("Cell width in pixels", SwingConstants.RIGHT));
		JTextField cw = new JTextField("20", 3);
		input.add(cw);
		labels.add(new JLabel("Cell height in pixels", SwingConstants.RIGHT));
		JTextField ch = new JTextField("20", 3);
		input.add(ch);
		labels.add(new JLabel("Level name", SwingConstants.RIGHT));
		JTextField ln = new JTextField("My First Level!", 10);
		input.add(ln);
		labels.add(new JLabel("Background image filename", SwingConstants.RIGHT));
		JTextField bg = new JTextField("file.png", 10);
		input.add(bg);
		
		panel.add(labels, BorderLayout.WEST);
		panel.add(input, BorderLayout.EAST);
		
		int result = JOptionPane.showConfirmDialog(null, panel, "Map Initialization", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		if (result == JOptionPane.OK_OPTION) {
			mapHeight = Integer.parseInt(mch.getText());
			mapWidth = Integer.parseInt(mcw.getText());
			windowHeight = Integer.parseInt(wh.getText());
			windowWidth = Integer.parseInt(ww.getText());
			cellWidth = Integer.parseInt(cw.getText());
			cellHeight = Integer.parseInt(ch.getText());
			levelName = ln.getText();
			backgroundFileName = bg.getText();
			
			m = new SimpleMap(mapWidth, mapHeight, cellWidth, cellHeight);
			startMapAndWorld();
			
			//visual markers at edges of map to see where the cursor is in comparison to something
//			m.addSimpleObject(new BlackSolid(), 0, 0);
//			m.addSimpleObject(new MLearnerSolid(), 0, mapHeight * cellHeight-cellHeight);
//			m.addSimpleObject(new BlackSolid(), mapWidth * cellWidth-cellWidth, mapHeight * cellHeight-cellHeight);
//			m.addSimpleObject(new MLearnerSolid(), mapWidth * cellWidth-cellWidth, 0);
		}
		else {
			splashWindow();
		}
	}
	
	//instantiates map, world, cursor, keylisteners, camera, and starts game loop
	private static void startMapAndWorld(){
		cursor = new Cursor(cellWidth, cellHeight);
		m.addSimpleObject(cursor, cellWidth, cellHeight);
		w = new SimpleWorld(m, windowWidth, windowHeight, "Space Game Level Builder: " + levelName);
		w.addKeyListener(cursor);
		w.setCameraStalk(cursor);
		w.start(false);
		
		File f = new File("resources/images/" + backgroundFileName);
		if (f.exists())
			w.setBGImage(DesktopImgUpload.getInstance(f.getParentFile()).getImg(f.getName()));
		
		selectObjectTypeWindow();
		extraArgumentsWindow();
		instructionsWindow();
	}
	
	public static void placeObject(){
		try {
			swf.addSimpleObject(id[objectType], cursor.getX(), cursor.getY(), argField.getText(), m);
		}
		catch (Exception e) {
			JOptionPane.showMessageDialog(w, "Incorrect extra arguments for object", null, JOptionPane.PLAIN_MESSAGE);
		}
	}
	
	private static SimpleObject remover = new SimpleSolid() {
		@Override
		public void collision(SimpleObject s) {
			if (!s.getClass().getName().equals("objects.Cursor"))
				objectToRemove = s;
		}
	
		@Override
		public void update() {
			removeObjectSecond();
		}
	
		@Override
		public int id() {
			return 0;
		}
	};

	public static void removeObjectFirst(){
		m.addSimpleObject(remover, cursor.getX(), cursor.getY());
	}
	
	private static void removeObjectSecond(){
		if (objectToRemove != null)
			m.removeSimpleObject(objectToRemove);
		objectToRemove = null;
		m.removeSimpleObject(remover);
	}

	public static void removeSolid(){
		m.removeSimpleSolid(cursor.getX() / cellWidth, cursor.getY() / cellHeight);
	}
	
	private static void splashWindow(){	
		final JFrame splashFrame = new JFrame("LevelBuilder");
		
		class SplashActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				splashFrame.dispose();
				if (e.getActionCommand().equals("loadMap"))
					loadLevel();
				else
					newLevel();
			}
		}
		
		File f = new File("resources/images/splash.png");
		final BufferedImage img = DesktopImgUpload.getInstance(f.getParentFile()).getImg(f.getName()).getSlide();			
		JPanel BGPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
		        g.drawImage(img, 0, 0, null);
			}
		};
		
		BGPanel.setOpaque(true);
		BGPanel.setLayout(new BorderLayout());
		BGPanel.setPreferredSize(new Dimension(800,600));
		BGPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 300, 0));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setOpaque(false);
		
		JButton loadMap = new JButton("Load Map");
		loadMap.setActionCommand("loadMap");
		loadMap.addActionListener(new SplashActionHandler());
		buttonPanel.add(loadMap);
		
		JButton newMap = new JButton("New Map");
		newMap.setActionCommand("newMap");
		newMap.addActionListener(new SplashActionHandler());
		buttonPanel.add(newMap);
		
		BGPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		splashFrame.add(BGPanel);
		splashFrame.pack();
		splashFrame.setVisible(true);
	}

	//selects object type to place
	private static void selectObjectTypeWindow(){
		class ObjectTypeActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				objectType = Integer.parseInt(e.getActionCommand());
			}
		}

		JPanel masterPanel = new JPanel(new GridLayout(0,2,0,0));
		JPanel labelPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,0,0));
		JButton[] button = new JButton[constructors.length];
		
		String name;
		BufferedImage img = null;
		//starts at one because the cursor is object 0 and we don't want to be able to add another one to the map
		for (int i = 1; i < constructors.length; i++) {
			name = constructors[i].getName();
			
			try {
				img = ((SimpleObject) constructors[i].newInstance()).getImage().getSlide();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			labelPanel.add(new JLabel(name.substring(name.lastIndexOf('.') + 1, name.length()), 
					new ImageIcon(img.getScaledInstance(cellWidth, cellHeight, Image.SCALE_FAST)), SwingConstants.LEFT));
			button[i] = new JButton("<--");
			button[i].setActionCommand(Integer.toString(i));
			button[i].addActionListener(new ObjectTypeActionHandler());
			buttonPanel.add(button[i]);
		}
		
		masterPanel.add(labelPanel, BorderLayout.WEST);
		masterPanel.add(buttonPanel, BorderLayout.EAST);
		
		JFrame frame = new JFrame("Available Objects");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		masterPanel.setOpaque(true);
		frame.setContentPane(masterPanel);
		frame.pack();
		frame.setLocation(windowWidth, 0);
		frame.setVisible(true);
	
		verticalWindowPlacement += frame.getBounds().height + frame.getBounds().y;
	}

	private static void extraArgumentsWindow() {
		JPanel panel = new JPanel();
		argField = new JTextField("", 20);
		panel.add(argField);
		JFrame frame = new JFrame("Extra Arguments");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocation(windowWidth, verticalWindowPlacement);
		frame.setVisible(true);
		
		verticalWindowPlacement += frame.getBounds().height;
	}
	
	private static void instructionsWindow() {
		JFrame frame = new JFrame("Instructions");
		JPanel panel = new JPanel();
		JPanel key = new JPanel(new GridLayout(0, 1, 0, 0));
		JPanel description = new JPanel(new GridLayout(0, 1, 0, 0));
		
		key.add(new JLabel("space", SwingConstants.LEFT));
		description.add(new JLabel("Places object", SwingConstants.LEFT));
		key.add(new JLabel("r", SwingConstants.LEFT));
		description.add(new JLabel("Removes solid", SwingConstants.LEFT));
		key.add(new JLabel("o", SwingConstants.LEFT));
		description.add(new JLabel("Removes non-solid object", SwingConstants.LEFT));
		key.add(new JLabel("arrow keys", SwingConstants.LEFT));
		description.add(new JLabel("Moves cursor", SwingConstants.LEFT));
		key.add(new JLabel("shift", SwingConstants.LEFT));
		description.add(new JLabel("Moves cursor faster", SwingConstants.LEFT));
		key.add(new JLabel("alt", SwingConstants.LEFT));
		description.add(new JLabel("Moves cursor slower", SwingConstants.LEFT));
		key.add(new JLabel("", SwingConstants.LEFT));
		description.add(new JLabel("", SwingConstants.LEFT));
		key.add(new JLabel("", SwingConstants.LEFT));
		description.add(new JLabel("- To change world properties edit the applicable .cfg file in the saves folder.", SwingConstants.LEFT));
		key.add(new JLabel("", SwingConstants.LEFT));
		description.add(new JLabel("- Images must be in .png or .jpg format and sounds must be in .wav or .midi format.", SwingConstants.LEFT));
		key.add(new JLabel("", SwingConstants.LEFT));
		description.add(new JLabel("- Images and sounds must be in appropriate folders under the resources directory", SwingConstants.LEFT));
		key.add(new JLabel("", SwingConstants.LEFT));
		description.add(new JLabel("- In the 'Extra Arguments' window you can set additional options defined by an object", SwingConstants.LEFT));
		
		panel.add(key, BorderLayout.WEST);
		panel.add(description, BorderLayout.EAST);
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocation(windowWidth, verticalWindowPlacement);
		frame.setVisible(true);
	}
}

/*The MIT License (MIT)

Copyright (c) 2014 Mark Groeneveld

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package levelBuilder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import dialog.DialogGraph;
import dialog.DialogNode;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

/**
 * GUI application for creating and editing dialog graphs.
 * 
 * @author Mark Groeneveld
 * @version 1.0
 */

//TODO delete edges
//TODO delete vertices
public class DialogMaker {
	private static DirectedSparseGraph<DialogNode, Double> g;
	private static Layout<DialogNode, Double> layout;
	private static DialogGraph dg;
	private static HashMap<String, DialogNode> nodeMap;
	private static int windowWidth = 800;
	private static int windowHeight = 800;
	private static int verticalWindowPlacement = 0;
	private static int horizontalWindowPlacement = 0;
	private static JTextField textField, probSetField, childrenField, strategyField;
	private static JCheckBox npcBox;
	private static boolean isNPCBoxChecked;
	private static int strategy = 0;
	private static DialogNode selectedNode;
	private static PickedState<DialogNode> pickedState;
	private static double addedEdgeID = 0.0;
	private static String imgDir = "resources/images/dialogMaker/";
	private static String saveDir = "resources/dialogs/";
	private static String saveName = "New File";
	
	/**
	 * Starts the program and brings up various windows.
	 * 
	 * @param args not used
	 */
	public static void main(String[] args) {
		keyWindow();
		nodePropertiesWindow();
		actionWindow();
		instructionsWindow();
		splashWindow();
	}
	
	/**
	 * First window to interact with. Offers options of load graph or new graph.
	 */
	private static void splashWindow(){	
		final JFrame frame = new JFrame("Dialog Maker");
		
		//Handles button pushes.
		class SplashActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				if (e.getActionCommand().equals("loadGraph"))
					loadFilePopup();
				else
					loadGraph(true);
			}
		}
		
		//Loads and sets background image.
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgDir + "splash.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage img2 = img;
		JPanel panel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
		        g.drawImage(img2, 0, 0, null);
			}
		};
		
		panel.setOpaque(true);
		panel.setLayout(new BorderLayout());
		panel.setPreferredSize(new Dimension(800,600));
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 300, 0));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setOpaque(false);
		
		
		//Buttons
		JButton loadMap = new JButton("Load Graph");
		loadMap.setActionCommand("loadGraph");
		loadMap.addActionListener(new SplashActionHandler());
		buttonPanel.add(loadMap);
		
		JButton newMap = new JButton("New Graph");
		newMap.setActionCommand("newGraph");
		newMap.addActionListener(new SplashActionHandler());
		buttonPanel.add(newMap);
		
		
		panel.add(buttonPanel, BorderLayout.SOUTH);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Filters out all files except .xml.
	 */
	private static FilenameFilter fileFilter = new FilenameFilter() {
	    public boolean accept(File directory, String fileName) {
	        return fileName.endsWith(".xml");
	    }
	};
	
	/**
	 * Displays key (i.e. legend) to the graph.
	 */
	private static void keyWindow(){
		JPanel panel = new JPanel(new GridLayout(0,1,0,0));

		BufferedImage img1 = null;
		BufferedImage img2 = null;
		BufferedImage img3 = null;
		BufferedImage img4 = null;
		BufferedImage img5 = null;
		try {
			img1 = ImageIO.read(new File(imgDir + "green.png"));
			img2 = ImageIO.read(new File(imgDir + "red.png"));
			img3 = ImageIO.read(new File(imgDir + "blue.png"));
			img4 = ImageIO.read(new File(imgDir + "circle.png"));
			img5 = ImageIO.read(new File(imgDir + "square.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		panel.add(new JLabel("Initial Node", new ImageIcon(img1), SwingConstants.LEFT));
		panel.add(new JLabel("End Node", new ImageIcon(img2), SwingConstants.LEFT));
		panel.add(new JLabel("Intermediate Node", new ImageIcon(img3), SwingConstants.LEFT));
		panel.add(new JLabel("Player Node", new ImageIcon(img4), SwingConstants.LEFT));
		panel.add(new JLabel("NPC Node", new ImageIcon(img5), SwingConstants.LEFT));
				
		JFrame frame = new JFrame("Key");
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocation(windowWidth, 0);
		frame.setVisible(true);
		
		verticalWindowPlacement += frame.getBounds().height + frame.getBounds().y;
		horizontalWindowPlacement += frame.getBounds().width;
	}
	
	/**
	 * Displays properties of the currently selected node.
	 * Also allows changing of most node properties. 
	 */
	private static void nodePropertiesWindow() {
		//Performs actions based on button pushes.
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()) {
				case "probSet":
					//parsing new probSet string to double[]
					HashMap<Integer, Double[]> map = selectedNode.getProbSets();
					if (probSetField.getText().equals("")) {
						map.remove(strategy);
					}
					else {
						String[] probSetInput = probSetField.getText().split(",");
						int numChildren = probSetInput.length;
						Double[] newProbSet = new Double[numChildren];
						for (int c = 0; c < numChildren; c++)
							newProbSet[c] = Double.parseDouble(probSetInput[c]);
						map.put(strategy, newProbSet);
					}
					selectedNode.setProbSets(map);
				case "npc":
					if (isNPCBoxChecked)
						selectedNode.setNPC();
					else
						selectedNode.setPC();
				case "text":
					dg.changeNodeText(selectedNode, textField.getText());
				case "strategy":
					strategy = Integer.parseInt(strategyField.getText());
				}
			}
		}
		
		JPanel fieldPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel labelPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel masterPanel = new JPanel(new GridLayout(0,3,0,0));
		
		
		//Buttons, ckeckboxes, textboxes, and labels
		textField = new JTextField("May I buy your wand?", 10);
		fieldPanel.add(textField);
		labelPanel.add(new JLabel("Node Text"));
		JButton button1 = new JButton("Change");
		button1.setActionCommand("text");
		button1.addActionListener(new ActionHandler());
		buttonPanel.add(button1);
		
		npcBox = new JCheckBox();
		npcBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					isNPCBoxChecked = false;
				else
					isNPCBoxChecked = true;
			}
		});		
		fieldPanel.add(npcBox);
		labelPanel.add(new JLabel("NPC"));
		JButton button2 = new JButton("Change");
		button2.setActionCommand("npc");
		button2.addActionListener(new ActionHandler());
		buttonPanel.add(button2);
		
		childrenField = new JTextField("child1,child2", 10);		
		fieldPanel.add(childrenField);
		labelPanel.add(new JLabel("Children"));
		JButton button3 = new JButton("");
		buttonPanel.add(button3);
		
		probSetField = new JTextField("0.1,0.9", 10);
		fieldPanel.add(probSetField);
		labelPanel.add(new JLabel("Probability set"));
		JButton button4 = new JButton("Change");
		button4.setActionCommand("probSet");
		button4.addActionListener(new ActionHandler());
		buttonPanel.add(button4);
		
		strategyField = new JTextField("0", 10);
		fieldPanel.add(strategyField);
		labelPanel.add(new JLabel("Strategy"));
		JButton button5 = new JButton("Change");
		button5.setActionCommand("strategy");
		button5.addActionListener(new ActionHandler());
		buttonPanel.add(button5);
		
		
		masterPanel.add(buttonPanel);
		masterPanel.add(fieldPanel);
		masterPanel.add(labelPanel);
		
		JFrame frame = new JFrame("Node Properties");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fieldPanel.setOpaque(true);
		frame.setContentPane(masterPanel);
		frame.pack();
		frame.setLocation(windowWidth, verticalWindowPlacement);
		frame.setVisible(true);
		
		verticalWindowPlacement += frame.getBounds().height;
	}
	
	/**
	 * Provides other action buttons.
	 */
	private static void actionWindow() {
		//Performs actions based on button pushes.
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				switch (e.getActionCommand()) {
				case "edge":
					if (addedEdgeID != 0.0) {
						DialogNode n = g.getSource(addedEdgeID);
						int length;
						if (n.getChildren().length == 0)
							length = 0;
						else
							length = n.getChildren().length;
						DialogNode[] newChildren = new DialogNode[length + 1];
						for (int c = 0; c < newChildren.length - 1; c++)
							newChildren[c] = n.getChildren()[c];
						newChildren[newChildren.length-1] = nodeMap.get(g.getDest(addedEdgeID).getText());
						n.setChildren(newChildren);
						addedEdgeID = 0.0;
					}
				case "save":
					saveGraph();
				case "check":
					//Checks for errors and displays them if they exist.
					ArrayList<String> errorList = new ArrayList<String>();
					errorList = dg.check();
					if (errorList.isEmpty()) {
						JOptionPane.showMessageDialog(null, "No errors!", null, JOptionPane.PLAIN_MESSAGE);
					}
					else {
						JPanel panel = new JPanel(new GridLayout(0,1,0,0));
						for (String error : errorList)
							panel.add(new JLabel(error));
						JOptionPane.showMessageDialog(null, panel, "Errors. Fix these to continue.", JOptionPane.PLAIN_MESSAGE);
					}
				case "reload":
					loadGraph(false);
				}
			}
		}
		
		JPanel panel = new JPanel(new GridLayout(0,1,0,0));
		
		
		//Buttons
		JButton button3 = new JButton("New edge");
		button3.setActionCommand("edge");
		button3.addActionListener(new ActionHandler());
		panel.add(button3);
		
		JButton button6 = new JButton("Save");
		button6.setActionCommand("save");
		button6.addActionListener(new ActionHandler());
		panel.add(button6);
		
		JButton button7 = new JButton("Check");
		button7.setActionCommand("check");
		button7.addActionListener(new ActionHandler());
		panel.add(button7);
		
		JButton button1 = new JButton("Reload");
		button1.setActionCommand("reload");
		button1.addActionListener(new ActionHandler());
		panel.add(button1);
		
		
		JFrame frame = new JFrame("Other Input");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocation(windowWidth + horizontalWindowPlacement, 0);
		frame.setVisible(true);
	}
	
	/**
	 * Provides instructions for using the program.
	 */
	private static void instructionsWindow() {
		JFrame frame = new JFrame("Instructions");
		JPanel panel = new JPanel(new GridLayout(0, 1, 0, 0));
		
		panel.add(new JLabel("DO NOT USE THE RIGHT CLICK MENU.", SwingConstants.LEFT));
		panel.add(new JLabel("DO NOT REMOVE NODES OR VERTICES WITH THIS GUI. EDIT THE XML FILE INSTEAD.", SwingConstants.LEFT));
		panel.add(new JLabel("", SwingConstants.LEFT));
		panel.add(new JLabel("The 'Check' button runs various checks to make sure the graph does not contain errors.", SwingConstants.LEFT));
		panel.add(new JLabel("", SwingConstants.LEFT));
		panel.add(new JLabel("Transforming mode:", SwingConstants.LEFT));
		panel.add(new JLabel("Shift + click + drag to rotate", SwingConstants.LEFT));
		panel.add(new JLabel("Scroll to zoom", SwingConstants.LEFT));
		panel.add(new JLabel("Command + click + drag to to weird stretchy thing", SwingConstants.LEFT));
		panel.add(new JLabel("", SwingConstants.LEFT));
		panel.add(new JLabel("Picking mode:", SwingConstants.LEFT));
		panel.add(new JLabel("Click + drag to select multiple nodes", SwingConstants.LEFT));
		panel.add(new JLabel("Click + drag moves node or group of nodes", SwingConstants.LEFT));
		panel.add(new JLabel("Only in picking mode can you see different strategies", SwingConstants.LEFT));
		panel.add(new JLabel("New strategies will also only appear after refocusing on the main window", SwingConstants.LEFT));
		panel.add(new JLabel("", SwingConstants.LEFT));
		panel.add(new JLabel("Editing mode:", SwingConstants.LEFT));
		panel.add(new JLabel("Click + drag from one node to another creates an edge", SwingConstants.LEFT));
		panel.add(new JLabel("After drawing a new edge you have to click the 'new edge' button", SwingConstants.LEFT));
		panel.add(new JLabel("Click to add a new node", SwingConstants.LEFT));
		
		panel.setOpaque(true);
		frame.setContentPane(panel);
		frame.pack();
		frame.setLocation(windowWidth, verticalWindowPlacement);
		frame.setVisible(true);
		
		verticalWindowPlacement += frame.getBounds().height;
	}
	
	/**
	 * Popup for selecting file to load.
	 */
	private static void loadFilePopup(){	
		File savesFolder = new File(saveDir);
		File[] saves = savesFolder.listFiles(fileFilter);
		if (savesFolder.exists()){
			if (saves.length > 0){
				String[] saveNames = savesFolder.list(fileFilter);
				saveName = (String) JOptionPane.showInputDialog(null, "Which file would you like to load?",
						"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveNames[0]);
				if (saveName == null) {
					splashWindow();
				}
				else {
					loadGraph(false);
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
	
	/**
	 * Loads graph from a file. 
	 * 
	 * @param isNewGraph true if no file is being loaded
	 */
	private static void loadGraph(boolean isNewGraph) {
		g = new DirectedSparseGraph<DialogNode, Double>();
		dg = new DialogGraph();
		if (!isNewGraph)
			dg.load(saveDir + saveName);
		nodeMap = dg.getGraph();
		
		//add vertices and edges
		for (DialogNode n : nodeMap.values()) {
			g.addVertex(n);
			for (DialogNode c : n.getChildren())
				g.addEdge((double)(c.hashCode()*n.hashCode()), n, c, EdgeType.DIRECTED);
		}
		
		displayGraph();
	}

	/**
	 * Saves graph to file.
	 */
	private static void saveGraph() {		
		//Checks for errors
		ArrayList<String> errorList = new ArrayList<String>();
		errorList = dg.check();
		if (errorList.isEmpty()) {
			//Save popup.
			String filePath = null;
			File savesFolder = new File(saveDir);
			if (!savesFolder.exists())
				savesFolder.mkdir();
			String[] saveNames = new String[savesFolder.list(fileFilter).length+1];
			for (int i = 0; i < saveNames.length-1; i++) {
				saveNames[i] = savesFolder.list(fileFilter)[i];
			}
			saveNames[saveNames.length-1] = "New File";
		
			if (saveNames.length > 0){		
				saveName = (String) JOptionPane.showInputDialog(null, "Choose file to overwrite, or a new file",
						"Choose!", JOptionPane.PLAIN_MESSAGE, null, saveNames, saveName);
				if (saveName == "New File") {
					filePath = JOptionPane.showInputDialog(null, "New file name", "Write!", JOptionPane.PLAIN_MESSAGE);
					filePath = saveDir + filePath + ".xml";
				}
				else if (saveName == null) {
					return;
				}
				else {
					filePath = saveDir + saveName;
				}
				
				//Saves position of each node.
				for (DialogNode n : nodeMap.values()) {
					n.setX(((AbstractLayout<DialogNode, Double>) layout).getX(n));
					n.setY(((AbstractLayout<DialogNode, Double>) layout).getY(n));
				}	
				
				//Saves graph
				dg.save(filePath);
			}
		}
		//Display errors
		else {
			JPanel panel = new JPanel(new GridLayout(0,1,0,0));
			for (String error : errorList)
				panel.add(new JLabel(error));
			JOptionPane.showMessageDialog(null, panel, "Errors. Fix these to continue.", JOptionPane.PLAIN_MESSAGE);
		}
	}

	/**
	 * Various routines necessary for displaying graph.
	 */
	private static void displayGraph() {
		JFrame frame = new JFrame("Dialog Maker");
		layout = new KKLayout<DialogNode, Double>(g);
		VisualizationViewer<DialogNode, Double> vv = new VisualizationViewer<DialogNode, Double>(layout);
		layout.setSize(new Dimension(windowWidth,windowHeight));
		vv.setPreferredSize(new Dimension(windowWidth,windowHeight));
		
		//Changes fields in node properties window when a node is selected
		pickedState = vv.getPickedVertexState();
		pickedState.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object subject = e.getItem();
				if (subject instanceof DialogNode) {
					selectedNode = (DialogNode)subject;
					textField.setText(selectedNode.getText());
					npcBox.setSelected(selectedNode.getIsNPC());
					String working = "";
					for (int c = 0; c < selectedNode.getChildren().length; c++) {
						if (c == 0)
							working += selectedNode.getChildren()[c].getText();
						else
							working += "," + selectedNode.getChildren()[c].getText();
					}
					if (working.equals("null"))
						working = "";
					childrenField.setText(working.replace("]", "").replace("[", ""));
					working = Arrays.toString(selectedNode.getProbSets().get(strategy));
					if (working.equals("null"))
						working = "";
					probSetField.setText(working.replace("]", "").replace("[", "").replace(" ", ""));
				}
			}
		});
			
		//Colors vertices according to 'initial', 'end', or 'middle' status.
		vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<DialogNode, Paint>() {
			@Override
			public Paint transform(DialogNode n) {
				if (n.getText().equals("initial"))
					return new Color(100, 255, 100);
				if (n.getChildren().length == 0)
					return new Color(255, 100, 100);
				return new Color(100, 100, 255);
			}
		});
		
		//Labels vertices with node text.
		vv.getRenderContext().setVertexLabelTransformer(new Transformer<DialogNode, String>() {
			@Override
			public String transform(DialogNode n) {
				return n.getText().split(" ")[0];
			}
			 
		});
		
		//Draws shape of vertices according to player or non-player status.
		vv.getRenderContext().setVertexShapeTransformer(new Transformer<DialogNode ,Shape>() {
			@Override
			public Shape transform(DialogNode n) {
				if (n.getIsNPC())
					return new Rectangle(-15, -15, 30, 30);
				else
					return new Ellipse2D.Double(-15.0, -15.0, 30.0, 30.0);
			}
			
		});
		
		//Labels edges with probability of child being selected under the current strategy.
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Double ,String>() {
			@Override
			public String transform(Double e) {
				DialogNode source = g.getSource(e);
				if (source.getProbSets().size() > 0)
					for (int c = 0; c < source.getChildren().length; c++)
						if (source.getChildren()[c].equals(g.getDest(e))) {
							Double[] a = source.getProbSets().get(strategy);
							if (source.getProbSets().get(strategy) != null)
								return Double.toString(source.getProbSets().get(strategy)[c]);
							else //If node does not have probSet for that strategy, default to strategy 0.
								return Double.toString(source.getProbSets().get(0)[c]);
						}
				return null;
			} 
		});
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		//Routines for editing mode.
		EditingModalGraphMouse<DialogNode, Double> gm = new EditingModalGraphMouse<DialogNode, Double>(vv.getRenderContext(), 
			//Runs when a new node is created.
			new Factory<DialogNode>() {
			@Override
			public DialogNode create() {
				HashMap<Integer, Double[]> emptyMap = new HashMap<Integer, Double[]>();
				DialogNode[] emptyArray = new DialogNode[0];
				DialogNode n = new DialogNode(isNPCBoxChecked, textField.getText(), emptyMap, emptyArray, 0, 0);
				dg.addNode(n);
				return n;
			}
		},	
			//Runs when a new edge is created.
			new Factory<Double>() {
			@Override
			public Double create() {
				addedEdgeID = Math.random();
				return addedEdgeID;
			}
		}); 
		vv.setGraphMouse(gm);
		
		//Frame and mode menu.
		JMenuBar menuBar = new JMenuBar();
		JMenu modeMenu = gm.getModeMenu();
		modeMenu.setText("Mouse Mode");
		modeMenu.setIcon(null);
		modeMenu.setPreferredSize(new Dimension(100,20));
		menuBar.add(modeMenu);
		frame.setJMenuBar(menuBar);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv); 
		frame.pack();
		frame.setVisible(true); 
				
		//Sets position of each node.
		for (DialogNode n : nodeMap.values()) {
			Point2D.Double point = new Point2D.Double(n.getX(), n.getY());
			if (point.x != 0.0 && point.y != 0.0)
				layout.setLocation(n, point);
		}
	}
}

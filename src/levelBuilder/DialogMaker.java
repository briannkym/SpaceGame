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
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
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

//TODO delete edges
//TODO move JUNG to levelBuilder package
public class DialogMaker {
	private static DirectedSparseGraph<DialogNode, Double> g = new DirectedSparseGraph<DialogNode, Double>();
	private static Layout<DialogNode, Double> layout;
	private static VisualizationViewer<DialogNode, Double> vv;
	private static DialogGraph dg = new DialogGraph();
	private static HashMap<String, DialogNode> nodeMap;
	private static Object[] nodeArray;	
	private static int windowWidth = 800;
	private static int windowHeight = 800;
	private static int verticalWindowPlacement = 0;
	private static JTextField textField, probSetField, childrenField, strategyField;
	private static JCheckBox npcBox;
	private static boolean npc;
	private static int strategy = 0;
	private static DialogNode selectedNode;
	private static PickedState<DialogNode> pickedState;
	private static double lastEdge = 0.0;
	private static String imgDir = "resources/images/dialogMaker/";
	private static String saveDir = "resources/dialogs/";
	
	public static void main(String[] args) {
		keyWindow();
		nodePropertiesWindow();
		inputWindow();
		instructionsWindow();
		splashWindow();
	}
	
	private static void splashWindow(){	
		final JFrame splashFrame = new JFrame("Dialog Maker");
		
		class SplashActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				splashFrame.dispose();
				if (e.getActionCommand().equals("loadGraph"))
					loadGraph();
				else
					newGraph();
			}
		}
				
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(imgDir + "splash.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		final BufferedImage img2 = img;
		JPanel BGPanel = new JPanel(){
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
		        g.drawImage(img2, 0, 0, null);
			}
		};
		
		BGPanel.setOpaque(true);
		BGPanel.setLayout(new BorderLayout());
		BGPanel.setPreferredSize(new Dimension(800,600));
		BGPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 300, 0));
		
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		buttonPanel.setOpaque(false);
		
		JButton loadMap = new JButton("Load Graph");
		loadMap.setActionCommand("loadGraph");
		loadMap.addActionListener(new SplashActionHandler());
		buttonPanel.add(loadMap);
		
		JButton newMap = new JButton("New Graph");
		newMap.setActionCommand("newGraph");
		newMap.addActionListener(new SplashActionHandler());
		buttonPanel.add(newMap);
		
		BGPanel.add(buttonPanel, BorderLayout.SOUTH);
		
		splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		splashFrame.add(BGPanel);
		splashFrame.pack();
		splashFrame.setVisible(true);
	}
	
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
	}
	
	private static void nodePropertiesWindow() {
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("probSet")) {
					//parsing new probSet string to double[][]
					HashMap<Integer, Double[]> map = selectedNode.getProbSet();
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
					dg.changeNodeProbSet(selectedNode.getText(), map);
				}
				if (e.getActionCommand().equals("npc"))
					dg.switchNodeNPC(selectedNode.getText());
				if (e.getActionCommand().equals("text"))
					dg.changeNodeText(selectedNode.getText(), textField.getText());
			}
		}
		
		JPanel fieldPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel labelPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel masterPanel = new JPanel(new GridLayout(0,3,0,0));
		
		
		textField = new JTextField("example", 10);
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
					npc = false;
				else
					npc = true;
			}
		});		
		
		fieldPanel.add(npcBox);
		labelPanel.add(new JLabel("NPC"));
		JButton button2 = new JButton("Change");
		button2.setActionCommand("npc");
		button2.addActionListener(new ActionHandler());
		buttonPanel.add(button2);
		
		childrenField = new JTextField("c00,c01", 10);		
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
	
	private static void inputWindow() {
		class ActionHandler implements ActionListener {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("edge") && lastEdge != 0.0) {
					DialogNode n = g.getSource(lastEdge);
					int length;
					if (n.getChildren() == null)
						length = 0;
					else
						length = n.getChildren().length;
					String[] newChildren = new String[length + 1];
					for (int c = 0; c < newChildren.length - 1; c++)
						newChildren[c] = n.getChildren()[c];
					newChildren[newChildren.length-1] = g.getDest(lastEdge).getText();
					dg.changeNodeChildren(n.getText(), newChildren);
					lastEdge = 0.0;
				}
				if (e.getActionCommand().equals("strategy"))
					strategy = Integer.parseInt(strategyField.getText());
				if (e.getActionCommand().equals("save"))
					saveGraph();
			}
		}
		
		JPanel fieldPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel buttonPanel = new JPanel(new GridLayout(0,1,0,0));
		JPanel masterPanel = new JPanel(new GridLayout(0,2,0,0));
	
		
		fieldPanel.add(new JTextField("<-- Click this after adding a new edge.", 21));
		JButton button3 = new JButton("New edge");
		button3.setActionCommand("edge");
		button3.addActionListener(new ActionHandler());
		buttonPanel.add(button3);
		
		strategyField = new JTextField("0", 21);
		fieldPanel.add(strategyField);
		JButton button5 = new JButton("Change Strategy");
		button5.setActionCommand("strategy");
		button5.addActionListener(new ActionHandler());
		buttonPanel.add(button5);
		
		fieldPanel.add(new JTextField("This field does nothing."));
		JButton button6 = new JButton("Save Graph");
		button6.setActionCommand("save");
		button6.addActionListener(new ActionHandler());
		buttonPanel.add(button6);	
		
		masterPanel.add(buttonPanel);
		masterPanel.add(fieldPanel);
		
		JFrame frame = new JFrame("Other Input");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fieldPanel.setOpaque(true);
		frame.setContentPane(masterPanel);
		frame.pack();
		frame.setLocation(windowWidth, verticalWindowPlacement);
		frame.setVisible(true);
		
		verticalWindowPlacement += frame.getBounds().height;
	}
	
	private static void instructionsWindow() {
		JFrame frame = new JFrame("Instructions");
		JPanel panel = new JPanel(new GridLayout(0, 1, 0, 0));
		
		panel.add(new JLabel("Transforming mode:", SwingConstants.LEFT));
		panel.add(new JLabel("Shift + click + drag to rotate", SwingConstants.LEFT));
		panel.add(new JLabel("Scroll to zoom", SwingConstants.LEFT));
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
	
	private static void newGraph() {
		nodeMap = dg.getMap();
		startGraph();
	}
	
	private static void loadGraph() {
		dg.test();
		nodeMap = dg.getMap();
		Collection<DialogNode> nodeCollection = nodeMap.values();
		nodeArray = nodeCollection.toArray();
		
		//adds vertices
		for (int n = 0; n < nodeArray.length; n ++) {
			DialogNode cn = (DialogNode) nodeArray[n];
			g.addVertex(cn);
		}		
		//add edges
		for (int n = 0; n < nodeArray.length; n ++) {
			DialogNode cn = (DialogNode) nodeArray[n];
			if (cn.getChildren() != null)
				for (int c = 0; c < cn.getChildren().length; c++) {
					DialogNode child = nodeMap.get(cn.getChildren()[c]);
					if (cn.isNPC()) {						
						g.addEdge((double)(child.hashCode()*cn.hashCode()), cn, child, EdgeType.DIRECTED);
					}
					else
						g.addEdge((double)(child.hashCode()*cn.hashCode()), cn, child, EdgeType.DIRECTED);
				}
		}
		
		 //sets position of each node
		Object[] array = nodeMap.values().toArray();
		for (Object o : array) {
			DialogNode n = (DialogNode) o;
			Point2D.Double point = new Point2D.Double(n.getX(), n.getY());
			if (point.x != 0 && point.y != 0)
				layout.setLocation(n, point);
		}
		
		startGraph();
	}
	
	private static void startGraph() {
		JFrame frame = new JFrame("Dialog Maker");
		 //TODO change to static layout and change test nodes
		layout = new KKLayout<DialogNode, Double>(g);
		vv = new VisualizationViewer<DialogNode, Double>(layout);
		pickedState = vv.getPickedVertexState();
		pickedState.addItemListener(new ItemListener() { //Changes fields in node properties window to match selected node
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object subject = e.getItem();
				if (subject instanceof DialogNode) {
					selectedNode = (DialogNode)subject;
					textField.setText(selectedNode.getText());
					npcBox.setSelected(selectedNode.isNPC());
					String working = Arrays.toString(selectedNode.getChildren());
					if (working.equals("null"))
						working = "";
					childrenField.setText(working.replace("]", "").replace("[", ""));
					working = Arrays.toString(selectedNode.getProbSet().get(strategy));
					if (working.equals("null"))
						working = "";
					probSetField.setText(working.replace("]", "").replace("[", ""));
				}
			}
		});
		layout.setSize(new Dimension(windowWidth,windowHeight));
		vv.setPreferredSize(new Dimension(windowWidth,windowHeight));			 
		vv.getRenderContext().setVertexFillPaintTransformer(new Transformer<DialogNode, Paint>() {
			@Override
			public Paint transform(DialogNode n) {
				if (n.getText().equals("initial"))
					return new Color(100, 255, 100);
				if (n.getChildren() == null)
					return new Color(255, 100, 100);
				return new Color(100, 100, 255);
			}
		});
		vv.getRenderContext().setVertexLabelTransformer(new Transformer<DialogNode, String>() {
			@Override
			public String transform(DialogNode n) {
				return n.getText().split(" ")[0];
			}
			 
		});
		vv.getRenderContext().setVertexShapeTransformer(new Transformer<DialogNode ,Shape>() {
			@Override
			public Shape transform(DialogNode n) {
				if (n.isNPC())
					return new Rectangle(-15, -15, 30, 30);
				else
					return new Ellipse2D.Double(-15.0, -15.0, 30.0, 30.0);
			}
			
		});
		vv.getRenderContext().setEdgeLabelTransformer(new Transformer<Double ,String>() {
			@Override
			public String transform(Double e) {
				DialogNode source = g.getSource(e);
				if (source.getProbSet().size() > 0)
					for (int c = 0; c < source.getChildren().length; c++)
						if (nodeMap.get(source.getChildren()[c]).equals(g.getDest(e))) {
							Double[] a = source.getProbSet().get(strategy);
							if (source.getProbSet().get(strategy) != null)
								return Double.toString(source.getProbSet().get(strategy)[c]);
							else //If node does not have probSet for that strategy, default to strategy 0.
								return Double.toString(source.getProbSet().get(0)[c]);
						}
				return null;
			} 
		});
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		EditingModalGraphMouse<DialogNode, Double> gm = new EditingModalGraphMouse<DialogNode, Double>(vv.getRenderContext(), 
				new Factory<DialogNode>() {
					@Override
					public DialogNode create() {
						dg.createNode(npc, textField.getText(), null, null, 0, 0);
						DialogNode n = nodeMap.get(textField.getText());
						n.setX(((AbstractLayout<DialogNode, Double>) layout).getX(n));
						n.setY(((AbstractLayout<DialogNode, Double>) layout).getY(n));
						return n;
					}
		}, new Factory<Double>() {
			@Override
			public Double create() {
				lastEdge = Math.random();
				return lastEdge;
			}
		}); 
		vv.setGraphMouse(gm);
		
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
	}
	
	private static void saveGraph() {
		Object[] array = nodeMap.values().toArray();
		for (Object o : array) { //saves position of each node
			DialogNode n = (DialogNode) o;
			n.setX(((AbstractLayout<DialogNode, Double>) layout).getX(n));
			n.setY(((AbstractLayout<DialogNode, Double>) layout).getY(n));
		}
		if (dg.check())
			dg.save("blah"); //TODO fimename selection
	}
}

package levelBuilder;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import world.SimpleMap;
import world.SimpleWorld;

public class levelBuilder {
	//direction of movement for cursor
	int XDirection;
	int YDirection;
	//starting position of cursor
	static int CursorY = 30;
	static int CursorX = 20;
	//window (frame?) dimensions
	static int height = 30 * 20;
	static int width = 40 * 20;
			
	static cursor cam = new cursor();
	static SimpleMap m = new SimpleMap(width, height, 20, 20);
	
	public static void main(String[] args){
		m.addSimpleObject(cam, CursorX, CursorY);
		
		//visual markers to see where the cursor is in comparison to something
		m.addSimpleObject(new cursor(), 0, 0);
		m.addSimpleObject(new cursor(), 0, height-30);
		m.addSimpleObject(new cursor(), width-20, height-30);
		m.addSimpleObject(new cursor(), width-20, 0);
		
		
		SimpleWorld w = new SimpleWorld(m, width, height, "Testing");	
		
		//I don't know what is going on here, it seems to do what I want it to though
		levelBuilder LB = new levelBuilder();
		w.addKeyListener(LB.new keyEventHandler());
		
		w.setCameraStalk(cam);
		w.start(false);
	}
	
	//method for moving cursor, called by keyEventHandler when an arrow key is pressed
	//moves cursor by deleting old one and creating a new one at a new position
	private void move(){
		//new position called for by key input
		int NewY;
		int NewX;
		
		m.removeSimpleObject(cam); 
		NewY = CursorY - 30*YDirection;
		NewX = CursorX + 20*XDirection;
		//checks if new position is within bounds
		if (NewY < 0 || NewX < 0 || NewY > height - 30 || NewX > width - 20){
			//out of bounds, no more moving in that direction
		}
		else {
			//in bounds, can move in that direction
			CursorY = NewY;
			CursorX = NewX;
		}
		m.addSimpleObject(cam, CursorX, CursorY);
	}
	
	//this class gets called whenever a key is pressed
	private class keyEventHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			
			int key = e.getKeyCode();

			if (key == KeyEvent.VK_LEFT) {
				XDirection = -1;
				YDirection = 0;
				move();
			}

			if (key == KeyEvent.VK_UP) {
				XDirection = 0;
				YDirection = 1;
				move();
			}

			if (key == KeyEvent.VK_RIGHT) {
				XDirection = 1;
				YDirection = 0;
				move();
			}

			if (key == KeyEvent.VK_DOWN) {
				XDirection = 0;
				YDirection = -1;
				move();
			}
		}
	}
}

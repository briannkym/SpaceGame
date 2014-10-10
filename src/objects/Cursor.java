//Copyright (c) 2014 Mark Groeneveld

package objects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import levelBuilder.LevelBuilder;
import sprite.ColorImg;
import sprite.Img;
import world.SimpleObject;

/**
 * Cursor for LevelBuilder
 * This is a necessary component of LevelBuilder.
 * 
 * @author Mark Groeneveld
 * @author Brian Nakayama
 * @version 1.0
 */

public class Cursor extends SimpleObject implements KeyListener{
	private Img<BufferedImage> red;
	private int xdir = 0, ydir = 0;
	private int cellWidth = 20, cellHeight = 20;;
	private int HMoveRate = cellWidth;
	private int VMoveRate = cellHeight;
	
	public Cursor(){
		red = new ColorImg(0x40FF0000, cellWidth, cellHeight);
		this.setImage(red);		
	}
	
	public Cursor(int w, int h){
		cellWidth = w;
		cellHeight = h;
		red = new ColorImg(0x40FF0000, cellWidth, cellHeight);
		this.setImage(red);		
	}
	
	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
		if (xdir != 0 || ydir != 0) {
			this.move(xdir, ydir, true);
			xdir = 0;
			ydir = 0;
		}
	}

	@Override
	public int id() {
		return 0;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//move cursor in a direction
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			xdir = -HMoveRate;
		if (e.getKeyCode() == KeyEvent.VK_UP)
			ydir = -VMoveRate;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			xdir = HMoveRate;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			ydir = VMoveRate;
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			HMoveRate = cellWidth * 10;
			VMoveRate = cellHeight * 10;
		}
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			HMoveRate = 1;
			VMoveRate = 1;
		}
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			LevelBuilder.placeObject();
		if (e.getKeyCode() == KeyEvent.VK_R)
			LevelBuilder.removeSolid();
		if (e.getKeyCode() == KeyEvent.VK_S)
			LevelBuilder.saveLevel();
		if (e.getKeyCode() == KeyEvent.VK_O)
			LevelBuilder.removeObjectFirst();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
			HMoveRate = cellWidth;
			VMoveRate = cellHeight;
		}
		if (e.getKeyCode() == KeyEvent.VK_ALT) {
			HMoveRate = cellWidth;
			VMoveRate = cellHeight;
		}
	}
}
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

package objects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import desktopView.ColorImg;
import levelBuilder.LevelBuilder;
import sprite.Img;
import world.SimpleObject;

/**
 * Cursor for LevelBuilder
 * Provides means of placing and removing objects.
 * Listens for key presses and takes appropriate action.
 * This is a necessary component of LevelBuilder.
 * 
 * @author Mark Groeneveld
 * @author Brian Nakayama
 * @version 1.0
 */

public class Cursor extends SimpleObject implements KeyListener{
	private Img red;
	private int cellWidth = 20, cellHeight = 20;;
	private int HMoveRate = cellWidth;
	private int VMoveRate = cellHeight;
	
	/**
	 * Default constructor.
	 */
	public Cursor(){
		red = new ColorImg(0x40FF0000, cellWidth, cellHeight);
		this.setImage(red);		
	}
	
	/**
	 * Constructor providing custom cell sizes.
	 * 
	 * @param w Cell width.
	 * @param h Cell height.
	 */
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
	}

	@Override
	public int id() {
		return 0;
	}

	/**
	 * Unused.
	 */
	@Override
	public void keyTyped(KeyEvent e) {	
	}

	/**
	 * Listens for key presses and takes appropriate actions.
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			this.move(-HMoveRate, 0, true);
			break;
		case KeyEvent.VK_UP:
			this.move(0, -VMoveRate, true);
			break;
		case KeyEvent.VK_RIGHT:
			this.move(HMoveRate, 0, true);
			break;
		case KeyEvent.VK_DOWN:
			this.move(0, VMoveRate, true);
			break;
		case KeyEvent.VK_SHIFT:
			HMoveRate = cellWidth * 10;
			VMoveRate = cellHeight * 10;
			break;
		case KeyEvent.VK_ALT:
			HMoveRate = 1;
			VMoveRate = 1;
			break;
		case KeyEvent.VK_SPACE:
			LevelBuilder.placeObject();
			break;
		case KeyEvent.VK_R:
			LevelBuilder.removeSolid();
			break;
		case KeyEvent.VK_S:
			LevelBuilder.saveLevel();
			break;
		case KeyEvent.VK_O:
			LevelBuilder.placeObjectRemover();
			break;
		}
	}

	/**
	 * Listens for key releases and takes appropriate actions.
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SHIFT:
			HMoveRate = cellWidth;
			VMoveRate = cellHeight;
			break;
		case KeyEvent.VK_ALT:
			HMoveRate = cellWidth;
			VMoveRate = cellHeight;
			break;
		}
	}
}

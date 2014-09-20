package objects;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import sprite.ColorImg;
import sprite.Img;
import world.SimpleObject;

public class Cursor extends SimpleObject implements KeyListener{
	private static int population = 0;
	private final Img red = new ColorImg(0x40FF0000, 20, 20);
	private int xdir = 0, ydir = 0;
	
	public Cursor(){
		this.setImage(red);
		population = (population +1) % 256;
	}
	
	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
		this.move(xdir, ydir, true);
		xdir = 0;
		ydir = 0;
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
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			xdir = -20; 
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			ydir = -20;
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			xdir = 20;
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			ydir = 20;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
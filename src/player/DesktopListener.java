package player;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class DesktopListener implements KeyListener{

	private Protagonist p;
	
	public DesktopListener(Protagonist p){
		this.p = p;
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			p.update(Protagonist.up);
			break;
		case KeyEvent.VK_DOWN:
			p.update(Protagonist.down);
			break;
		case KeyEvent.VK_RIGHT:
			p.update(Protagonist.right);
			break;
		case KeyEvent.VK_LEFT:
			p.update(Protagonist.left);
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()){
		case KeyEvent.VK_UP:
			p.update(Protagonist.sUp);
			break;
		case KeyEvent.VK_DOWN:
			p.update(Protagonist.sDown);
			break;
		case KeyEvent.VK_RIGHT:
			p.update(Protagonist.sRight);
			break;
		case KeyEvent.VK_LEFT:
			p.update(Protagonist.sLeft);
			break;
		}
	}
}

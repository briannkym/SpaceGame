package player;

import main.SpaceGame;
import sprite.Img;

public interface Protagonist {
	public static final int down = 0, right = 1, up = 2, left = 3;
	public static final int sDown = 4, sRight = 5, sUp = 6, sLeft = 7;
	
	
	public void update(int command);
}

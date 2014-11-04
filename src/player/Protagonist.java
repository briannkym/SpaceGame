package player;

import main.SpaceGame;
import sprite.Img;


public interface Protagonist {
	public static final int down = 0, right = 1, up = 2, left = 3;
	public static final int sDown = 4, sRight = 5, sUp = 6, sLeft = 7;

	public static final Img iUp = SpaceGame.dc.getImg("resources/images/Prot/protagonist-N.png");
	public static final Img iRight = SpaceGame.dc.getImg("resources/images/Prot/protagonist-E.png");
	public static final Img iDown = SpaceGame.dc.getImg("resources/images/Prot/protagonist-S.png");
	public static final Img iLeft = SpaceGame.dc.getImg("resources/images/Prot/protagonist-W.png");
	
	public void update(int command);
}

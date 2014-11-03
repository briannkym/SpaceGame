package player;

import main.SpaceGame;
import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;


public class ProtagonistTD extends SimpleSolid implements Protagonist{

	public static final Img iUp = SpaceGame.dc.getImg("resources/images/Prot/protagonist-N.png");
	public static final Img iRight = SpaceGame.dc.getImg("resources/images/Prot/protagonist-E.png");
	public static final Img iDown = SpaceGame.dc.getImg("resources/images/Prot/protagonist-S.png");
	public static final Img iLeft = SpaceGame.dc.getImg("resources/images/Prot/protagonist-W.png");
	private int command = 0;
	
	public ProtagonistTD(){
		this.setImage(iDown);
	}
	
	@Override
	public void update(int command){
		this.command = command;
	}

	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
		switch(command){
		case down:
			this.move(0, 4, true);
			this.setImage(iDown);
			this.getImage().animate(true);
			break;
		case up:
			this.move(0, -4, true);
			this.setImage(iUp);
			this.getImage().animate(true);
			break;
		case right:
			this.move(4, 0, true);
			this.setImage(iRight);
			this.getImage().animate(true);
			break;
		case left:
			this.move(-4, 0, true);
			this.setImage(iLeft);
			this.getImage().animate(true);
			break;
		case sDown:
		case sUp:
		case sLeft:
		case sRight:
			this.getImage().animate(false);
			break;
		default:
			break;
		}
	}

	@Override
	public int id() {
		// TODO Auto-generated method stub
		return 0;
	}

}

package objects;

import main.SpaceGame;
import sprite.Anm;
import sprite.Img;
import sprite.ImgCommand;
import sprite.ImgUpload;
import world.SimpleObject;
import world.SimpleSolid;

public class NPC extends SimpleSolid{
	public static int ID = generateID();
	
	public static final ImgUpload wheels = SpaceGame.dc.getImgUpload("resources/images/Wheels/");
	
	public static final Img iUp = wheels.getImg("Wheels-N.png");
	public static final Img iRight = wheels.getImg("Wheels-E.png");
	public static final Img iDown = wheels.getImg("Wheels-S.png");
	public static final Img iLeft = wheels.getImg("Wheels-W.png");
	
	public static final int down = 0, right = 1, up = 2, left = 3;
	public static final int sDown = 4, sRight = 5, sUp = 6, sLeft = 7;
	
	private int move = 0;
	private int wait = 0;
	private int direction = 0;
	
	private ImgCommand pause = new ImgCommand(){
		@Override
		public void accept(Img i) {
			//Do nothing.
		}
		
		public void accept(Anm a) {
			a.animate(false);
		}
	};
	
	private ImgCommand resume = new ImgCommand(){
		@Override
		public void accept(Img i) {
			//Do nothing.
		}
		
		public void accept(Anm a) {
			a.animate(true);
		}
	};

	public NPC() {
		this.setImage(iDown);
	}

	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
		if(wait == 0){
			this.getImage().accept(resume);
			move = (int)(Math.random()*4);
		} else if (wait == -20){
			wait = (int)(Math.random()*30);
			move = -1;
			this.getImage().accept(pause); 
		}
		
		wait --;
		switch (move) {
		case 0:
			this.move(0, 3, true);
			direction = down;
			break;
		case 1:
			this.move(0, -3, true);
			direction = up;
			break;
		case 2:
			this.move(3, 0, true);
			direction = right;
			break;
		case 3:
			this.move(-3, 0, true);
			direction = left;
			break;
		default:
		}

		switch (direction) {
		case up:
			this.setImage(iUp);
			break;
		case down:
			this.setImage(iDown);
			break;
		case left:
			this.setImage(iLeft);
			break;
		case right:
			this.setImage(iRight);
			break;
		}
	}

	@Override
	public int id() {
		// TODO Auto-generated method stub
		return ID;
	}


}

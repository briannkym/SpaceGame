package objects;

import sprite.Img;
import desktopView.ColorImg;
import world.SimpleSolid;
import world.SimpleObject;

public class BlackSolid extends SimpleSolid{
	public static int ID = generateID();
	static Img img = new ColorImg(0xFF000000, 16, 16);
	
	public BlackSolid(){
		this.setImage(img);
	}
	
	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
	}

	@Override
	public int id() {
		return ID;
	}
}

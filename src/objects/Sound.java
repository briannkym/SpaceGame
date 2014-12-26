package objects;

import desktopView.ColorImg;
import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;

public class Sound extends SimpleSolid{
	public static int ID = generateID();
	
	private static int population = 0;
	private final Img solidBlue = new ColorImg(0xFF0000FF, 20, 20);
	
	public Sound(){
		this.setImage(solidBlue);
		population = (population +1) % 256;
//		this.playSound("resources/sounds/III. Menuetto and Trio (Allegretto).wav");
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

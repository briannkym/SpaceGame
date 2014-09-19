package objects;

import sprite.ColorImg;
import sprite.Img;
import world.SimpleSolid;
import world.SimpleObject;

public class Solid1 extends SimpleSolid{
	private static int population = 0;
	private final Img solidBlack = new ColorImg(0xFF000000, 20, 20);
	
	public Solid1(){
		this.setImage(solidBlack);
		population = (population +1) % 256;
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
}

package objects;

import sprite.ColorImg;
import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;

public class MLSolid extends SimpleSolid{
	private static int population = 0;
	private final Img solidRed = new ColorImg(0xFFFF0000, 20, 20);
	
	public MLSolid(){
		this.setImage(solidRed);
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

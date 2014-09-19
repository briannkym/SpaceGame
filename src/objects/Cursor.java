package objects;

import sprite.ColorImg;
import sprite.Img;
import world.SimpleObject;

public class Cursor extends SimpleObject{
	private static int population = 0;
	private final Img red = new ColorImg(0x40FF0000, 20, 20);
	
	public Cursor(){
		this.setImage(red);
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
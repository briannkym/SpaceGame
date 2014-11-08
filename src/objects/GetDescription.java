package objects;

import java.awt.image.BufferedImage;

import desktopView.ColorImg;
import sprite.Img;
import world.SimpleObject;

//Place this object over another to have its getDescription() printed to console.
public class GetDescription extends SimpleObject{	
	private static Img img = new ColorImg(0x70000000, 20, 20);
	private boolean printed = false;
	private int counter = 0;

	@Override
	public void collision(SimpleObject s) {
		if (printed == false) {
			System.out.println(s.getDescription());
			printed = true;
		}
	}
	
	public GetDescription() {
		this.setImage(img);
	}

	@Override
	public void update() {
		counter ++;
		if (counter == 10) {
			this.getMap().removeSimpleObject(this);
		}
	}

	@Override
	public int id() {
		return 1;
	}

}

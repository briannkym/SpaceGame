package objects;

import java.awt.image.BufferedImage;
import java.io.File;

import sprite.ColorImg;
import sprite.Img;
import sprite.ImgUpload;
import world.SimpleSolid;
import world.SimpleObject;

public class BlackSolid extends SimpleSolid{
	static Img<BufferedImage> img = new ColorImg(0xFF000000, 20, 20);
	
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
		return 2;
	}
}

package objects;

import java.awt.image.BufferedImage;

import sprite.ColorImg;
import sprite.Img;
import world.SimpleObject;

public class Sign extends SimpleObject{
//	private Img<BufferedImage> defaultImg = new TextImg("T", 20, 20);
	private Img<BufferedImage> defaultImg = new ColorImg(0x40FF0000, 20, 20);
	private Img<BufferedImage> img;
	private String description = "";
	
	public Sign() {
		this.setImage(defaultImg);
	}
	
	public Sign(String s){
//		img = new TextImg("", width, height);
//		this.setImage(img);
		this.setImage(defaultImg);
	}

	@Override
	public void collision(SimpleObject s) {		
	}

	@Override
	public void update() {
	}

	@Override
	public int id() {
		return 5;
	}
	
	@Override
	public String getDescription() {
		return "";
	}
	
	@Override
	public SimpleObject getClone(String s) {		
		return new Sign(s);
	}
}

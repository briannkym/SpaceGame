package objects;

import java.awt.image.BufferedImage;

import desktopView.ColorImg;

import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;

public class MLearnerSolid extends SimpleSolid{
	private static final Img<BufferedImage> solidRed = new ColorImg(0xFFFF0000, 20, 20);
	private static int population = 0;
	private int id;
	private int isItTime = 0;
	
	public MLearnerSolid(){
		this.setImage(solidRed);
		population ++;
		id = population % 30;
	}
	
	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
		isItTime = isItTime % 30;
	}

	@Override
	public int id() {
		return 7;
	}
}

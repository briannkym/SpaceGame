package objects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sprite.Img;
import sprite.Sprite;
import world.SimpleObject;

public class Splash extends SimpleObject{
   
   	public Splash(){
   		BufferedImage BI = null;
   		try {
   		    BI = ImageIO.read(new File("resources/images/splash.png"));
   		} catch (IOException e) {
   		}
   		Img img = new Sprite(BI);
   		this.setImage(img);
   	}

	@Override
	public void collision(SimpleObject s) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int id() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}

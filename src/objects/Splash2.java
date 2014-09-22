package objects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sprite.Img;
import sprite.Sprite;
import world.SimpleWorldObject;

public class Splash2 extends SimpleWorldObject{

	@Override
	public void updateScreen(BufferedImage bi, Graphics2D g) {
				
	}
	
	public Splash2(){
		BufferedImage BI = null;
   		try {
   		    BI = ImageIO.read(new File("resources/images/splash.png"));
   		} catch (IOException e) {
   		}
   		Img img = new Sprite(BI);
	}
	
}

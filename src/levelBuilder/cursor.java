package levelBuilder;

import sprite.ColorImg;
import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;

public class cursor extends SimpleSolid{
	private int move = 0;
	private static final int[][] direction = {{0,0},{0,0},{0,0},{0,0}};
	private static int population = 0;
	private final Img blue = new ColorImg(0x99FF9900, 20, 30);
	
	public cursor(){
		this.setImage(blue);
		population = (population +1) % 256;
	}
	
	@Override
	public void collision(SimpleObject s) {
		switch(s.id()){
		case 0:
			move = (int)(Math.random() * 4);
		}
		
	}

	@Override
	public void update() {
		this.move(direction[move][0], direction[move][1], true);	
	}

	@Override
	public int id() {
		return 0;
	}
}

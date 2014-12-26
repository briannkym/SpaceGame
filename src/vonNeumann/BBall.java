package vonNeumann;

import world.SimpleObject;
import world.SimpleSolid;

public class BBall extends SimpleSolid{
	
	public static final int ID = 0x100;
	private int x_vel = 0;
	private int y_vel = 0;
	
	
	public BBall(int x_vel, int y_vel){
		this.x_vel = x_vel;
		this.y_vel = y_vel;
	}
	
	
	@Override
	public void collision(SimpleObject s) {
		switch(s.id()){
			case ID:
				int difx = getX() - s.getX();
				int dify = getY() - s.getY();
				
				break;
		}
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int id() {
		return ID;
	}

}

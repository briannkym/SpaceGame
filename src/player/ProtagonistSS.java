package player;

import world.SimpleObject;
import world.SimpleSolid;

public class ProtagonistSS extends SimpleSolid implements Protagonist {

	public static final int D_NORTH = 0;
	public static final int D_WEST = 1;
	public static final int D_EAST = 2;
	public static final int D_SOUTH = 3;

	int hor_vel = 0;
	int ver_vel = 0;
	int acc_x = 0;
	int acc_y = 0;

	int gravity = 2;
	int jump = 7;
	int speed = 4;
	
	int direction = D_NORTH;
	
	boolean grounded = true;

	@Override
	public void update(int command) {
		switch (command) {
		case down:
			break;
		case up: {
			if(grounded){
				ver_vel = -jump;
			}
		}
			break;
		case left:
			hor_vel = -speed;
			break;
		case right:
			hor_vel = speed;
			break;
		case sDown:
			break;
		case sUp:
			break;
		case sLeft:
			if (speed < 0){
				hor_vel = 0;
			}
			break;
		case sRight:
			if (speed > 0){
				hor_vel = 0;
			}
			break;
		}

	}

	public int getDirection() {
		return direction;
	}

	@Override
	public void collision(SimpleObject s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update() {
		SimpleSolid ground;
		grounded = false;
		switch (direction){
		case D_NORTH:
			if((ground = getSolid(0, ver_vel, true))!= null){
				grounded = true;
				ver_vel = ground.getY() - getMap().cellHeight - getY();
			}
			move(0, ver_vel, true);
			move(hor_vel, 0, 2);
			break;
		case D_WEST:
			if((ground = getSolid(ver_vel, 0, true))!= null){
				grounded = true;
				ver_vel = ground.getX() - getMap().cellWidth - getX();
			}
			move(ver_vel, 0, true);
			move(0, -hor_vel, 2);
			break;
		case D_EAST:
			if((ground = getSolid(-ver_vel, 0, true))!= null){
				grounded = true;
				ver_vel = ground.getX() + getMap().cellWidth - getX();
			}
			move(-ver_vel, 0, true);
			move(0, hor_vel, 2);
			break;
		case D_SOUTH:
			if((ground = getSolid(0, -ver_vel, true))!= null){
				grounded = true;
				ver_vel = ground.getY() + getMap().cellHeight - getY();
			}
			move(0, -ver_vel, true);
			move(-hor_vel, 0, 2);
			break;
		}
		
		ver_vel -= gravity;
	}

	@Override
	public int id() {
		return ID;
	}

}

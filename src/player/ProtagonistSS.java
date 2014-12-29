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

	int direction = D_NORTH;
	int gravity = 2;
	int max_grav_coeff = 7;
	int jump = 7;
	int speed = 4;

	boolean grounded = true;

	public ProtagonistSS() {
		setImage(prot[down][right]);
		getImage().accept(pause);
	}

	@Override
	public void update(int command) {
		switch (command) {
		case down:
			break;
		case up: {
			if (grounded) {
				ver_vel = -jump;
			}
		}
			break;
		case left:
			hor_vel = -speed;
			setImage(prot[down][left]);
			getImage().accept(resume);
			break;
		case right:
			hor_vel = speed;
			setImage(prot[down][right]);
			getImage().accept(resume);
			break;
		case sDown:
			break;
		case sUp:
			break;
		case sLeft:
			if (hor_vel < 0) {
				hor_vel = 0;
				getImage().accept(pause);
			}
			break;
		case sRight:
			if (hor_vel > 0) {
				hor_vel = 0;
				getImage().accept(pause);
			}
			break;
		}

	}

	public int getDirection() {
		return direction;
	}

	@Override
	public void collision(SimpleObject s) {
		
	}

	@Override
	public void update() {
		grounded = false;
		switch (direction) {
		case D_NORTH:
			if(!move(0, ver_vel, true)){
				move(0, ver_vel, -1);
				grounded = true;
				ver_vel = 0;
			}
			move(hor_vel, 0, 2);
			break;
		case D_WEST:
			if(!move(ver_vel, 0, true)){
				move(ver_vel, 0, -1);
				grounded = true;
				ver_vel = 0;
			}
			move(0, -hor_vel, 2);
			break;
		case D_EAST:
			if(!move(-ver_vel, 0, true)){
				move(-ver_vel, 0, -1);
				grounded = true;
				ver_vel = 0;
			}
			move(0, hor_vel, 2);
			break;
		case D_SOUTH:
			if(!move(0, -ver_vel, true)){
				move(0, -ver_vel, -1);
				grounded = true;
				ver_vel = 0;
			}
			move(-hor_vel, 0, 2);
			break;
		}

		if (ver_vel != gravity *max_grav_coeff){
			ver_vel += gravity;
		}
		
	}

	@Override
	public int id() {
		return ID;
	}

}

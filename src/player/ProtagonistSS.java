package player;

import main.SpaceGame;
import world.SimpleObject;
import world.SimpleSolid;

public class ProtagonistSS extends SimpleSolid implements Protagonist {

	int hor_vel = 0;
	int ver_vel = 0;
	int acc_x = 0;
	int acc_y = 0;

	int direction = down;
	int next_direction = down;

	int gravity = 2;
	int max_grav = 14;
	int jump = 7;
	int speed = 4;

	boolean grounded = true;
	boolean facing_right = true;

	public ProtagonistSS() {
		setImage(prot[direction][right]);
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
			next_direction = (direction + 2) % 4;
		}
			break;
		case left:
			hor_vel = -speed;
			facing_right = false;
			setImage(prot[direction][left]);
			getImage().accept(resume);
			next_direction = (direction + 1) % 4;
			break;
		case right:
			hor_vel = speed;
			facing_right = true;
			setImage(prot[direction][right]);
			getImage().accept(resume);
			next_direction = (direction + 3) % 4;
			break;
		case sAction1:
			direction = next_direction;
			facing_right = true;
			setImage(prot[direction][right]);
			getImage().accept(pause);
			break;
		case sAction2:
			hor_vel = 1;
			setImage(prot[direction][right]);
			break;
		case sDown:
			next_direction = direction;
			break;
		case sUp:
			next_direction = direction;
			break;
		case sLeft:
			next_direction = direction;
			if (hor_vel < 0) {
				hor_vel = 0;
			}
			break;
		case sRight:
			next_direction = direction;
			if (hor_vel > 0) {
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

	}

	@Override
	public void update() {
		int diff = (90 * direction - SpaceGame.Orientation + 360) % 360;
		if (diff != 0) {
			SpaceGame.Orientation = (SpaceGame.Orientation + 360) % 360;
			if (diff <= 180) {
				SpaceGame.Orientation += 9;
				SpaceGame.canvas.setRotation(SpaceGame.Orientation);
			} else {
				SpaceGame.Orientation -= 9;
				SpaceGame.canvas.setRotation(SpaceGame.Orientation);
			}
		}
		grounded = false;
		switch (direction) {
		case down:
			if (!move(0, ver_vel, true)) {
				fuzzMove(0, ver_vel, -1);
				grounded = true;
				ver_vel = 0;
			}
			fuzzMove(hor_vel, 0, -1);
			break;
		case left:
			if (!move(ver_vel, 0, true)) {
				fuzzMove(ver_vel, 0, -1);
				grounded = true;
				ver_vel = 0;
			}
			fuzzMove(0, -hor_vel, -1);
			break;
		case right:
			if (!move(-ver_vel, 0, true)) {
				fuzzMove(-ver_vel, 0, -1);
				grounded = true;
				ver_vel = 0;
			}
			fuzzMove(0, hor_vel, -1);
			break;
		case up:
			if (!move(0, -ver_vel, true)) {
				fuzzMove(0, -ver_vel, -1);
				grounded = true;
				ver_vel = 0;
			}
			fuzzMove(-hor_vel, 0, -1);
			break;
		}

		if (Math.abs(ver_vel) < max_grav) {
			ver_vel += gravity;
		}

		if (!grounded) {
			if (ver_vel * gravity < 0) {
				if (facing_right) {
					setImage(prot[direction][jumpRight]);
				} else {
					setImage(prot[direction][jumpLeft]);
				}
			} else {
				if (facing_right) {
					setImage(prot[direction][fallRight]);
				} else {
					setImage(prot[direction][fallLeft]);
				}
			}
		} else {
			if (facing_right) {
				setImage(prot[direction][right]);
			} else {
				setImage(prot[direction][left]);
			}
			
			if(hor_vel == 0)
			getImage().accept(pause);
		}

	}

	@Override
	public int id() {
		return ID;
	}

}

package player;

import world.SimpleObject;
import world.SimpleSolid;

/* Idea for Protagonist:
 * Gravity Distortion Device
 * Bionic Legs
 * Time Refrigerator v1
 * Time Refrigerator v2
 * Telekinector
 * Teletransporter
 * Alternative Reality Machine
 * Spacio-Temporal Parodoxim
 */

public class ProtagonistTD extends SimpleSolid implements Protagonist {

	private int move = 0;
	private int direction = down;	

	public ProtagonistTD() {
		this.setImage(prot[down][down]);
	}

	@Override
	public void update(int command) {
		switch (command) {
		case down:
			direction = command;
			this.getImage().accept(resume);
			move |= 0b1;
			break;
		case up:
			direction = command;
			this.getImage().accept(resume);
			move |= 0b10;
			break;
		case right:
			direction = command;
			this.getImage().accept(resume);
			move |= 0b100;
			break;
		case left:
			direction = command;
			this.getImage().accept(resume);
			move |= 0b1000;
			break;
		case sDown:
			move &= 0b1110;
			break;
		case sUp:
			move &= 0b1101;
			break;
		case sRight:
			move &= 0b1011;
			break;
		case sLeft:
			move &= 0b0111;
			break;
		default:
		}
	}

	@Override
	public void collision(SimpleObject s) {
	}

	@Override
	public void update() {
		switch (move) {
		case 0b0001:
			this.move(0, 4, 2);
			direction = down;
			break;
		case 0b0010:
			this.move(0, -4, 2);
			direction = up;
			break;
		case 0b0100:
			this.move(4, 0, 2);
			direction = right;
			break;
		case 0b1000:
			this.move(-4, 0, 2);
			direction = left;
			break;
		case 0b0101:
			this.move(3, 3, 2);
			break;
		case 0b1001:
			this.move(-3, 3, 2);
			break;
		case 0b0110:
			this.move(3, -3, 2);
			break;
		case 0b1010:
			this.move(-3, -3, 2);
			break;
		default:
			this.getImage().accept(pause);
			move = 0;
		}

		switch (direction) {
		case up:
			this.setImage(prot[down][up]);
			break;
		case down:
			this.setImage(prot[down][down]);
			break;
		case left:
			this.setImage(prot[down][left]);
			break;
		case right:
			this.setImage(prot[down][right]);
			break;
		}
	}

	@Override
	public int id() {
		return ID;
	}

}

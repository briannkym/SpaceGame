//Copyright (c) 2014 Mark Groeneveld

package objects;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import desktopView.DesktopImgUpload;

import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;

/**
 * Example of player character.
 * 
 * @author Mark Groeneveld
 * @author Brian Nakayama
 * @version 0.5
 */

/* TODO Change this description
 * 
 * Red is a person from Hong Kong who identifies as female.
 * She was a pro-democracy activist but after threats to her life she is now...
 * Height 5' 3"
 * Weight 125 lb
 * Wears an old but in good condition backpack everywhere (I didn't put in the the low res figures yet)
 */

/**
 * EXTRA ARGUMENTS
 * 
 * @param facingDirection
 *            (Int) 0-3 indicating initial orientation.
 * @param isWandering
 *            (boolean) Turns wandering on or off.
 * @param range
 *            (Int) Indicates maximum wander range in one step.
 * @param speed
 *            (Int) Wander speed in pixels / update call.
 * @param pauseTime
 *            (Int) Time between wanderings (in # of updates).
 * @param dialogFile
 *            (String) Dialog file name
 */
//TODO draw backpack for Red
//TODO Dialog
//TODO how are dialogs initiated?
//TODO make character, player-character, and npc abstract classes?
public class Red extends SimpleSolid{ //TODO Change Red to your character's name.
	static private String path = "resources/images/" +  "Red" + "/"; //TODO Change "Red" to your character's name. This is the subfolder where your images are stored.
	static private File f1 = new File(path + "FrontRun.png");
	static private File f2 = new File(path + "FrontStand.png");	
	static private File f3 = new File(path + "LeftRun.png");
	static private File f4 = new File(path + "LeftStand.png");
	static private File f5 = new File(path + "BackRun.png");
	static private File f6 = new File(path + "BackStand.png");
	static private File f7 = new File(path + "RightRun.png");
	static private File f8 = new File(path + "RightStand.png");
	
	static private Img<?>[] Running = {DesktopImgUpload.getInstance(f1.getParentFile()).getImg(f1.getName()), 
		DesktopImgUpload.getInstance(f3.getParentFile()).getImg(f3.getName()), 
		DesktopImgUpload.getInstance(f5.getParentFile()).getImg(f5.getName()), 
		DesktopImgUpload.getInstance(f7.getParentFile()).getImg(f7.getName())};
	static private Img<?>[] Standing = {DesktopImgUpload.getInstance(f2.getParentFile()).getImg(f2.getName()), 
		DesktopImgUpload.getInstance(f4.getParentFile()).getImg(f4.getName()), 
		DesktopImgUpload.getInstance(f6.getParentFile()).getImg(f6.getName()), 
		DesktopImgUpload.getInstance(f8.getParentFile()).getImg(f8.getName())};
		
	private boolean moving = false;
	private int ySpeed = 0;
	private int xSpeed = 0;
	private int yDestination = 0;
	private int xDestination = 0;
	private int direction = 0;
	private String dialogF = null;
	
	private int wanderRange;
	private int wanderSpeed;
	private int wanderPause;
	private boolean wandering = false;
	private int counter = 0;
	
	@SuppressWarnings("unchecked")
	public Red() {
		this.setImage((Img<BufferedImage>) Standing[direction]);
	}
	
	@SuppressWarnings("unchecked")
	public Red(int facingDirection, boolean isWandering, int range, int speed, int pauseTime, String dialogFile) {
		wanderRange = range;
		wanderSpeed = speed;
		wanderPause = pauseTime;
		direction = facingDirection;
		wandering = isWandering;
		dialogF = dialogFile;
		this.setImage((Img<BufferedImage>) Standing[direction]);
	}
	
	//Triggered when the player interacts with this NPC. Not useful for PC's (unless you want to have a dialog with yourself?).
	public void dialog(){
		
	}
	
	@Override
	public void collision(SimpleObject s) {
		if (s.getSolid() != null)
			stand();
	}
	
	/**
	 * Starts or stops wandering behavior
	 * 
	 * @param range
	 *            (Int) Indicates maximum wander range in one step.
	 * @param speed
	 *            (Int) Wander speed in pixels / update call.
	 * @param pauseTime
	 *            (Int) Time between wanderings, in number of updates.
	 */
	public void wander(int range, int speed, int pauseTime) {
		wanderRange = range;
		wanderSpeed = speed;
		wanderPause = pauseTime;
		wandering = true;
	}
	
	public void stopWandering() {
		wandering = false;
	}
	
	@SuppressWarnings("unchecked")
	public void stand() {
		moving = false;
		this.setImage((Img<BufferedImage>) Standing[direction]);
	}

	@Override
	public void update() {
		//movement
		if (moving) {
			if (Math.abs(yDestination - this.getY()) < Math.abs(ySpeed))
				ySpeed = yDestination - this.getY();
			if (Math.abs(xDestination - this.getX()) < Math.abs(xSpeed))
				xSpeed = xDestination - this.getX();
			
			if (yDestination == this.getY())
				ySpeed = 0;
			if (xDestination == this.getX())
				xSpeed = 0;
			if (ySpeed == 0 && xSpeed == 0)
				stand();
			else
				this.move(xSpeed, ySpeed, true);
		}
		
		//wandering
		if (wandering) {
			counter ++;
			if (counter >= wanderPause && moving == false) {
				this.animatedMove(this.getX() + (int)(Math.random() * wanderRange) - (int)(wanderRange / 2), 
						this.getY() + (int)(Math.random() * wanderRange) - (int)(wanderRange / 2), wanderSpeed);
				counter = 0;
			}
		}
	}
	
	/**
	 * Runs a movement animation and moves object in small steps until destination is reached
	 * 
	 * @param x
	 *            The new absolute x-coordinate.
	 * @param y
	 *            The new absolute y-coordinate.
	 * @param speed
	 *            Movement speed in pixels / update.
	 */
	@SuppressWarnings("unchecked")
	public void animatedMove(int x, int y, int speed) {
		moving = true;
		
		xDestination = x;
		yDestination = y;
		if (xDestination < 0)
			xDestination = 0;
		else if (xDestination > this.getMap().getMapPixelWidth())
			xDestination = this.getMap().getMapPixelWidth();
		if (yDestination < 0)
			yDestination = 0;
		else if (yDestination > this.getMap().mapWmax)
			yDestination = this.getMap().getMapPixelHeight();
		
		x = x - this.getX();
		y = y - this.getY();
		if (x > 0)
			xSpeed = speed;
		else if (x == 0)
			xSpeed = 0;
		else if (x < 0)
			xSpeed = -speed;
		if (y > 0)
			ySpeed = speed;
		else if (y == 0)
			ySpeed = 0;
		else if (y < 0)
			ySpeed = -speed;
		
		if (Math.abs(y) > Math.abs(x)) {
			if (y > 0)
				direction = 0;
			else
				direction = 2;
		}
		else {
			if (x > 0)
				direction = 3;
			else
				direction = 1;
		}
		this.setImage((Img<BufferedImage>) Running[direction]);
	}

	@Override
	public int id() {
		return 6;
	}
	
	@Override
	public SimpleObject getClone(String s) throws InputMismatchException, NoSuchElementException{
		Scanner scanner = new Scanner(s);
		SimpleObject temp = new Red(scanner.nextInt(), 
				scanner.nextBoolean(), 
				scanner.nextInt(), 
				scanner.nextInt(), 
				scanner.nextInt(), 
				scanner.next());
		scanner.close();
		return temp;
	}
	
	@Override
	public String getDescription() {
		return Integer.toString(direction)
				+ " " + Boolean.toString(wandering)
				+ " " + Integer.toString(wanderRange)
				+ " " + Integer.toString(wanderSpeed)
				+ " " + Integer.toString(wanderPause)
				+ " " + dialogF;
	}
}

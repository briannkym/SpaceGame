/*The MIT License (MIT)

Copyright (c) 2014 Mark Groeneveld

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

package objects;

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
 * Short black hair with red stripe
 * Confident
 * Wears an old but in-good-condition backpack everywhere (I didn't put in the the low res figures yet)
 */

//TODO draw backpack for Red
//TODO how are dialogs initiated?
//TODO make character, player-character, and npc abstract classes?
public class Red extends SimpleSolid{ //TODO Change Red to your character's name.
	public static int ID = generateID();
	
	static private String imgDir = "resources/images/objects/Red/"; //TODO Change "Red" to your character's name. This is the subfolder where your images are stored.
	static private File f1 = new File(imgDir + "FrontRun.png");
	static private File f2 = new File(imgDir + "FrontStand.png");	
	static private File f3 = new File(imgDir + "LeftRun.png");
	static private File f4 = new File(imgDir + "LeftStand.png");
	static private File f5 = new File(imgDir + "BackRun.png");
	static private File f6 = new File(imgDir + "BackStand.png");
	static private File f7 = new File(imgDir + "RightRun.png");
	static private File f8 = new File(imgDir + "RightStand.png");
	
	static private Img[] Running = {DesktopImgUpload.getInstance(f1.getParentFile()).getImg(f1.getName()), 
		DesktopImgUpload.getInstance(f3.getParentFile()).getImg(f3.getName()), 
		DesktopImgUpload.getInstance(f5.getParentFile()).getImg(f5.getName()), 
		DesktopImgUpload.getInstance(f7.getParentFile()).getImg(f7.getName())};
	static private Img[] Standing = {DesktopImgUpload.getInstance(f2.getParentFile()).getImg(f2.getName()), 
		DesktopImgUpload.getInstance(f4.getParentFile()).getImg(f4.getName()), 
		DesktopImgUpload.getInstance(f6.getParentFile()).getImg(f6.getName()), 
		DesktopImgUpload.getInstance(f8.getParentFile()).getImg(f8.getName())};
		
	private boolean moving = false;
	private int ySpeed = 0;
	private int xSpeed = 0;
	private int yDestination = 0;
	private int xDestination = 0;
	private int direction = 0;
	private String dialogFile = null;
	
	private int wanderRange;
	private int wanderSpeed;
	private int wanderPause;
	private boolean isWandering = false;
	private int counter = 0;
	
	public Red() {
		this.setImage((Img) Standing[direction]);
	}
	
	/**
	 * EXTRA ARGUMENTS
	 * 
	 * @param facingDirection
	 *            0-3 indicating initial orientation.
	 * @param isWandering
	 *            Turns wandering on or off.
	 * @param range
	 *            Indicates maximum wander range in one step.
	 * @param speed
	 *            Wander speed in pixels / update call.
	 * @param pauseTime
	 *            Time between wanderings (in # of updates).
	 * @param dialogFile
	 *            Dialog file name
	 */
	public Red(int facingDirection, boolean isWandering, int range, int speed, int pauseTime, String dialogFile) {
		this.wanderRange = range;
		this.wanderSpeed = speed;
		this.wanderPause = pauseTime;
		this.direction = facingDirection;
		this.isWandering = isWandering;
		this.dialogFile = dialogFile;
		this.setImage((Img) Standing[direction]);
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
	 *            Indicates maximum wander range in one step.
	 * @param speed
	 *            Wander speed in pixels / update call.
	 * @param pauseTime
	 *            Time between wanderings, in number of updates.
	 */
	public void wander(int range, int speed, int pauseTime) {
		wanderRange = range;
		wanderSpeed = speed;
		wanderPause = pauseTime;
		isWandering = true;
	}
	
	public void stopWandering() {
		isWandering = false;
	}
	
	public void stand() {
		moving = false;
		this.setImage((Img) Standing[direction]);
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
		if (isWandering) {
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
		this.setImage((Img) Running[direction]);
	}

	@Override
	public int id() {
		return ID;
	}
	
	@Override
	public SimpleObject getClone(String s) throws InputMismatchException, NoSuchElementException{
		Scanner scanner = new Scanner(s);
		SimpleObject object = new Red(scanner.nextInt(), 
				scanner.nextBoolean(), 
				scanner.nextInt(), 
				scanner.nextInt(), 
				scanner.nextInt(), 
				scanner.next());
		scanner.close();
		return object;
	}
	
	@Override
	public String getDescription() {
		return Integer.toString(direction)
				+ " " + Boolean.toString(isWandering)
				+ " " + Integer.toString(wanderRange)
				+ " " + Integer.toString(wanderSpeed)
				+ " " + Integer.toString(wanderPause)
				+ " " + dialogFile;
	}
}

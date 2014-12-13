package objects;

import java.io.File;

import desktopView.DesktopImgUpload;
import sprite.Img;
import world.SimpleObject;
import world.SimpleSolid;

/*TODO READ THIS FIRST
This is an example SimpleObject. Instructions are prefaced by "//TODO". That puts little blue bars to the right of this screen so you can see where they are.
Every .java file in the objects folder will automatically be loaded into LevelBuilder and be available for use in the game.
To create your own SimpleObject or SimpleSolid copy this file. Change the filename and two places where "ExampleObject" are written to your new name.
There are a few other things to do described in the rest of the TODO's.  
There are also some more advanced things you can do that are not described here. You can check out Cursor.java or Sign.java for examples of that.
*/

public class ExampleObject extends SimpleObject{ //TODO This object is a SimpleObject. If you want a SimpleSolid just change SimpleObject to SimpleSolid.
	static File f = new File("resources/images/objects/ExampleObject/ExampleObject.png"); //TODO This is the filename of this object's icon.
	static Img img = DesktopImgUpload.getInstance(f.getParentFile()).getImg(f.getName());

	public ExampleObject() {
		//TODO This method is called whenever a new object of this type is created.
		this.setImage(img); //For instance, every time a new ExampleObject is created, this line sets its image.
	}
	
	@Override
	public void collision(SimpleObject s) {
		//TODO This method is called every time this object collides with another object. s is the other object.
		//You can do different things depending on what type of object s is. s's id is obtainable via s.id()
	}

	@Override
	public void update() {
		//TODO This method gets called every time the game state is iterated, many times per second.
	}

	@Override
	public int id() {
		return 4; //TODO Every object needs a unique ID. Change this number to the next available one. 
	}
}

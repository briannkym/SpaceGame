package levelBuilder;

import objects.Cursor;
import test.testObject;
import world.SimpleMap;
import world.SimpleWorld;

public class Test {	
	public static void main(String[] args){
		SimpleMap m = new SimpleMap(160, 120, 20, 20);
		testObject cam = new testObject();
		m.addSimpleObject(cam, 790, 590);
		SimpleWorld w = new SimpleWorld(m, 800, 600, "Testing");
		w.setCameraStalk(cam);
		w.start(false);
		
		m.removeSimpleObject(cam);
	}
}

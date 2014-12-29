package player;

import main.SpaceGame;
import sprite.Img;
import sprite.ImgUpload;
import sprite.Pause;
import sprite.Resume;
import world.SimpleObject;

public interface Protagonist {
	public Pause pause = Pause.getInstance();
	public Resume resume = Resume.getInstance();

	public static final int down = 0, right = 1, up = 2, left = 3;
	public static final int sDown = 4, sRight = 5, sUp = 6, sLeft = 7;
	public static final int action1 = 8, action2 = 9;
	public static final int sAction1 = 10, sAction2 = 11;

	public static final int ID = SimpleObject.generateID();

	public static final ImgUpload protImg = SpaceGame.dc
			.getImgUpload("resources/images/Prot/");
	public static final Img prot[][] = {
			{ protImg.getImg("protagonist-S.png"),
				protImg.getImg("protagonist-E.png"),
					protImg.getImg("protagonist-N.png"),
					protImg.getImg("protagonist-W.png") },
			{ protImg.getRotatedImg("protagonist-S.png", 90),
						protImg.getRotatedImg("protagonist-E.png", 90),
					protImg.getRotatedImg("protagonist-N.png", 90),
					protImg.getRotatedImg("protagonist-W.png", 90) },
			{ protImg.getRotatedImg("protagonist-S.png", 180),
						protImg.getRotatedImg("protagonist-E.png", 180),
					protImg.getRotatedImg("protagonist-N.png", 180),
					protImg.getRotatedImg("protagonist-W.png", 180) },
			{ protImg.getRotatedImg("protagonist-S.png", 270),
						protImg.getRotatedImg("protagonist-E.png", 270),
					protImg.getRotatedImg("protagonist-N.png", 270),
					protImg.getRotatedImg("protagonist-W.png", 270) } };

	public void update(int command);
}

//Single file (class) containing several game properties:
public class GameProperties {
	
	//Screen dimensions:
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 800;
	
	//Scoreboard:
	public static final int SCORE_TXT_SIZE = 20;
	
	//# pixels sprites move upon trigger:
	public static final int PLAYER_WIDTH = 50;
	public static final int PLAYER_HEIGHT = 60;
	public static final int PLAYER_STEP = 25;
	
	public static final int PRJCT_PLAYER_STEP = 30;
	public static final int PRJCT_PLAYER_WIDTH = 25;
	public static final int PRJCT_PLAYER_HEIGHT = 30;
	
	public static final int ENEMY_WIDTH = 50;
	public static final int ENEMY_HEIGHT = 50;
	public static final int ENEMY_SPACING = 0;
	public static int ENEMY_STEP = 5;
	
	public static final int PRJCT_ENEMY_STEP = 20;
	public static final int PRJCT_ENEMY_WIDTH = 25;
	public static final int PRJCT_ENEMY_HEIGHT = 30;
	
	public static final int PTS_PER_ENEMY = 20;

	//# rows & columns in ememy array:
	public static final int ENEMY_ROWS = 5;
	public static final int ENEMY_COLS = 11;
	public static int ENEMY_COUNT = (ENEMY_ROWS * ENEMY_COLS);
}

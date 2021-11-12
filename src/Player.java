import javax.swing.JLabel;

public class Player extends Sprite {
	
	private Boolean alienFlag[][];
	private JLabel lbl_player;
	private Boolean canMove;
	private int playerLives, playerScore;
	
	//Getters & setters:
	public Boolean getCanMove() {return canMove;}
	public void setCanMove(Boolean canMove) {this.canMove = canMove;}
	
	public int getPlayerLives() {return playerLives;}
	public void setPlayerLives(int playerLives) {this.playerLives = playerLives;}
	
	public int getPlayerScore() {return playerScore;}
	public void setPlayerScore(int playerScore) {this.playerScore = playerScore;}
	
	public JLabel getLbl_player() {return lbl_player;}
	public void setLbl_player(JLabel lbl_player) {this.lbl_player = lbl_player;}
	
	//Constructors:
	public Player() {
		super(GameProperties.PLAYER_WIDTH, GameProperties.PLAYER_HEIGHT, "img_player.png", false, true, false);
		canMove = true;
		playerLives = 3;
		playerScore = 0;
		alienFlag = new Boolean[GameProperties.ENEMY_ROWS][GameProperties.ENEMY_COLS];
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				alienFlag[i][j] = true;
			}
		}
	}
	
	public Player(JLabel temp) {
		super(GameProperties.PLAYER_WIDTH, GameProperties.PLAYER_HEIGHT, "img_player.png", false, true, false);
		this.lbl_player = temp;
		canMove = true;
		playerLives = 3;
		playerScore = 0;
		alienFlag = new Boolean[GameProperties.ENEMY_ROWS][GameProperties.ENEMY_COLS];
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				alienFlag[i][j] = true;
			}
		}
	}
	
	public void setAlienFlag(int i, int j, Boolean value) {
		alienFlag[i][j] = value;
	}
	
	public Boolean checkAliens() {
		Boolean alive = false;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				if(alienFlag[i][j]) {
					alive = true;
					break;
				}
			}
		}
		return alive;
	}
	
	//Other methods
	@Override
	public void hide() {
		lbl_player.setVisible(false);
	}
	
	@Override
	public void show() {
		lbl_player.setVisible(true);
	}
}

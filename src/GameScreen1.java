import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameScreen1 extends JFrame implements KeyListener{

	private static final long serialVersionUID = -8824959153681940829L;
	
	//Storage classes for game sprites:
	private Player myPlayer;
	private ProjectilePlayer prjct_player;
	private ProjectileEnemy [][] enemyProjectiles;
	private Enemy[][] enemies;
	private Background myBg;
	
	//JLabels to display sprites:
	private JLabel lbl_player, lbl_prjct_player, lbl_bg, lbl_score, lbl_currentScore;
	private ImageIcon img_player, img_enemy, img_prjct_player, img_prjct_enemy, img_bg;
	private JLabel[] lbl_playerLives;
		
	//Graphics container:
	private Container container1;
	
	//GUI set-up (constructor):
	public GameScreen1() {
		//Window title:
		super("Space Invaders");
		//Screen size:
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		//Centers game window on screen upon launching:
		setLocationRelativeTo(null);
		
		lbl_player = new JLabel();
		myPlayer = new Player(lbl_player);
		img_player = new ImageIcon(getClass().getResource(myPlayer.getFileName()));
		lbl_player.setIcon(img_player);
		lbl_player.setSize(myPlayer.getWidth(), myPlayer.getHeight());
		
		//SCOREBOARD:
		//"SCORE" label:
		lbl_score = new JLabel("SCORE");
		lbl_score.setFont(new Font("Serif", Font.BOLD, GameProperties.SCORE_TXT_SIZE));
		lbl_score.setForeground(Color.white);
		lbl_score.setSize(70, GameProperties.SCORE_TXT_SIZE);
		//Displays current score:
		lbl_currentScore = new JLabel(String.valueOf(myPlayer.getPlayerScore()));
		lbl_currentScore.setFont(new Font("Serif", Font.ITALIC, GameProperties.SCORE_TXT_SIZE));
		lbl_currentScore.setForeground(Color.white);
		lbl_currentScore.setSize(70, GameProperties.SCORE_TXT_SIZE);
		
		lbl_playerLives = new JLabel[3];
		for(int i = 0; i < 3; i++) {
			lbl_playerLives[i] = new JLabel();
			lbl_playerLives[i].setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
			lbl_playerLives[i].setSize(30, 30);
		}
		
		enemies = new Enemy[GameProperties.ENEMY_ROWS][GameProperties.ENEMY_COLS];
		int enemyOffsetX = 0;
		int enemyOffsetY = 0;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			enemyOffsetX = 0;
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j] = new Enemy((0 + enemyOffsetX), (GameProperties.ENEMY_HEIGHT + enemyOffsetY), new JLabel(), myPlayer, enemies, lbl_playerLives);
				img_enemy = new ImageIcon(getClass().getResource(enemies[i][j].getFileName()));
				enemies[i][j].getLbl_enemy().setIcon(img_enemy);
				enemies[i][j].getLbl_enemy().setSize(enemies[i][j].getWidth(), enemies[i][j].getHeight());			
				enemyOffsetX += (enemies[i][j].getWidth() + GameProperties.ENEMY_SPACING);
				// If this is the first enemy in the first row (top left corner):
				if((i == 0) && (j == 0)) {
					// It is the left bumper:
					enemies[i][j].setIsLeftBumper(true);
					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
				}
				// Else, if this is the last enemy in the first row (top right corner):
				else if((i == 0) && (j == GameProperties.ENEMY_COLS - 1)) {
					// It is the right bumper:
					enemies[i][j].setIsRightBumper(true);
					// Get focus first because enemies move to right first:
					enemies[i][j].setHasFocus(true);
					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
					
				}
				// Else, if this enemy is the first in the last row (bottom left corner):
				else if((i == (GameProperties.ENEMY_ROWS - 1)) && (j == 0)) {
					// Set "bumper" flag to true:
					enemies[i][j].setIsBottomBumper(true);
					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
				}
			
				// If this enemy is the last in its row:
				if(j == GameProperties.ENEMY_COLS - 1) {
				// Start positioning enemies on next row:
				enemyOffsetY += enemies[i][j].getHeight();
				}
				
				// If enemy is in bottom row:
				if(i == GameProperties.ENEMY_ROWS - 1) {
					// Set "can shoot" flag to true:
					enemies[i][j].setCanShoot(true);
//					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
				}
				
				// ENEMY'S PROJECTILE:
				enemies[i][j].getEnemyProjectile().setX(enemies[i][j].getX() + 12); // Enemy projectile sprite half width of enemy sprite, so to be centered behind enemy, sprite needs to be offset by half width of enemy
				enemies[i][j].getEnemyProjectile().setY(enemies[i][j].getY());
				// Set size of enemy projectile label to match size of enemy projectile sprite:
				enemies[i][j].getLbl_enemy().setSize(enemies[i][j].getWidth(), enemies[i][j].getHeight());			

				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setSize(GameProperties.PRJCT_ENEMY_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT);
				// Set image icon of enemy projectile:
				img_prjct_enemy = new ImageIcon(getClass().getResource(enemies[i][j].getEnemyProjectile().getFileName()));
				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setIcon(img_prjct_enemy);
			}	
		}
		
		lbl_prjct_player = new JLabel();
		prjct_player = new ProjectilePlayer(lbl_prjct_player, myPlayer, lbl_currentScore);
		img_prjct_player = new ImageIcon(getClass().getResource(prjct_player.getFileName()));
		lbl_prjct_player.setIcon(img_prjct_player);
		lbl_prjct_player.setSize(prjct_player.getWidth(), prjct_player.getHeight());
		prjct_player.setLbl_prjct_player(lbl_prjct_player);
		prjct_player.setEnemies(enemies);
		
		lbl_bg = new JLabel();
		myBg = new Background();
		img_bg = new ImageIcon(getClass().getResource(myBg.getFileName()));
		lbl_bg.setIcon(img_bg);
		lbl_bg.setSize(myBg.getWidth(), myBg.getHeight());

		container1 = getContentPane();
		container1.setBackground(Color.black);
		setLayout(null);
		
		//Set object coordinates:
		myPlayer.setX((GameProperties.SCREEN_WIDTH/2) - myPlayer.getWidth());
		myPlayer.setY(GameProperties.SCREEN_HEIGHT - (myPlayer.getHeight() * 2));
		
		prjct_player.setX(myPlayer.getX() + (prjct_player.getWidth()/2));
		prjct_player.setY(myPlayer.getY());
		
		myBg.setX(0);
		myBg.setY(0);
		
		//Update lbl positions to match stored values:
		lbl_score.setLocation(15, 15);
		lbl_currentScore.setLocation(15, (15 + GameProperties.SCORE_TXT_SIZE));
		lbl_player.setLocation(myPlayer.getX(), myPlayer.getY());
		
		int offset_playerLives = 0;
		for(int i = 0; i < lbl_playerLives.length; i++) {
			lbl_playerLives[i].setLocation(offset_playerLives, (GameProperties.SCREEN_HEIGHT - 70));
			offset_playerLives += 30;
		}
		
		lbl_prjct_player.setLocation(prjct_player.getX(), prjct_player.getY());
		
		int offset_enemyX = 0;
		int offset_enemyY = 0;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			offset_enemyX = 0;
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j].getLbl_enemy().setLocation(enemies[i][j].getX() + offset_enemyX, enemies[i][j].getY() + offset_enemyY);
				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setLocation(enemies[i][j].getEnemyProjectile().getX(), enemies[i][j].getEnemyProjectile().getY());
				offset_enemyX += (enemies[i][j].getWidth() + GameProperties.ENEMY_SPACING);
				if(j == (GameProperties.ENEMY_COLS)) {
					offset_enemyY += enemies[i][j].getHeight();
				}
			}
		}
		
		lbl_bg.setLocation(myBg.getX(), myBg.getY());
		
		//Add objects to screen:
		add(lbl_score);
		add(lbl_currentScore);
		add(lbl_player);
		
		for(int i = 0; i < lbl_playerLives.length; i++) {
			this.add(lbl_playerLives[i]);
		}
		
		add(lbl_prjct_player);
		
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				this.add(enemies[i][j].getLbl_enemy()).setVisible(enemies[i][j].getVisible());
				this.add(enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy()).setVisible(false);
			}
		}
		
		lbl_prjct_player.setVisible(false);
		
		container1.addKeyListener(this);
		container1.setFocusable(true);
		//Action upon hitting close button:
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}
	
	public static void gameOver(GameScreen1 myGameScreen) {
		// Stop the player:
		myGameScreen.myPlayer.stop();
		// Stop each enemy:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				myGameScreen.enemies[i][j].stop();
				myGameScreen.enemies[i][j].setCanShoot(false);
				myGameScreen.enemies[i][j].getEnemyProjectile().stop();
				myGameScreen.enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setVisible(false);	
			}
		}
		// Stop & hide projectiles:
		myGameScreen.prjct_player.stop();
		myGameScreen.lbl_prjct_player.setVisible(false);
		// Display "GAME OVER" msg & exit game:
		JOptionPane.showMessageDialog(null, "GAME OVER");
		System.exit(0);
	}

	public static void main(String args[]) { 
		GameScreen1 myGameScreen = new GameScreen1();
		myGameScreen.setVisible(true); 
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				myGameScreen.enemies[i][j].moveEnemy();
				// If enemy can shoot (in bottom row), start its projectile thread:
				if(myGameScreen.enemies[i][j].getCanShoot()) {
					myGameScreen.enemies[i][j].getEnemyProjectile().startEnemyProjectileThread();
				}
			}
		}
		
		//Infinite loop to continusouly scan for "can shoot" and "gameover" flags:
		while(true) {
			// Continue checking for "game over" flag in enemy objects:
			for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
				for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
					if(myGameScreen.enemies[i][j].getGameOver()) {
						gameOver(myGameScreen);	
					}
					if(myGameScreen.enemies[i][j].getEnemyProjectile().getGameOver()) {
						gameOver(myGameScreen);
					}
					if(myGameScreen.enemies[i][j].getCanShoot()) {
						myGameScreen.enemies[i][j].getEnemyProjectile().startEnemyProjectileThread();
					}
				}				
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Key press & release treated as one
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//Key press only
		int player_x = myPlayer.getX();
		int player_y = myPlayer.getY();
		int enemy_x = 0;
		int enemy_y = 0;
		
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if((myPlayer.getCanMove() == true) && (player_x - GameProperties.PLAYER_STEP) > 0) {
				player_x -= GameProperties.PLAYER_STEP;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if((myPlayer.getCanMove() == true) && (player_x + myPlayer.getWidth() + GameProperties.PLAYER_STEP) < GameProperties.SCREEN_WIDTH) {
				player_x += GameProperties.PLAYER_STEP;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {//Launch player projectile
				if((myPlayer.getCanMove() == true) && (prjct_player.getInMotion() == false)) {
					//Shoot
					prjct_player.launchPlayerProjectile();
				}
		}
		myPlayer.setX(player_x);
		lbl_player.setLocation(myPlayer.getX(), myPlayer.getY());
		
		//Update x-coordinate of player projectile ONLY IF not already in motion:
		if(prjct_player.getInMotion() == false) {
			prjct_player.setX(player_x + (prjct_player.getHeight()/2));
			prjct_player.setY(player_y);
			lbl_prjct_player.setLocation(prjct_player.getX(), prjct_player.getY());
		}

		//Update x-coordinate of enemy projectile ONLY IF not already in motion:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemy_x = enemies[i][j].getX();
				enemy_y = enemies[i][j].getY();

				if(enemies[i][j].getEnemyProjectile().getInMotion() == false) {
					enemies[i][j].getEnemyProjectile().setX(enemy_x);
					enemies[i][j].getEnemyProjectile().setY(enemy_y);
				}
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Key release only	
	}	
}

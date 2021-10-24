import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameScreen1 extends JFrame implements KeyListener{

	private static final long serialVersionUID = -8824959153681940829L;
	
	//Storage classes for game sprites:
	private Player myPlayer;
	private ProjectilePlayer prjct_player;
	private ProjectileEnemy prjct_enemy;
	private Enemy[][] enemies;
	private Background myBg;
	
	//JLabels to display sprites:
	private JLabel lbl_player, lbl_prjct_player, lbl_prjct_enemy, lbl_bg, lbl_score, lbl_currentScore;
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
//		img_enemy = new ImageIcon(getClass().getResource(myEnemy.getFileName()));
//		lbl_enemy.setIcon(img_enemy);
//		lbl_enemy.setSize(myEnemy.getWidth(), myEnemy.getHeight());
		int enemyOffsetX = 0;
		int enemyOffsetY = 0;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			enemyOffsetX = 0;
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j] = new Enemy((0 + enemyOffsetX), (GameProperties.ENEMY_HEIGHT + enemyOffsetY), new JLabel(), myPlayer);
				enemies[i][j].moveEnemy();
				img_enemy = new ImageIcon(getClass().getResource(enemies[i][j].getFileName()));
				enemies[i][j].getLbl_enemy().setIcon(img_enemy);
				enemies[i][j].getLbl_enemy().setSize(enemies[i][j].getWidth(), enemies[i][j].getHeight());			
				enemyOffsetX += (enemies[i][j].getWidth() + GameProperties.ENEMY_SPACING);
				if(j == (GameProperties.ENEMY_COLS - 1)) {
					enemyOffsetY += enemies[i][j].getHeight();
				}
			}
		}
		
		lbl_prjct_player = new JLabel();
		prjct_player = new ProjectilePlayer(lbl_prjct_player, myPlayer, lbl_currentScore);
		img_prjct_player = new ImageIcon(getClass().getResource(prjct_player.getFileName()));
		lbl_prjct_player.setIcon(img_prjct_player);
		lbl_prjct_player.setSize(prjct_player.getWidth(), prjct_player.getHeight());
		prjct_player.setLbl_prjct_player(lbl_prjct_player);
		prjct_player.setEnemies(enemies);
		
		lbl_prjct_enemy = new JLabel();
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				prjct_enemy = new ProjectileEnemy(enemies[i][j], lbl_playerLives);
			}
		}
		img_prjct_enemy = new ImageIcon(getClass().getResource(prjct_enemy.getFileName()));
		lbl_prjct_enemy.setIcon(img_prjct_enemy);
		lbl_prjct_enemy.setSize(prjct_enemy.getWidth(), prjct_enemy.getHeight());
		prjct_enemy.setLbl_prjct_enemy(lbl_prjct_enemy);
		prjct_enemy.setPlayer(myPlayer);
		prjct_enemy.setLbl_player(lbl_player);
		
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
		
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				prjct_enemy.setX(enemies[i][j].getX());
			}
		}
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				prjct_enemy.setY(enemies[i][j].getY());
			}
		}
		
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
				offset_enemyX += (enemies[i][j].getWidth() + GameProperties.ENEMY_SPACING);
				if(j == (GameProperties.ENEMY_COLS)) {
					offset_enemyY += enemies[i][j].getHeight();
				}
			}
		}
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				lbl_prjct_enemy.setLocation(enemies[i][j].getX(), enemies[i][j].getY());
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
				this.add(enemies[i][j].getLbl_enemy()).setVisible(enemies[i][j].getVisible());;
			}
		}
		add(lbl_prjct_enemy);
		lbl_prjct_player.setVisible(false);
		lbl_prjct_enemy.setVisible(false);
		
		container1.addKeyListener(this);
		container1.setFocusable(true);
		//Action upon hitting close button:
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}

	public static void main(String args[]) { 
		GameScreen1 myGameScreen = new GameScreen1();
		myGameScreen.setVisible(true); 
		myGameScreen.prjct_enemy.startEnemyProjectileThread();
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

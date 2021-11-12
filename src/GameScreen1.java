// CURRENT VERSION: 2021-11-07 11:57 PM

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class GameScreen1 extends JFrame implements KeyListener{

	private static final long serialVersionUID = -8824959153681940829L;
	
	//Storage classes for game sprites:
	private Player myPlayer;
	private ProjectilePlayer prjct_player;
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
//					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
				}
				// Else, if this is the last enemy in the first row (top right corner):
				else if((i == 0) && (j == GameProperties.ENEMY_COLS - 1)) {
					// It is the right bumper:
					enemies[i][j].setIsRightBumper(true);
					// Get focus first because enemies move to right first:
					enemies[i][j].setHasFocus(true);
//					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
					
				}
				// Else, if this enemy is the first in the last row (bottom left corner):
				else if((i == (GameProperties.ENEMY_ROWS - 1)) && (j == 0)) {
					// Set "bumper" flag to true:
					enemies[i][j].setIsBottomBumper(true);
//					enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
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
		
		// Start thread for each enemy & its projectile:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j].moveEnemy();
				// If enemy can shoot (in bottom row), start its projectile thread:
				if(enemies[i][j].getCanShoot()) {
					enemies[i][j].getEnemyProjectile().startEnemyProjectileThread();
				}
			}
		}
		
		// Thread to check if game win or lose conditions met:
		Thread thread_main = new Thread (new Runnable() {
			public void run() {
				synchronized(this) {
					while(true) {
						for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
							for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
								
								if(enemies[i][j].getInMotion() && enemies[i][j].getGameOver()) {
									gameOverRoutine();
									break;
								}
								
								if(enemies[i][j].getInMotion() && enemies[i][j].getEnemyProjectile().getGameOver()) {
									gameOverRoutine();	
									break;
								}
								
								if(prjct_player.getInvasionStopped()) {
									gameWonRoutine();
									break;
								}
							}				
						}
					}
				}
			}
		});
		thread_main.start();
		
		//Action upon hitting close button:
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	} // End GameScreen constructor
	
	public void stopGame() {
		// Stop the player & its projectile:
		myPlayer.stop();
		prjct_player.stop();
		
		// Hide player projectile:
		lbl_prjct_player.setVisible(false);

		// Stop enemies & their projectiles, & hide enemy projectiles:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j].stop();
				enemies[i][j].setCanShoot(false);
				enemies[i][j].getEnemyProjectile().setStopProjectile(true);
				enemies[i][j].getEnemyProjectile().getLbl_prjct_enemy().setVisible(false);	
			}
		}
	}
	
	public static void displayScoreboard(ResultSet rs) throws SQLException {
		String[] names = new String[10];
		int[] scores = new int[10];
		int i = 0;
		// While still more records:
		while(rs.next()) {
			// Store name at next index of name array, and score at next index of score array:
			names[i] = rs.getString("name");
			scores[i] = rs.getInt("score");	
			i++;
		}
		JOptionPane.showMessageDialog(null, "Leaderboard: \n 1. " 
				+ names[0] + "  " + scores[0] + "\n2. "
				+ names[1] + "  " + scores[1] + "\n3. "
				+ names[2] + "  " + scores[2] + "\n4. "
				+ names[3] + "  " + scores[3] + "\n5. "
				+ names[4] + "  " + scores[4] + "\n6. "
				+ names[5] + "  " + scores[5] + "\n7. "
				+ names[6] + "  " + scores[6] + "\n8. "
				+ names[7] + "  " + scores[7] + "\n9. "
				+ names[8] + "  " + scores[8] + "\n10. "
				+ names[9] + "  " + scores[9]	
		);
	}
	
	public static void submitScore(String name, int score) {
		//Declare connection & SQL statement
		String playerName = name;
		int playerScore = score;
		Connection conn = null;
		Statement stmt = null;
		
		try {
			Class.forName("org.sqlite.JDBC"); //JDBC -> Java DataBase Connectivity
			System.out.println("Database Driver Loaded");
			
			String dbURL = "jdbc:sqlite:space-invaders.db"; // db file created in bin folder
			conn = DriverManager.getConnection(dbURL);
			
			if(conn != null) {
				//If db connection was successful:
				System.out.println("Connected to database");
				conn.setAutoCommit(false);
				
				DatabaseMetaData dm = (DatabaseMetaData) conn.getMetaData();
				System.out.println("Driver name: " + dm.getDriverName());
				System.out.println("Driver version: " + dm.getDriverVersion());
				System.out.println("Product name: " + dm.getDatabaseProductName());
				System.out.println("Product version: " + dm.getDatabaseProductVersion());
				
				// Create "SCOREBOARD" table:
				stmt = conn.createStatement();
				
				String sql = "CREATE TABLE IF NOT EXISTS SCOREBOARD" +
							 "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + //AUTOINCREMENT requires INTEGER (not INT)
							 "NAME TEXT NOT NULL," +
							 "SCORE INT NOT NULL)";
				stmt.executeUpdate(sql);
				conn.commit();
				System.out.println("Table Created Successfully");
				
				sql = "INSERT INTO SCOREBOARD (NAME, SCORE) VALUES (?, ?)";
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, playerName);
				pstmt.setInt(2, playerScore);
				pstmt.executeUpdate();
				conn.commit();
				
				// Select 10 highest scores (& corresponding player name) from db:
				ResultSet rs = stmt.executeQuery("SELECT * FROM SCOREBOARD ORDER BY SCORE DESC LIMIT 10");
				displayScoreboard(rs); // Displays top scores
				rs.close(); //close results-set to free resources
			
				conn.close(); //closes connection to db file	
			}
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	}
	
	public void gameOverRoutine() {
		stopGame();
		// Display "GAME OVER" msg:
		JOptionPane.showMessageDialog(null, "GAME OVER");
		String playerName = JOptionPane.showInputDialog("Please enter your name: ");
		while(playerName.trim().isEmpty()) {
			playerName = JOptionPane.showInputDialog("Please enter your name: ");
		}
		int playerScore = myPlayer.getPlayerScore();
		submitScore(playerName, playerScore);
		System.exit(0);
	}
	
	public int awardLifeBonus(int score, int lives) {
		int preBonusScore = score;
		int numLivesRemaining = lives;
		int bonusAwarded = 0;

		// Adjust bonus pts awarded based on num lives remaining:
		if(numLivesRemaining == 3) {
			bonusAwarded = GameProperties.NO_HIT_BONUS;
		}
		else {
			bonusAwarded = numLivesRemaining * GameProperties.PTS_PER_BONUS_LIFE;
		}
		
		return (preBonusScore + bonusAwarded);
	}
	
	public void gameWonRoutine() {
		stopGame();
		// Display congratulatory msg:
		JOptionPane.showMessageDialog(null, "YOU STOPPED THE IVASION!");
		String playerName = JOptionPane.showInputDialog("Please enter your name: ");
		
		while(playerName.trim().isEmpty()) {
			playerName = JOptionPane.showInputDialog("Please enter your name: ");
		}
		
		int playerScore = awardLifeBonus(myPlayer.getPlayerScore(), myPlayer.getPlayerLives());
		submitScore(playerName, playerScore);
		System.exit(0);
	}
	
//=====================================================================================================
//	PROGRAM MAIN
//=====================================================================================================
	
	public static void main(String args[]) { 
		GameScreen1 myGameScreen = new GameScreen1();
		myGameScreen.setVisible(true); 
	}
	
//=====================================================================================================
//	KEYBOARD CONTROLS
//=====================================================================================================
	
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
	
} // End GameScreen1 Class

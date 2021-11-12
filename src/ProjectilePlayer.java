import javax.swing.JLabel;

public class ProjectilePlayer extends Sprite implements Runnable {
	
	//Attributes:
	private Thread thread;
	private JLabel lbl_prjct_player, lbl_currentScore;
	private Enemy[][] enemies;
	private Boolean collision, keyPressed, invasionStopped;
	private Player myPlayer;
	private int enemyCount;
	
	public int getEnemyCount() {
		return enemyCount;
	}
	public void setEnemyCount(int enemyCount) {
		this.enemyCount = enemyCount;
	}
	public Boolean getCollision() {return collision;}
	public void setCollision(Boolean collision) {this.collision = collision;}
	
	public Boolean getKeyPressed() {return keyPressed;}
	public void setKeyPressed(Boolean keyPressed) {this.keyPressed = keyPressed;}
	
	public JLabel getLbl_currentScore() {return lbl_currentScore;}
	public void setLbl_currentScore(JLabel lbl_currentScore) {this.lbl_currentScore = lbl_currentScore;}
	
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setEnemies(Enemy[][] temp) {this.enemies = temp;}
	public void setLbl_prjct_player(JLabel temp) {this.lbl_prjct_player = temp;}
	
	public Boolean getInvasionStopped() {return invasionStopped;}
	public void setInvasionStopped(Boolean boardCleared) {this.invasionStopped = boardCleared;}
	
	//Constructors:
	//Default
	public ProjectilePlayer() {
		super(GameProperties.PRJCT_PLAYER_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT, "img_prjct_player.png", false, false, false);
		this.collision = false;
		this.inMotion = false;
		this.keyPressed = false;
		this.enemyCount = GameProperties.ENEMY_COUNT;
		this.invasionStopped = false;
	}
	
	//Secondary
	public ProjectilePlayer(JLabel temp1, Player temp2, JLabel temp3) {
		super(GameProperties.PRJCT_PLAYER_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT, "img_prjct_player.png", false, false, false);
		this.lbl_prjct_player = temp1;
		this.myPlayer = temp2;
		this.lbl_currentScore = temp3;
		this.collision = false;
		this.inMotion = false;
		this.keyPressed = false;
		this.enemyCount = GameProperties.ENEMY_COUNT;
		this.invasionStopped = false;
	}
	
	//Other methods:
	@Override
	public void hide() {
		this.lbl_prjct_player.setVisible(false);
	}
	@Override
	public void show() {
		this.lbl_prjct_player.setVisible(true);
	}
	
	private void resetPlayerProjectile() {
		//Resets x,y coordinates & label of player projectile:
		this.setX(myPlayer.getX());
		this.setY(myPlayer.getY());
		this.lbl_prjct_player.setLocation(this.getX(), this.getY());
		//Set motion flag to false:
		this.setInMotion(false);
		//Set collision flag to false:
		this.setCollision(false);
		//Hides the label:
		this.hide();
		this.setKeyPressed(false);
	}
	
	public void updatePlayerScore() {
		myPlayer.setPlayerScore(myPlayer.getPlayerScore() + GameProperties.PTS_PER_ENEMY);
		this.getLbl_currentScore().setText(String.valueOf(myPlayer.getPlayerScore()));
//		System.out.println(myPlayer.getPlayerScore());
	}
	
	public void reassignRightBumper(Enemy enemy) {
		// Store enemy array in "enemies" variable:
		Enemy[][] enemies = enemy.getEnemies();
		int currentRightBumperCol;
		Boolean enemyHadFocus = false;
		// Get this enemy's position (row & column #):
		OUTER:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				
				// Find this enemy:
				if(enemies[i][j] == enemy) {
					currentRightBumperCol = j;
					if(enemies[i][j].getHasFocus()) {
						enemies[i][j].setHasFocus(false);
						enemyHadFocus = true;
					}
					
					// Find the next in-motion (not destroyed) enemy to left of old right bumper:
					for(j = currentRightBumperCol - 1; j >= 0; j--) {
						
						// If enemy in motion (not destroyed)						
						if(enemies[i][j].getInMotion()) {
							
							// Becomes the next right bumper:
							enemies[i][j].setIsRightBumper(true);
							if(enemyHadFocus) {
								enemies[i][j].setHasFocus(true);
							}
//							enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
							break OUTER;
						}
					}
				}
			}
		}
	}
	
	public void reassignBottomBumper(Enemy enemy) {
		// Store enemy array in "enemies" variable:
		Enemy[][] enemies = enemy.getEnemies();
		int currentBottomBumperRow = 0;
		int currentBottomBumperCol = 0;
		Boolean enemyHadFocus = false;
		// Get this enemy's position (row & column #):
		OUTER:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				
				//Find this enemy:
				if(enemies[i][j] == enemy) {
					currentBottomBumperRow = i;
					currentBottomBumperCol = j;
				
					// Set right bumper flag to false:
					if(enemies[i][j].getHasFocus()) {
						enemies[i][j].setHasFocus(false);
						enemyHadFocus = true;
					}
					
					// Find the next in-motion (not destroyed) enemy to left of old right bumper:
					for(i = currentBottomBumperRow; i >= 0; i--) {
						for(j = currentBottomBumperCol; j < GameProperties.ENEMY_COLS; j++) {
							
							// If enemy in motion (not destroyed)						
							if(enemies[i][j].getInMotion()) {
								
								// Becomes the next right bumper:
								enemies[i][j].setIsBottomBumper(true);
//								enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
								if(enemyHadFocus) {
									enemies[i][j].setHasFocus(true);
								}
								break OUTER;
							}
						}
						currentBottomBumperCol = 0;
					}
				}
			}
		}
	}
	
	public void reassignLeftBumper(Enemy enemy) {
		// Store enemy array in "enemies" variable:
		Enemy[][] enemies = enemy.getEnemies();
		//int currentRightBumperRow = 0;
		int currentLeftBumperCol;
		boolean enemyHadFocus = false;
		// Get this enemy's position (row & column #):
		OUTER:
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				// If right bumper found:
				if(enemies[i][j] == enemy) {
					currentLeftBumperCol = j;
					if(enemies[i][j].getHasFocus()) {
						enemies[i][j].setHasFocus(false);
						enemyHadFocus = true;
					}
					// Find the next in-motion (not destroyed) enemy to left of old right bumper:
					for(j = currentLeftBumperCol + 1; j < GameProperties.ENEMY_COLS; j++) {
						// If enemy in motion (not destroyed)						
						if(enemies[i][j].getInMotion()) {
							// Becomes the next right bumper:
							enemies[i][j].setIsLeftBumper(true);
//							enemies[i][j].getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_playerLives.png")));
							if(enemyHadFocus) {
								enemies[i][j].setHasFocus(true);
							}
							break OUTER;
						}
					}
				}
			}
		}
	}
	
	private void increaseEnemySpeed(int count) {
		switch(count) {
			case 40: GameProperties.ENEMY_STEP += 5; break;
			case 25: GameProperties.ENEMY_STEP += 5; break;
			case 10: GameProperties.ENEMY_STEP += 5; break;
			case 5: GameProperties.ENEMY_STEP += 5; break;
			case 1: GameProperties.ENEMY_STEP += 15; break;
		}	
	}
	
	private void updateEnemyCount() {
		GameProperties.ENEMY_COUNT -= 1;
	}
	
	public void reassignCanShoot(Enemy enemy) {
		Enemy[][] enemies = enemy.getEnemies();
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j< GameProperties.ENEMY_COLS; j++) {
				if(enemies[i][j] == enemy) {
					enemy.setCanShoot(false);
					// If not first row (prevents flag reassignment going out of bounds):
					if((i - 1) >= 0) {
						// Sets "can shoot" flag of enemy directly behind destroyed enemy to true:
						enemies[i - 1][j].setCanShoot(true);
						enemies[i - 1][j].getEnemyProjectile().startEnemyProjectileThread();
					}
				}
			}
		}		
	}

	private void destroyEnemy(Enemy enemy) {
		enemy.stop();
		enemy.setCanShoot(false);
		enemy.getHitbox().setSize(0, 0);
		enemy.hide();
		this.updatePlayerScore();
		this.updateEnemyCount();
		
		if(GameProperties.ENEMY_COUNT == 0) {
			this.setInvasionStopped(true);
		}
		
		// Reassign "can shoot" flag:
		reassignCanShoot(enemy);
		
		// If enemy has bumper flag set, reassign it:
		if(enemy.getIsRightBumper()) {
			enemy.setIsRightBumper(false);
			reassignRightBumper(enemy);	
		}
		
		if(enemy.getIsBottomBumper()) {
			enemy.setIsBottomBumper(false);
			reassignBottomBumper(enemy);
		}
		
		if(enemy.getIsLeftBumper()) {
			enemy.setIsLeftBumper(false);
			reassignLeftBumper(enemy);
		}
		
		increaseEnemySpeed(GameProperties.ENEMY_COUNT);
	}
	
	private Boolean detectEnemyCollision() {
		//Iterate through enemy array and test each enemy's hitbox for collision w/ player projectile hitbox:
		Boolean collisionDetected = false;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				//If player collision detected:
				if(this.hitbox.intersects(enemies[i][j].getHitbox().getX(), (enemies[i][j].getHitbox().getY() - enemies[i][j].getHeight() ), enemies[i][j].getHitbox().getWidth(), enemies[i][j].getHitbox().getHeight())) {
					collisionDetected = true;
					resetPlayerProjectile();
					myPlayer.setAlienFlag(i, j, false);
					destroyEnemy(enemies[i][j]);	
				}
			}
		}
		return collisionDetected;
	}
	
	public void launchPlayerProjectile() {
		this.setKeyPressed(true);
		this.thread = new Thread(this, "Enemy Projectile Thread");
		this.thread.start();
	}
	
	@Override
	public void run() {
		while(this.getKeyPressed() == true) {
			int thread_x = this.getX();
			int thread_y = this.getY();
			while(thread_y > 0 && this.getCollision() == false) {
				this.move();
				this.show(); //overridden to do same as lbl_prjct_enemy.setVisible(true);
				thread_y -= GameProperties.PRJCT_PLAYER_STEP;
				this.setX(thread_x);
				this.setY(thread_y);
				this.lbl_prjct_player.setLocation(this.x, this.y);
				if(this.detectEnemyCollision()) {
					break;
				}
				else if(thread_y <= 0) {
					resetPlayerProjectile();
					break;
				}
				try {
					Thread.sleep(25);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
}

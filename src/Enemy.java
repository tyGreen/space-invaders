import javax.swing.JLabel;

public class Enemy extends Sprite implements Runnable {
	
	private Thread thread;
	private JLabel lbl_enemy;
	private Player myPlayer;
	private ProjectileEnemy enemyProjectile;
	private Boolean isRightBumper, isLeftBumper, isBottomBumper, hasFocus;
		// Flag to track if enemy's position within array is located on the perimeter 
		// (i.e. will it be one of the first enemies to reach a wall or the ground?)
	private Enemy[][] enemies;
	
	public ProjectileEnemy getEnemyProjectile() {return enemyProjectile;}
	public void setEnemyProjectile(ProjectileEnemy enemyProjectile) {this.enemyProjectile = enemyProjectile;}
	
	public JLabel getLbl_enemy() {return lbl_enemy;}
	public void setLbl_enemy(JLabel lbl_enemy) {this.lbl_enemy = lbl_enemy;}
	
	public Thread getThread() {return thread;}
	public void setThread(Thread thread) {this.thread = thread;}
	
	public Boolean getHasFocus() {
		return hasFocus;
	}
	public void setHasFocus(Boolean hasFocus) {
		this.hasFocus = hasFocus;
	}
	public Boolean getIsRightBumper() {
		return isRightBumper;
	}
	public void setIsRightBumper(Boolean isRightBumper) {
		this.isRightBumper = isRightBumper;
	}
	public Boolean getIsLeftBumper() {
		return isLeftBumper;
	}
	public void setIsLeftBumper(Boolean isLeftBumper) {
		this.isLeftBumper = isLeftBumper;
	}
	public Boolean getIsBottomBumper() {
		return isBottomBumper;
	}
	public void setIsBottomBumper(Boolean isBottomBumper) {
		this.isBottomBumper = isBottomBumper;
	}
	//Constructors:
	public Enemy() {
		super(GameProperties.ENEMY_WIDTH, GameProperties.ENEMY_HEIGHT, "img_invader1.gif", true, true, false);
		this.enemyProjectile = new ProjectileEnemy();
		this.isRightBumper = false;
		this.isLeftBumper = false;
		this.isBottomBumper = false;
		this.hasFocus = false;
	}
	
	public Enemy(int temp1, int temp2, JLabel temp3, Player temp4, Enemy[][] temp5) {
		super(GameProperties.ENEMY_WIDTH, GameProperties.ENEMY_HEIGHT, "img_invader1.gif", true, true, false);
		this.x = temp1;
		this.y = temp2;
		this.lbl_enemy = temp3;
		this.myPlayer = temp4;
		this.enemies = temp5;
		this.enemyProjectile = new ProjectileEnemy();
		this.isRightBumper = false;
		this.isLeftBumper = false;
		this.isBottomBumper = false;
		this.hasFocus = false;
	}
	
	//Other methods:
	@Override
	public void hide() {this.lbl_enemy.setVisible(false);}
	
	@Override
	public void show() {this.lbl_enemy.setVisible(true);}
	
	public void display() {System.out.println("x,y / visible?: " + this.x + "," + this.y + " / " + this.visible);}

	public void moveEnemy() {
		this.thread = new Thread(this, "Enemey Thread");
		this.thread.start(); //Triggers/starts the thread
	}
	
	public void moveEnemiesRight(Enemy[][] array) {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				array[i][j].setX(array[i][j].getX() + GameProperties.ENEMY_STEP);
				array[i][j].getLbl_enemy().setLocation(array[i][j].getX(), array[i][j].getY());
			}
		}
	}
	
	public void moveEnemiesDown(Enemy[][] array) {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				array[i][j].setY(array[i][j].getY() + array[i][j].getHeight());
				array[i][j].getLbl_enemy().setLocation(array[i][j].getX(), array[i][j].getY());
			}
		}
	}
	
	public void moveEnemiesLeft(Enemy[][] array) {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				array[i][j].setX(array[i][j].getX() - GameProperties.ENEMY_STEP);
				array[i][j].getLbl_enemy().setLocation(array[i][j].getX(), array[i][j].getY());
			}
		}
	}

	@Override
	public void run() { 
		// Master loop - when this ends, thread dies:
		while(this.getInMotion()) {
			// While enemy has focus:
				// This loop keeps all (non-destroyed) enemies moving by running four inner loops
				// in sequence depending on current flag set, & so long as at least one enemy has 
				// the focus (at least one enemy left on screen)
			while(this.getHasFocus()) { 
				
				// While enemy has RIGHT bumper flag set:
				if(this.getIsRightBumper()) {
					
					// While RIGHT bumper has room to move right:
					while( (this.getX() + this.getWidth() + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
						// Move all enemies right:
						moveEnemiesRight(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					// Focus shifts from this enemy to enemy with BOTTOM bumper flag set:
					this.setHasFocus(false);
					// Iterate through array to find enemy with BOTTOM bumper flag set:
					for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
						for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
							if(enemies[i][j].getIsBottomBumper()) {
								enemies[i][j].setHasFocus(true);
							}
						}
					}
				}
				
				// While enemy has BOTTOM bumper flag set:
				if(this.getIsBottomBumper()) {
					// While BOTTOM bumper still has room to move down:
					if((this.getY() + this.getHeight()) <= myPlayer.getY()) {
						moveEnemiesDown(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
						this.setHasFocus(false);
						// Determine whether to shift focus to right bumper or left bumper:
						// Iterate through array to find right bumper:
						OUTER:
						for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
							for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
								if(enemies[i][j].getIsRightBumper()) {
									// Right bumper found. Test if right step would put it out of bounds:
									if( (enemies[i][j].getX() + enemies[i][j].getWidth() + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
										// If not, give right bumper the focus next:
										enemies[i][j].setHasFocus(true);
										break OUTER;
									}
									// Otherwise we must be at right edge of screen & must now move left:
									else {
										// Left bumper will get focus next. Iterate through array again to find it:
										for(i = 0; i < GameProperties.ENEMY_ROWS; i++) {
											for(j = 0; j < GameProperties.ENEMY_COLS; j++) {
												if(enemies[i][j].getIsLeftBumper()) {
													// Once found, give it focus:
													enemies[i][j].setHasFocus(true);
													break OUTER;
												}
											}
										}
									}
								}
							}
						}	
					}
				}
				
				// While enemy has LEFT bumper flag set:
				if(this.getIsLeftBumper()) {
					// While LEFT bumper has room to move left:
					while( (this.getX() - GameProperties.ENEMY_STEP) > 0) {
						// Move all enemies down
						moveEnemiesLeft(this.enemies);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					// Focus shifts from this enemy to enemy with BOTTOM bumper flag set:
					this.setHasFocus(false);
					// Iterate through array to find enemy with BOTTOM bumper flag set:
					for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
						for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
							if(enemies[i][j].getIsBottomBumper()) {
								enemies[i][j].setHasFocus(true);
							}
						}
					}
				}		
			}
			
			// While this enemy DOESN'T have focus:
			while(!this.getHasFocus()) {
				// Loop to keep enemy threads alive when they don't have focus.
				// Will break out of this loop if focus flag is set or enemy is destroyed
				// (no longer in motion)
				try {
					Thread.sleep(200);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
}
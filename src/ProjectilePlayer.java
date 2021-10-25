import javax.swing.JLabel;

public class ProjectilePlayer extends Sprite implements Runnable {
	
	//Attributes:
	private Thread thread;
	private JLabel lbl_prjct_player, lbl_currentScore;
	private Enemy[][] enemies;
	private Boolean collision, keyPressed;
	private Player myPlayer;
	
	public Boolean getCollision() {return collision;}
	public void setCollision(Boolean collision) {this.collision = collision;}
	
	public Boolean getKeyPressed() {return keyPressed;}
	public void setKeyPressed(Boolean keyPressed) {this.keyPressed = keyPressed;}
	
	public JLabel getLbl_currentScore() {return lbl_currentScore;}
	public void setLbl_currentScore(JLabel lbl_currentScore) {this.lbl_currentScore = lbl_currentScore;}
	
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setEnemies(Enemy[][] temp) {this.enemies = temp;}
	public void setLbl_prjct_player(JLabel temp) {this.lbl_prjct_player = temp;}
	
	//Constructors:
	//Default
	public ProjectilePlayer() {
		super(GameProperties.PRJCT_PLAYER_WIDTH, GameProperties.PRJCT_PLAYER_HEIGHT, "img_prjct_player.png", false, false, false);
		this.collision = false;
		this.inMotion = false;
		this.keyPressed = false;
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
	
//		this.setCollision(false);
//		enemyHit.getLbl_enemy().setIcon(new ImageIcon(getClass().getResource("img_explosion_enemy.gif")));
//		timer = new Timer(600, new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				enemyHit.getLbl_enemy().setVisible(false);
//			}
//		});
//		timer.start();
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
		System.out.println(myPlayer.getPlayerScore());
	}
	
	private void destroyEnemy(Enemy enemy) {
		enemy.getThread().interrupt();
		enemy.getHitbox().setSize(0, 0);
		enemy.stop();
		enemy.hide();
		this.updatePlayerScore();
		if(enemy.getThread().isAlive()) {
			System.out.println("Thread is alive");
		}
		else {
			System.out.println("Thread is NOT alive...");

		}
	}
	
	private Boolean detectEnemyCollision() {
		//Iterate through enemy array and test each enemy's hitbox for collision w/ player projectile hitbox:
//		OUTER_LOOP:
		Boolean collisionDetected = false;
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				//If player collision detected:
				if(this.hitbox.intersects(enemies[i][j].getHitbox())) {
					collisionDetected = true;
					resetPlayerProjectile();
					destroyEnemy(enemies[i][j]);	
				}
			}
		}
		return collisionDetected;
	}
	
	
//	public void launchPlayerProjectile() {
//		//If projectile not already in motion:
//		if(this.getInMotion() == false) {
//			//Move projectile toward enemies while it's collision-free
//			//& is still has room to move (within bounds):
//			Boolean enemyCollisionDetected;
//			do {
//				//Move projectile toward enemies:
//				this.setY(this.getY() - GameProperties.PRJCT_PLAYER_STEP);
//				//Update image label:
//				lbl_prjct_player.setLocation(this.getX(), this.getY());
//				//Check for collision (determines whether loop continues running):
//				enemyCollisionDetected = this.detectEnemyCollision();
//			}
//			while((!enemyCollisionDetected) && (this.getY() - GameProperties.PRJCT_PLAYER_STEP) < 0);
//		}		
//	}
	
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

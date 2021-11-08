import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class ProjectileEnemy extends Sprite implements Runnable {
	
	//Attributes:
	private Thread thread1;
	private JLabel lbl_prjct_enemy, lbl_player;
	private Player myPlayer;
	private Timer tmr_regeneratePlayer;
	private Enemy myEnemy;
	private Boolean collision, gameOver;
	private JLabel[] lbl_playerLives;
	
	public Boolean getCollision() {return collision;}
	public void setCollision(Boolean temp) {this.collision = temp;}
	
	public Boolean getGameOver() {
		return gameOver;
	}
	public void setGameOver(Boolean gameOver) {
		this.gameOver = gameOver;
	}
	
	public JLabel getLbl_prjct_enemy() {
		return lbl_prjct_enemy;
	}
	public void setLbl_prjct_enemy(JLabel lbl_prjct_enemy) {
		this.lbl_prjct_enemy = lbl_prjct_enemy;
	}
	
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setPlayer(Player temp) {this.myPlayer = temp;}
	public void setLbl_player(JLabel temp) {this.lbl_player = temp;}

	//Constructors:
	//Default
	public ProjectileEnemy() {
		super(GameProperties.PRJCT_ENEMY_WIDTH, GameProperties.PRJCT_ENEMY_HEIGHT, "img_prjct_enemy.png", false, false, false);
		this.collision = false;
		this.gameOver = false;
		this.lbl_prjct_enemy = new JLabel();
	}
	
	public ProjectileEnemy(Enemy temp1, JLabel[] temp2, Player temp3) {
		super(GameProperties.PRJCT_ENEMY_WIDTH, GameProperties.PRJCT_ENEMY_HEIGHT, "img_prjct_enemy.png", false, false, false);
		this.myEnemy = temp1;
		this.lbl_playerLives = temp2;
		this.myPlayer = temp3;
		this.collision = false;
		this.gameOver  = false;
		this.lbl_prjct_enemy = new JLabel();
	}
	
	//Other methods:
	@Override
	public void hide() {
		lbl_prjct_enemy.setVisible(false);
	}
	@Override
	public void show() {
		lbl_prjct_enemy.setVisible(true);
	}
	
	private boolean detectPlayerCollision() {
		if(this.hitbox.intersects(myPlayer.getHitbox())) {
			this.setCollision(true);
			this.hide();
			this.returnToEnemy();
			
			// Disable player's hitbox:
			myPlayer.getHitbox().setSize(0, 0);
			
			// Disable player's ability to move:
			myPlayer.setCanMove(false);
			
			// Set player sprite to explosion:
			lbl_player.setIcon(new ImageIcon(getClass().getResource("img_explosion_player.gif")));
			
			// After delay of 1.5 seconds:
			tmr_regeneratePlayer = new Timer(1500, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					// Restore player hitbox:
					myPlayer.getHitbox().setSize(myPlayer.getWidth(), myPlayer.getHeight());
					// Restore player movement:
					myPlayer.setCanMove(true);
					// Restore player sprite:
					lbl_player.setIcon(new ImageIcon(getClass().getResource("img_player.png")));	
				}
			});
			tmr_regeneratePlayer.start();	
		}
		return true;
	}
	
	public void returnToEnemy() {
		this.setX(this.myEnemy.getX());
		this.setY(this.myEnemy.getY());
		this.getLbl_prjct_enemy().setLocation(this.getX(), this.getY());
	}
	
	public void startEnemyProjectileThread() {
		thread1 = new Thread(this, "Enemy Projectile Thread");
		thread1.start();
	}
	
	@Override
	public void run() {
		while(this.myEnemy.getInMotion()) {
			while(this.myEnemy.getCanShoot()) {
				// Generate two random ints:
				int randomInt1 = (int)(Math.random() * 999999 + 1);
				int randomInt2 = (int)(Math.random() * 999999 + 1);
				// If ints match:
				if(randomInt1 == randomInt2) {
					// While project within bounds && collision flag is false:
					while((this.getY() < GameProperties.SCREEN_HEIGHT) && this.getCollision() == false) {
						// Launch projectile toward player:
						this.move();
						this.show(); //overridden to do same as lbl_prjct_enemy.setVisible(true);
						this.setY(this.getY() + GameProperties.PRJCT_ENEMY_STEP);
						lbl_prjct_enemy.setLocation(this.x, this.y);
						if(detectPlayerCollision()) {
							if(myPlayer.getPlayerLives() > 0) {
								myPlayer.setPlayerLives(myPlayer.getPlayerLives() - 1);
							}
							lbl_playerLives[myPlayer.getPlayerLives()].setVisible(false);
							System.out.println("Player Lives Remaining: " + myPlayer.getPlayerLives());
							
							// If player lives = 0:
							if(myPlayer.getPlayerLives() == 0) {
								// Game over:
								this.setGameOver(true);
							}
							break;
						}
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
				}
			}
		}
	}
}
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class ProjectileEnemy extends Sprite implements Runnable {
	
	//Attributes:
	private Thread thread;
	private JLabel lbl_prjct_enemy, lbl_player;
	private Player myPlayer;
	private Timer tmr_regeneratePlayer;
	private Enemy myEnemy;
	private Boolean stopProjectile, gameOver;
	private JLabel[] lbl_playerLives;
	private Enemy[][] enemies;
	
	public Boolean getStopProjectile() {return stopProjectile;}
	public void setStopProjectile(Boolean temp) {this.stopProjectile = temp;}
	
	public Boolean getGameOver() {return gameOver;}
	public void setGameOver(Boolean gameOver) {this.gameOver = gameOver;}
	
	public JLabel getLbl_prjct_enemy() {return lbl_prjct_enemy;}
	public void setLbl_prjct_enemy(JLabel lbl_prjct_enemy) {this.lbl_prjct_enemy = lbl_prjct_enemy;}
	
	public Thread getThread() {return thread;}
	public void setThread(Thread thread) {this.thread = thread;}
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setPlayer(Player temp) {this.myPlayer = temp;}
	public void setLbl_player(JLabel temp) {this.lbl_player = temp;}
	public void setEnemy(Enemy temp) {this.myEnemy = temp;}

	//Constructors:
	//Default
	public ProjectileEnemy() {
		super(GameProperties.PRJCT_ENEMY_WIDTH, GameProperties.PRJCT_ENEMY_HEIGHT, "img_prjct_enemy.png", false, false, false);
		this.stopProjectile = false;
		this.gameOver = false;
		this.lbl_prjct_enemy = new JLabel();
	}
	
	public ProjectileEnemy(Enemy temp1, JLabel[] temp2, Player temp3, Enemy[][] temp4) {
		super(GameProperties.PRJCT_ENEMY_WIDTH, GameProperties.PRJCT_ENEMY_HEIGHT, "img_prjct_enemy.png", false, false, false);
		this.myEnemy = temp1;
		this.lbl_playerLives = temp2;
		this.myPlayer = temp3;
		this.enemies = temp4;
		this.stopProjectile = false;
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
	
	public void launchProjectile() {
		// Make projectile (label) visisble:
		this.lbl_prjct_enemy.setVisible(true);
		// Set its "in motion" flag to true (required to prevent X-coordinate from syncing w/ enemies while launching):
		this.setInMotion(true);
		// Update projectile's Y-coordinate (+20px south):
		this.setY(this.y + GameProperties.PRJCT_PLAYER_STEP);
		// Update label to match new coordinate(s):
		this.lbl_prjct_enemy.setLocation(this.x, this.y);
	}
	
	public void resetProjectile() {
		// Hide the projectile (label):
		this.lbl_prjct_enemy.setVisible(false);
		// Set "in motion" flag to false (important so coordinates can sync w/ corresponding enemy):
		this.setInMotion(false);
		this.setX(this.myEnemy.getX());
		this.setY(this.myEnemy.getY());
		this.lbl_prjct_enemy.setLocation(this.x, this.y);
	}
	
	public boolean collisionDetected() {
		if(this.hitbox.intersects(this.myPlayer.getHitbox())) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void subtractPlayerLife() {
		this.myPlayer.setPlayerLives(this.myPlayer.getPlayerLives() - 1);
		
		// Update player lives label to reflect current # player lives remaining:
		this.lbl_playerLives[this.myPlayer.getPlayerLives()].setVisible(false);
		
		// If player lives hit 0:
		if(this.myPlayer.getPlayerLives() == 0) {
			// Disable enemy projectile hitbox:
			this.getHitbox().setSize(0, 0);
			// Disable player hitbox:
			myPlayer.getHitbox().setSize(0, 0);
			// Set the enemy projectile's "game over" flag to true for detection by game screen:
			this.setGameOver(true);
		}
	}
	
	public void resetPlayer() {
		// Disable player hitbox:
		myPlayer.getHitbox().setSize(0, 0);
		// Disable player movement (& by extension, ability to shoot):
		myPlayer.setCanMove(false);
		// Set player icon to explosion:
		myPlayer.getLbl_player().setIcon(new ImageIcon(getClass().getResource("img_explosion_player.gif")));
		// After delay of 1.5 seconds:
		tmr_regeneratePlayer = new Timer(1500, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Restore the hitbox, key controls, & icon:
				myPlayer.getHitbox().setSize(myPlayer.getWidth(), myPlayer.getHeight());
				myPlayer.setCanMove(true);
				myPlayer.getLbl_player().setIcon(new ImageIcon(getClass().getResource("img_player.png")));	
			}
		});
		tmr_regeneratePlayer.start();
	}
	
	public boolean enemyWillShoot() {
		// Generates two random integers between 1 & GameProperties.ENEMY_SHOT_FREQ:
		int randomInt1 = (int)(Math.random() * GameProperties.PRJCT_ENEMY_SHOT_FREQ + 1);
		int randomInt2 = (int)(Math.random() * GameProperties.PRJCT_ENEMY_SHOT_FREQ + 1);
		// If integers match:
		if(randomInt1 == randomInt2) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void playSoundEffect() {
		try {
			// Retrieve sound file & store in var:
			URL url = this.getClass().getClassLoader().getResource("sfx_playerExplosion.wav");

			// Open audio input stream:
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
 
			// Retrieve sound clip resource:
			Clip clip = AudioSystem.getClip();
 
			// Open clip and load sample from input stream:
			clip.open(audioIn);
			clip.start();
	         
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	  }
	
	public void startEnemyProjectileThread() {
		thread = new Thread(this, "Enemy Projectile Thread");
		thread.start();
	}
	
	@Override
	public void run() {
		
		// While the enemy that owns this projectile is moving (not destroyed) && can shoot:
		while(this.myEnemy.getInMotion() && this.myEnemy.getCanShoot()) {
			
			// If RNG determines enemy will shoot:
			if(enemyWillShoot()) {
				
				// While the projectile has room to move south without going out of bounds:
				while(!this.getStopProjectile() && (this.y + GameProperties.PRJCT_ENEMY_STEP) < GameProperties.SCREEN_HEIGHT) {
					launchProjectile();
					if(collisionDetected()) {
						this.playSoundEffect();
						subtractPlayerLife();
						resetProjectile();
						resetPlayer();
						break;
					}
					try {
						Thread.sleep(100);
					}
					catch(Exception e) {
						
					}
				}
				
				// When projectile >= screen height (goes out of bounds):
				// Reset location of enemy projectile back to its enemy's current location:
				resetProjectile();	
				try {
					Thread.sleep(200);
				}
				catch(Exception e) {
					
				}
			}
			try {
				Thread.sleep(200);
			}
			catch(Exception e) {
				
			}
		}	
		
	} // End run()
	
} // End ProjectileEnemy.java
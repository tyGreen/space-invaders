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
	private Boolean collision;
	private JLabel[] lbl_playerLives;
	
	public Boolean getCollision() {return collision;}
	public void setCollision(Boolean temp) {this.collision = temp;}
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setPlayer(Player temp) {this.myPlayer = temp;}
	public void setLbl_prjct_enemy(JLabel temp) {this.lbl_prjct_enemy = temp;}
	public void setLbl_player(JLabel temp) {this.lbl_player = temp;}
//	public void setMyEnemy(Enemy temp) {this.myEnemy = temp;}

	//Constructors:
	//Default
	public ProjectileEnemy() {
		super(25, 30, "img_prjct_enemy.png", false, false, false);
		this.collision = false;
	}
	
	public ProjectileEnemy(Enemy temp1, JLabel[] temp2) {
		super(25, 30, "img_prjct_enemy.png", false, false, false);
		this.myEnemy = temp1;
		this.lbl_playerLives = temp2;
		this.collision = false;
	}
	
//	//Secondary
//	public ProjectileEnemy(JLabel temp1) {
//		super(25, 30, "img_prjct_enemy.png", false, false);
//		this.lbl_prjct_enemy = temp1;
//	}
	
	//Other methods:
	@Override
	public void hide() {
		lbl_prjct_enemy.setVisible(false);
	}
	@Override
	public void show() {
		lbl_prjct_enemy.setVisible(true);
	}
		
	public void startEnemyProjectileThread() {
		thread1 = new Thread(this, "Enemy Projectile Thread");
		thread1.start();
	}
	
	private void detectPlayerCollision() {
		if(this.hitbox.intersects(myPlayer.getHitbox())) {
			this.setCollision(true);
			
			myPlayer.setPlayerLives(myPlayer.getPlayerLives() - 1);
			lbl_playerLives[myPlayer.getPlayerLives()].setVisible(false);
			System.out.println("Player Lives Remaining: " + myPlayer.getPlayerLives());
			
			myPlayer.hitbox.setSize(0, 0);
			myPlayer.setCanMove(false);
			lbl_player.setIcon(new ImageIcon(getClass().getResource("img_explosion_player.gif")));
			tmr_regeneratePlayer = new Timer(1500, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myPlayer.hitbox.setSize(myPlayer.getWidth(), myPlayer.getHeight());
					myPlayer.setCanMove(true);
					lbl_player.setIcon(new ImageIcon(getClass().getResource("img_player.png")));	
				}
			});
			tmr_regeneratePlayer.start();	
		}
	}
	
	@Override
	public void run() {
		while(this.myEnemy.getInMotion() == true) {
			int thread1_x = this.myEnemy.getX();
			int thread1_y = this.myEnemy.getY();
			this.setCollision(false);
			while(thread1_y < GameProperties.SCREEN_HEIGHT && this.getCollision() == false) {
				this.move();
				this.show(); //overridden to do same as lbl_prjct_enemy.setVisible(true);
				thread1_y += GameProperties.PRJCT_ENEMY_STEP;
				this.setX(thread1_x);
				this.setY(thread1_y);
				lbl_prjct_enemy.setLocation(this.x, this.y);
				this.detectPlayerCollision();
				
				//Check player lives to determine if game should continue:
				if(myPlayer.getPlayerLives() == 0) {
					myEnemy.stop();
					myEnemy.setGameOver(true);
					this.gameOver();
				}
				
				try {
					Thread.sleep(50);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
}

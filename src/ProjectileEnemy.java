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
	private Timer tmr_regeneratePlayer, tmr_launchEnemyProjectile;
	private Enemy myEnemy;
	
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setPlayer(Player temp) {this.myPlayer = temp;}
	public void setLbl_prjct_enemy(JLabel temp) {this.lbl_prjct_enemy = temp;}
	public void setLbl_player(JLabel temp) {this.lbl_player = temp;}
//	public void setMyEnemy(Enemy temp) {this.myEnemy = temp;}

	//Constructors:
	//Default
	public ProjectileEnemy() {
		super(25, 30, "img_prjct_enemy.png", false, false);
	}
	
	public ProjectileEnemy(Enemy temp) {
		super(25, 30, "img_prjct_enemy.png", false, false);
		this.myEnemy = temp;
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
	
	@Override
	public void run() {
		while(this.myEnemy.getInMotion() == true) {
			int thread1_x = this.myEnemy.getX();
			int thread1_y = this.myEnemy.getY();
			while(thread1_y < GameProperties.SCREEN_HEIGHT) {
				this.move();
				this.show(); //overridden to do same as lbl_prjct_enemy.setVisinle(true);
				thread1_y += GameProperties.PRJCT_ENEMY_STEP;
				this.setX(thread1_x);
				this.setY(thread1_y);
				lbl_prjct_enemy.setLocation(this.x, this.y);
				this.detectPlayerCollision();
				if(this.hitbox.intersects(myPlayer.getHitbox())) {
					//If player collision detected, reset projectile
					break;
				}
				try {
					Thread.sleep(50);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
	
	private void detectPlayerCollision() {
		if(this.hitbox.intersects(myPlayer.getHitbox())) {
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
}

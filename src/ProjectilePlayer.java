import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

public class ProjectilePlayer extends Sprite implements Runnable {
	
	//Attributes:
	private Thread thread1;
	private JLabel lbl_prjct_player, lbl_enemy;
	private Enemy myEnemy;
	private Timer timer;
	
	//When passing in arguments via setters, DO NOT alter or create new constructors for the argument - setter only!
	public void setEnemy(Enemy temp) {this.myEnemy = temp;}
	public void setLbl_prjct_player(JLabel temp) {this.lbl_prjct_player = temp;}
	public void setLbl_enemy(JLabel temp) {this.lbl_enemy = temp;}

	//Constructors:
	//Default
	public ProjectilePlayer() {
		super(50, 60, "img_prjct_player.png", false, false);
	}
	
	//Secondary
	public ProjectilePlayer(JLabel temp1) {
		super(50, 60, "img_prjct_player.png", false, false);
		this.lbl_prjct_player = temp1;
	}
	
	//Other methods:
	@Override
	public void hide() {
		lbl_prjct_player.setVisible(false);
	}
	@Override
	public void show() {
		lbl_prjct_player.setVisible(true);
	}
	
	public void launchPlayerProjectile() {
		thread1 = new Thread(this, "Player Projectile Thread");
		thread1.start();
	}
	
	private void detectEnemyCollision() {
		if(this.hitbox.intersects(myEnemy.getHitbox())) {
			myEnemy.stop();
			System.out.println("Boom!");
			lbl_enemy.setIcon(new ImageIcon(getClass().getResource("img_explosion_enemy.gif")));
			timer = new Timer(600, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					myEnemy.hitbox.setSize(0, 0);
					lbl_enemy.setVisible(false);
				}
			});
			timer.start();
		}
	}
	
	@Override
	public void run() {
		int thread1_x = this.x;
		int thread1_y = this.y;
		
		while((thread1_y > 0)) {
			this.move();
			this.show();
			lbl_prjct_player.setVisible(true);
			thread1_y -= GameProperties.PRJCT_PLAYER_STEP;
			this.setX(thread1_x);
			this.setY(thread1_y);
			lbl_prjct_player.setLocation(this.x, this.y);
			this.detectEnemyCollision();
			if(this.hitbox.intersects(myEnemy.getHitbox())) {
				//If enemy collision detected, reset laser
				break;
			}
			try {
				Thread.sleep(45);
			}
			catch(Exception e) {
				
			}
		}
		this.stop();
		lbl_prjct_player.setVisible(false);
	}
}

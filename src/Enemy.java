import javax.swing.JLabel;

public class Enemy extends Sprite implements Runnable { //Runnable -> threading class
	
	private Thread thread;
	private JLabel lbl_enemy;
//	private Enemy[][] enemies;
	private Player myPlayer;
	private ProjectileEnemy enemyProjectile;
	
	public ProjectileEnemy getEnemyProjectile() {return enemyProjectile;}
	public void setEnemyProjectile(ProjectileEnemy enemyProjectile) {this.enemyProjectile = enemyProjectile;}
	
	public JLabel getLbl_enemy() {return lbl_enemy;}
	public void setLbl_enemy(JLabel lbl_enemy) {this.lbl_enemy = lbl_enemy;}
	
	

	public Thread getThread() {
		return thread;
	}
	public void setThread(Thread thread) {
		this.thread = thread;
	}
	//Constructors:
	public Enemy() {
		super(GameProperties.ENEMY_WIDTH, GameProperties.ENEMY_HEIGHT, "img_invader1.gif", true, true, false);
		this.enemyProjectile = new ProjectileEnemy();
	}
	
	public Enemy(int temp1, int temp2, JLabel temp3, Player temp4) {
		super(GameProperties.ENEMY_WIDTH, GameProperties.ENEMY_HEIGHT, "img_invader1.gif", true, true, false);
		this.enemyProjectile = new ProjectileEnemy();
		this.x = temp1;
		this.y = temp2;
		this.lbl_enemy = temp3;
		this.myPlayer = temp4;
	}
	
	//Other methods:
	@Override
	public void hide() {
		this.lbl_enemy.setVisible(false);
//		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
//			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
//				enemies[i][j].setVisible(false);
//			}
//		}
	}
	
	@Override
	public void show() {
		this.lbl_enemy.setVisible(true);
//		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
//			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
//				enemies[i][j].setVisible(true);
//			}
//		}
	}
	
	public void display() {
		System.out.println("x,y / visible?: " + this.x + "," + this.y + " / " + this.visible);
	}

	public void moveEnemy() {
//		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
//			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				this.thread = new Thread(this, "Enemey Thread");
				this.thread.start(); //Triggers/starts the thread
//			}
//		}
	}
	
//	private void moveEnemyDown(int x, int y) {
//		y += this.getHeight();
//		this.setX(x);
//		this.setY(y);
//		lbl_enemy.setLocation(this.x, this.y);
//	}

	@Override
	public void run() { 
		while(this.getThread().isAlive()) {
			//Get current x,y:
			int thread1_x = this.x;
			int thread1_y = this.y;
			while(this.getInMotion()) {
				while((thread1_x + this.width + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
					
					//Move right:
					thread1_x += GameProperties.ENEMY_STEP;
					this.setX(thread1_x);
					this.setY(thread1_y);
					this.getLbl_enemy().setLocation(this.getX(), this.getY());
					try {
						Thread.sleep(200);
					}
					catch(Exception e) {
						
					}
				}
				
				if((thread1_y + this.getHeight()) <= myPlayer.getY()) {
					//Drop enemies down one row when they reach right wall:
					thread1_y += this.getHeight();
					this.setX(thread1_x);
					this.setY(thread1_y);
					this.getLbl_enemy().setLocation(this.getX(), this.getY());
				}
				
				while((thread1_x > 0)) {
					//Move left:
					thread1_x -= GameProperties.ENEMY_STEP;
					this.setX(thread1_x);
					this.setY(thread1_y);
					this.getLbl_enemy().setLocation(this.getX(), this.getY());
					try {
						Thread.sleep(200);
					}
					catch(Exception e) {
					}
				}
				
				if((thread1_y + this.getHeight()) <= myPlayer.getY()) {
					//Drop enemies down one row when they reach left wall:
					thread1_y += this.getHeight();
					this.setX(thread1_x);
					this.setY(thread1_y);
					this.getLbl_enemy().setLocation(this.getX(), this.getY());
				}
			}
		}
	}
}


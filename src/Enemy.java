import javax.swing.JLabel;

public class Enemy extends Sprite implements Runnable { //Runnable -> threading class
	
	private Thread thread;
	private JLabel lbl_enemy;
	private Enemy[][] enemies;
	private Player myPlayer;
	private ProjectileEnemy enemyProjectile;
	
	public ProjectileEnemy getEnemyProjectile() {return enemyProjectile;}
	public void setEnemyProjectile(ProjectileEnemy enemyProjectile) {this.enemyProjectile = enemyProjectile;}
	
	public JLabel getLbl_enemy() {return lbl_enemy;}
	public void setLbl_enemy(JLabel lbl_enemy) {this.lbl_enemy = lbl_enemy;}

	//Constructors:
	public Enemy() {
		super(50, 50, "img_invader1.gif", true, true, false);
		this.enemyProjectile = new ProjectileEnemy();
	}
	
	public Enemy(int temp1, int temp2, JLabel temp3, Player temp4) {
		super(50, 50, "img_invader1.gif", true, true, false);
		this.enemyProjectile = new ProjectileEnemy();
		this.x = temp1;
		this.y = temp2;
		this.lbl_enemy = temp3;
		this.myPlayer = temp4;
	}
	
	//Other methods:
	@Override
	public void hide() {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j].setVisible(false);
			}
		}
	}
	
	@Override
	public void show() {
		for(int i = 0; i < GameProperties.ENEMY_ROWS; i++) {
			for(int j = 0; j < GameProperties.ENEMY_COLS; j++) {
				enemies[i][j].setVisible(true);
			}
		}
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
		while(true) {
				//Get current x,y:
				int thread1_x = this.x;
				int thread1_y = this.y;
//				int offset_enemyX = 0;
//				int offset_enemyY = 0;

				while(!this.getGameOver()) {
					while((this.getInMotion() == true) && (thread1_x + this.width + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
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
					else {
						this.setGameOver(true);
						this.gameOver();
					}
					
					while((this.getInMotion() == true) && (thread1_x > 0)) {
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
					else {
						this.setGameOver(true);
						this.gameOver();
					}
				}
		}
	}
}

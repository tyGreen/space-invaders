import javax.swing.JLabel;

public class Enemy extends Sprite implements Runnable { //Runnable -> threading class
	
	private Thread thread1;
	private JLabel lbl_enemy;
	private Player myPlayer;

	//Constructors:
	public Enemy() {
		super(50, 50, "img_invader1.gif", true, true, false);
	}
	
	public Enemy(JLabel temp1, Player temp2) {
		super(50, 50, "img_invader1.gif", true, true, false);
		this.lbl_enemy = temp1;
		this.myPlayer = temp2;
	}
	
	//Other methods:
	@Override
	public void hide() {
		lbl_enemy.setVisible(false);
	}
	
	@Override
	public void show() {
		lbl_enemy.setVisible(true);
	}
	
	public void display() {
		System.out.println("x,y / visible?: " + this.x + "," + this.y + " / " + this.visible);
	}

	public void moveEnemy() {
		thread1 = new Thread(this, "Enemey Thread");
		thread1.start(); //Triggers/starts the thread
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

				while(!this.getGameOver()) {
					while((this.getInMotion() == true) && (thread1_x + this.width + GameProperties.ENEMY_STEP) < GameProperties.SCREEN_WIDTH) {
						//Move right:
						thread1_x += GameProperties.ENEMY_STEP;
						this.setX(thread1_x);
						this.setY(thread1_y);
						lbl_enemy.setLocation(this.x, this.y);
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					
//					if((thread1_y + (this.getHeight()*2)) < GameProperties.SCREEN_HEIGHT && thread1_y >= 0) {
					if((thread1_y + this.getHeight()) <= myPlayer.getY()) {
						//Drop enemies down one row when they reach right wall:
						thread1_y += this.getHeight();
						this.setX(thread1_x);
						this.setY(thread1_y);
						lbl_enemy.setLocation(this.x, this.y);
					}
					else {
						this.setGameOver(true);
						myPlayer.setCanMove(false);
						this.gameOver();
					}
					
					while((this.getInMotion() == true) && (thread1_x > 0)) {
						//Move left:
						thread1_x -= GameProperties.ENEMY_STEP;
						this.setX(thread1_x);
						this.setY(thread1_y);
						lbl_enemy.setLocation(this.x, this.y);			
						try {
							Thread.sleep(200);
						}
						catch(Exception e) {
							
						}
					}
					
//					if((thread1_y + this.getHeight()) < GameProperties.SCREEN_HEIGHT && thread1_y >= 0) {
					if((thread1_y + this.getHeight()) <= myPlayer.getY()) {
						//Drop enemies down one row when they reach left wall:
						thread1_y += this.getHeight();
						this.setX(thread1_x);
						this.setY(thread1_y);
						lbl_enemy.setLocation(this.x, this.y);
					}
					else {
						this.setGameOver(true);
						myPlayer.setCanMove(false);
						this.gameOver();
					}
				}
		}
	}
}

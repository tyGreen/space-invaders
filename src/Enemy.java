import javax.swing.JLabel;

public class Enemy extends Sprite implements Runnable { //Runnable -> threading class
	
	private Thread thread1;
	private JLabel lbl_enemy;

	//Constructors:
	public Enemy() {
		super(50, 50, "img_invader1.gif", true, true);
	}
	
	public Enemy(JLabel temp) {
		super(50, 50, "img_invader1.gif", true, true);
		this.lbl_enemy = temp;
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

				while(true) {
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
					
					if(thread1_y < 800) {
						//Drop enemies down one row when they hit a right wall:
						thread1_y += this.getHeight();
						this.setX(thread1_x);
						this.setY(thread1_y);
						lbl_enemy.setLocation(this.x, this.y);
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
					
					if(thread1_y < GameProperties.SCREEN_HEIGHT && thread1_y > 0) {
						//Drop enemies down one row when they hit a left wall:
						thread1_y += this.getHeight();
						this.setX(thread1_x);
						this.setY(thread1_y);
						lbl_enemy.setLocation(this.x, this.y);
					}
				}
		}
	}
}

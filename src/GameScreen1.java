import java.awt.Color;
import java.awt.Container;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameScreen1 extends JFrame implements KeyListener{

	private static final long serialVersionUID = -8824959153681940829L;
	
	//Storage classes for game sprites:
	private Player myPlayer;
	private ProjectilePlayer prjct_player;
	private ProjectileEnemy prjct_enemy;
	private Enemy myEnemy;
	private Background myBg;
	
	//JLabels to display sprites:
	private JLabel lbl_player, lbl_enemy, lbl_prjct_player, lbl_prjct_enemy, lbl_bg;
	private ImageIcon img_player, img_enemy, img_prjct_player, img_prjct_enemy, img_bg;
	
//	//Button to control Enemy:
//	private JButton btn_hideEnemy, btn_moveEnemy;
	
	//Graphics container:
	private Container container1;
	
	//GUI set-up (constructor):
	public GameScreen1() {
		//Window title:
		super("Space Invaders");
		//Screen size:
		setSize(GameProperties.SCREEN_WIDTH, GameProperties.SCREEN_HEIGHT);
		//Centers game window on screen upon launching:
		setLocationRelativeTo(null);
		
		lbl_player = new JLabel();
		myPlayer = new Player(lbl_player);
		img_player = new ImageIcon(getClass().getResource(myPlayer.getFileName()));
		lbl_player.setIcon(img_player);
		lbl_player.setSize(myPlayer.getWidth(), myPlayer.getHeight());
		
		lbl_enemy = new JLabel();
		myEnemy = new Enemy(lbl_enemy);
		img_enemy = new ImageIcon(getClass().getResource(myEnemy.getFileName()));
		lbl_enemy.setIcon(img_enemy);
		lbl_enemy.setSize(myEnemy.getWidth(), myEnemy.getHeight());
		myEnemy.moveEnemy();
//		myEnemy.setLbl_enemy(lbl_enemy);
//		myEnemy.setProjectile(myProjectile);
//		myEnemy.setLbl_projectile(lbl_projectile);
		
		lbl_prjct_player = new JLabel();
		prjct_player = new ProjectilePlayer(lbl_prjct_player);
		img_prjct_player = new ImageIcon(getClass().getResource(prjct_player.getFileName()));
		lbl_prjct_player.setIcon(img_prjct_player);
		lbl_prjct_player.setSize(prjct_player.getWidth(), prjct_player.getHeight());
		prjct_player.setLbl_prjct_player(lbl_prjct_player);
		prjct_player.setEnemy(myEnemy);
		prjct_player.setLbl_enemy(lbl_enemy);
		
		lbl_prjct_enemy = new JLabel();
//		prjct_enemy = new ProjectileEnemy(lbl_prjct_enemy, myEnemy);
		prjct_enemy = new ProjectileEnemy(myEnemy);
		img_prjct_enemy = new ImageIcon(getClass().getResource(prjct_enemy.getFileName()));
		lbl_prjct_enemy.setIcon(img_prjct_enemy);
		lbl_prjct_enemy.setSize(prjct_enemy.getWidth(), prjct_enemy.getHeight());
		prjct_enemy.setLbl_prjct_enemy(lbl_prjct_enemy);
		prjct_enemy.setPlayer(myPlayer);
		prjct_enemy.setLbl_player(lbl_player);
		
		lbl_bg = new JLabel();
		myBg = new Background();
		img_bg = new ImageIcon(getClass().getResource(myBg.getFileName()));
		lbl_bg.setIcon(img_bg);
		lbl_bg.setSize(myBg.getWidth(), myBg.getHeight());
		

		container1 = getContentPane();
		container1.setBackground(Color.black);
		setLayout(null);
		
		//Set object coordinates:
		myPlayer.setX((GameProperties.SCREEN_WIDTH/2) - myPlayer.getWidth());
		myPlayer.setY(GameProperties.SCREEN_HEIGHT - (myPlayer.getHeight() * 2));
		
		prjct_player.setX(myPlayer.getX());
		prjct_player.setY(myPlayer.getY());
		
		prjct_enemy.setX(myEnemy.getX());
		prjct_enemy.setY(myEnemy.getY());
		
		myBg.setX(0);
		myBg.setY(0);
		
		//Update lbl positions to match stored values:
		lbl_player.setLocation(myPlayer.getX(), myPlayer.getY());
		lbl_prjct_player.setLocation(myPlayer.getX(), myPlayer.getY());
		lbl_enemy.setLocation(myEnemy.getX(), myEnemy.getY());
		lbl_prjct_enemy.setLocation(myEnemy.getX(), myEnemy.getY());
		lbl_bg.setLocation(myBg.getX(), myBg.getY());
		
		//Add objects to screen:
		add(lbl_player);
		add(lbl_prjct_player);
		add(lbl_enemy);
		add(lbl_prjct_enemy);
		add(lbl_bg);
		lbl_enemy.setVisible(myEnemy.getVisible());
		lbl_prjct_player.setVisible(false);
		lbl_prjct_enemy.setVisible(false);
		
		container1.addKeyListener(this);
		container1.setFocusable(true);
		//Action upon hitting close button:
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
	}

	public static void main(String args[]) { 
		GameScreen1 myGameScreen = new GameScreen1();
		myGameScreen.setVisible(true); 
		myGameScreen.prjct_enemy.startEnemyProjectileThread();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		//Key press & release treated as one
	}

	@Override
	public void keyPressed(KeyEvent e) {
		//Key press only
		int player_x = myPlayer.getX();
		int player_y = myPlayer.getY();
		int enemy_x = myEnemy.getX();
		int enemy_y = myEnemy.getY();

		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if((myPlayer.getCanMove() == true) && (player_x - GameProperties.PLAYER_STEP) > 0) {
				player_x -= GameProperties.PLAYER_STEP;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if((myPlayer.getCanMove() == true) && (player_x + myPlayer.getWidth() + GameProperties.PLAYER_STEP) < GameProperties.SCREEN_WIDTH) {
				player_x += GameProperties.PLAYER_STEP;
			}
		}
		else if(e.getKeyCode() == KeyEvent.VK_SPACE) {//Launch player projectile
				if((myPlayer.getCanMove() == true) && (prjct_player.getInMotion() == false)) {
					//Shoot
					prjct_player.launchPlayerProjectile();
				}
		}
//		else if(e.getKeyCode() == KeyEvent.VK_S) {//Launch enemy projectile
//			if(myEnemy.getInMotion() == true) {
//				if(prjct_enemy.getInMotion() == false) {
//					//Shoot
//					prjct_enemy.startEnemyProjectileThread();
//				}
//			}
//		}
		myPlayer.setX(player_x);
		lbl_player.setLocation(myPlayer.getX(), myPlayer.getY());
		//Update x-coordinate of player projectile ONLY IF not in already motion:
		if(prjct_player.getInMotion() == false) {
			prjct_player.setX(player_x);
			prjct_player.setY(player_y);
			lbl_prjct_player.setLocation(myPlayer.getX(), myPlayer.getY());
		}
		//Update x-coordinate of enemy projectile ONLY IF not already in motion:
		if(prjct_enemy.getInMotion() == false) {
			prjct_enemy.setX(enemy_x);
			prjct_enemy.setY(enemy_y);
			lbl_prjct_enemy.setLocation(myEnemy.getX(), myEnemy.getY());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		//Key release only	
	}	
}

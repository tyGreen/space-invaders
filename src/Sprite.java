import java.awt.Rectangle;

import javax.swing.JOptionPane;

//Base class for all game sprites
public class Sprite {
	protected int x, y, width, height;
		//x & y -> upper left COORDINATES of object
		//width & height -> SIZE of object
	protected String fileName;
	protected Rectangle hitbox;
	protected Boolean inMotion, visible, gameOver;
	
	//Getters & setters:
	public int getX() {return x;}
	public void setX(int x) {this.x = x; this.hitbox.setLocation(this.x, this.y);}
	
	public int getY() {return y;}
	public void setY(int y) {this.y = y; this.hitbox.setLocation(this.x, this.y);}
	
	public int getWidth() {return width;}
	public void setWidth(int width) {this.width = width; this.hitbox.setSize(this.width, this.height);}
	
	public int getHeight() {return height;}
	public void setHeight(int height) {this.height = height; this.hitbox.setSize(this.width, this.height);}
	
	public String getFileName() {return fileName;}
	public void setFileName(String fileName) {this.fileName = fileName;}
	
	public Boolean getInMotion() {return inMotion;}
	public void setInMotion(Boolean moving) {this.inMotion = moving;}
	
	public Boolean getVisible() {return visible;}
	public void setVisible(Boolean visible) {this.visible = visible;}
	
	public Boolean getGameOver() {return gameOver;}
	public void setGameOver(Boolean gameOver) {this.gameOver = gameOver;}
	
	public Rectangle getHitbox() {return hitbox;}
//	public void setHitbox(Rectangle hitbox) {this.hitbox = hitbox;}
	
	//Default constructor:
	public Sprite() {
		super();
		this.x = 0;
		this.y = 0;
		this.width = 0;
		this.height = 0;
		this.fileName = "";
		this.inMotion = false;
		this.visible = false;
		this.gameOver = false;
		this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
	}
	
	//Secondary constructors:
		//All fields:
	public Sprite(int x, int y, int width, int height, String fileName, Boolean moving, Boolean visible, Boolean gameOver) {
		super();
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.fileName = fileName;
		this.inMotion = moving;
		this.visible = visible;
		this.gameOver = gameOver;
		this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
	}
	
		//width, height, fileName, moving, visible, & gameOver:
	public Sprite(int width, int height, String fileName, Boolean moving, Boolean visible, Boolean gameOver) {
		super();
		this.x = 0;
		this.y = 0;
		this.width = width;
		this.height = height;
		this.fileName = fileName;
		this.inMotion = moving;
		this.visible = visible;
		this.gameOver = false;
		this.hitbox = new Rectangle(this.x, this.y, this.width, this.height);
	}
	
	//Display f(x):
	public void display() {
		System.out.println("x,y: " + this.x + "," + this.y);
	}
	
	//Other methods:
	public void move() {this.inMotion = true;}
	public void stop( ) {this.inMotion = false;}
	public void hide() {this.visible = false;}
	public void show() {this.visible = true;}
	
	public void gameOver() {
		this.gameOver = true;
		this.stop();
		JOptionPane.showMessageDialog(null, "GAME OVER");
		System.exit(0);
	}
}

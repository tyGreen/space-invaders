import javax.swing.JLabel;

public class Player extends Sprite {
	
	private JLabel lbl_player;
	private Boolean canMove;
	
	//Getters & setters:
	public Boolean getCanMove() {return canMove;}
	public void setCanMove(Boolean canMove) {this.canMove = canMove;}

	//Constructors:
	public Player() {
		super(50, 60, "img_player.png", false, true, false);
		canMove = true;
	}
	
	public Player(JLabel temp) {
		super(50, 60, "img_player.png", false, true, false);
		this.lbl_player = temp;
		canMove = true;
	}
	
	//Other methods
	@Override
	public void hide() {
		lbl_player.setVisible(false);
	}
	
	@Override
	public void show() {
		lbl_player.setVisible(true);
	}
}

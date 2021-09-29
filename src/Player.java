import javax.swing.JLabel;

public class Player extends Sprite {
	
	private JLabel lbl_player;
	
	//Constructors:
	public Player() {
		super(50, 60, "img_player.png", false, true);
	}
	
	public Player(JLabel temp) {
		super(50, 60, "img_player.png", false, true);
		this.lbl_player = temp;
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

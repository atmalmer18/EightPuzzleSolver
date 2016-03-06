// BUTTON USED FOR THE GAME

import javax.swing.JButton;
import java.awt.Color;

public class Tile extends JButton{
	int value;
	int xCoordinate;
	int yCoordinate;

	public Tile () {
		value = 0;
	}

	public Tile (int val) {
		value = val;
	}
	
	public void toggleTile (Tile nextTile) {
		int tmp;
		
		tmp = value;
		value = nextTile.value;
		nextTile.value = tmp;
		
		this.setLabel("");
		this.setEnabled(false);
		this.setBackground(Color.DARK_GRAY);
		
		nextTile.setLabel("" + nextTile.value);
		nextTile.setEnabled(true);
		nextTile.setBackground(Color.BLACK);
	}
}

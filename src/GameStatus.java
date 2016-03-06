// BUTTONS USED FOR THE GAME

import javax.swing.JButton;
import java.awt.Color;

public class GameStatus extends JButton{
	boolean isPaused = false;

	public GameStatus () {
			this.setText("Solve");
	}

	// will toggle status of light and UI
	public void toggleStatus () {
		if (this.isPaused == true) {
			this.setText("Solve");
			this.isPaused = false;
		} else {
			this.setText("Back to game");
			this.isPaused = true;
		}
	}
}

// CONTAINS MAIN FUNCTION

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

public class MendozaEx3 {
  public static void main (String[] args) {

	// configure frame and panels
	JFrame frame = new JFrame("8Puzzle");
	JPanel headerPanel = new JPanel();
	JPanel tilesPanel = new JPanel();
	JPanel menuPanel = new JPanel();
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	EightPuzzle lightsOut = new EightPuzzle(headerPanel, tilesPanel, menuPanel);

	// positioning elements
	frame.getContentPane().add(headerPanel, BorderLayout.NORTH);
	frame.getContentPane().add(tilesPanel, BorderLayout.CENTER);
	frame.getContentPane().add(menuPanel, BorderLayout.SOUTH);

	// show game
	frame.pack();
	frame.setVisible(true);
	
	System.out.println("\nGame generated");
  }
}

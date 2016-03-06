// GAME FILE

import java.lang.Integer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Point;

import java.util.Random;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.io.*;

import java.io.BufferedReader;
import java.io.PrintWriter;

public class EightPuzzle {
	EightPuzzle game;
	GameStatus statusButton;
	State state;

	Tile[][] tiles;
	Tile[][] output;
	
	LinkedList <State> tmpStates;
	
	JButton prevButton;
	JButton nextButton;
	
	int pageFlag;

	public EightPuzzle (JPanel headerPanel, JPanel tilesPanel, JPanel menuPanel) {
		JLabel headerLabel = new JLabel("by almer_mendoza", SwingConstants.CENTER);
		JButton generateButton = new JButton("Generate");
		LinkedList <Integer> list = new LinkedList <Integer> ();
		int tmpNum, num = 1;
		Random rand = new Random();

		tiles = new Tile[3][3];
		statusButton = new GameStatus();

		// configure headers and labels
		headerLabel.setForeground(Color.GREEN);
		headerPanel.setBackground(Color.DARK_GRAY);
		headerPanel.add(headerLabel);
		tilesPanel.setLayout(new GridLayout(3, 3));

		// generate board
		for (int i = 0; i < 3; i += 1) {
			for (int j = 0; j < 3; j += 1, num += 1) {
				tiles[i][j] = new Tile();

				// configure buttons
				tiles[i][j].setPreferredSize(new Dimension(125, 125));
				tiles[i][j].setBackground(Color.BLACK);
				tiles[i][j].setForeground(Color.WHITE);

				// will generate a random board
				while (true) {
					tmpNum = rand.nextInt(9);
					if (list.indexOf(tmpNum) == -1) {
						num = tmpNum;
						break;
					}
				}

				list.add(num);
				tiles[i][j].setLabel("" + num);
				tiles[i][j].value = num;
				
				if (tiles[i][j].value != 0) {
					tiles[i][j].setEnabled(true);
					tiles[i][j].setBackground(Color.BLACK);							
				} else {
					tiles[i][j].setEnabled(false);
					tiles[i][j].setLabel("");
					tiles[i][j].setBackground(Color.DARK_GRAY);
				}

				tiles[i][j].addActionListener(new ActionListener () {
					public void actionPerformed (ActionEvent e) {
						Object source = e.getSource();
						if (source instanceof Tile) {
                            Tile btn = (Tile)source;
                            Point tmpPoint = null;
							for (int x = 0; x < 3; x += 1) {
                                for (int y = 0; y < 3; y += 1) {
									// if button selected, then toggle tiles closest to it
									if (tiles[x][y] == btn) {
										tmpPoint = getZeroTile(tiles);
										if (
											(Math.abs(y - tmpPoint.y) == 0 && Math.abs(x - tmpPoint.x) == 1) ||
											(Math.abs(x - tmpPoint.x) == 0 && Math.abs(y - tmpPoint.y) == 1)
										) {
											tiles[x][y].toggleTile(tiles[tmpPoint.x][tmpPoint.y]);
											System.out.println("[" + x + "," + y + "] clicked!");
											break;
										}
                                    }
                                }
                            }
							
							int[][] config = new int[3][3];
							for (int x = 0; x < 3; x += 1) {
								for (int y = 0; y < 3; y += 1) {
									config[x][y] = tiles[x][y].value;
								}
							}

							// if win then close game then prompt
							if (goalTest(new State(config))) {
								JOptionPane.showMessageDialog(new JFrame(), "You win!");
								System.exit(0);
							}
                        }
                    }
				});

				// add button to game
				tiles[i][j].xCoordinate = i;
				tiles[i][j].yCoordinate = j;
				
				tilesPanel.add(tiles[i][j]);
			}
		}

		list.clear();

		// button to reset board to goal state
		JButton resetButton = new JButton ("Reset");
		
		resetButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				int num = 1;
				for (int i = 0; i < 3; i += 1) {
					for (int j = 0; j < 3; j += 1, num += 1) {
						// configure buttons
						tiles[i][j].setPreferredSize(new Dimension(125, 125));
						tiles[i][j].setBackground(Color.BLACK);
						tiles[i][j].setForeground(Color.WHITE);

						if (num == 9) num = 0;
						tiles[i][j].value = num;
						if (num != 0) tiles[i][j].setLabel("" + num);
						else tiles[i][j].setLabel("");
						// will generate a random board

						// add button to game
						tiles[i][j].xCoordinate = i;
						tiles[i][j].yCoordinate = j;
						
						if (tiles[i][j].value == 0) {
							tiles[i][j].setEnabled(false);
							tiles[i][j].setBackground(Color.DARK_GRAY);				
						} else {
							tiles[i][j].setEnabled(true);
						}
					}
				}
			}
		});

		// will randomize the board on click
		generateButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				Random rand = new Random();
				LinkedList <Integer> list = new LinkedList <Integer> ();
				int tmpNum;
				int num = 0;
				for (int i = 0; i < 3; i += 1) {
					for (int j = 0; j < 3; j += 1) {
						while (true) {
							tmpNum = rand.nextInt(9);
							if (list.indexOf(tmpNum) == -1) {
								num = tmpNum;
								break;
							}
						}

						list.add(num);
						tiles[i][j].setLabel("" + num);
						tiles[i][j].value = num;
						
						if (tiles[i][j].value != 0) {
							tiles[i][j].setEnabled(true);
							tiles[i][j].setBackground(Color.BLACK);							
						} else {
							tiles[i][j].setEnabled(false);
							tiles[i][j].setLabel("");
							tiles[i][j].setBackground(Color.DARK_GRAY);
						}
					}
				}
			}
		});

		// will show solution to the puzzle
		statusButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				Object source = e.getSource();
				GameStatus btn = (GameStatus)source;
				int[][] config = new int[3][3];
				State state;

				System.out.println("\nWill present solution to the puzzle \n");

				if (statusButton.isPaused == true) {
					for (int x = 0; x < 3; x += 1) {
						for (int y = 0; y < 3; y += 1) {
							if (tiles[x][y].value != 0) tiles[x][y].setEnabled(true);
						}
					}
				} else {
					for (int x = 0; x < 3; x += 1) {
						for (int y = 0; y < 3; y += 1) {
							// tiles[x][y].setEnabled(false);
							config[x][y] = tiles[x][y].value;
						}
					}

					state = new State(config);
					State newState = AStarSearch(state);
					if (newState != null) showSolution(newState);
					else showUnsolvable(state);
				}

				btn.toggleStatus();
			}
		});

		menuPanel.setBackground(Color.DARK_GRAY);
		
		resetButton.setBackground(Color.BLACK);
		resetButton.setForeground(Color.GREEN);

		generateButton.setBackground(Color.BLACK);
		generateButton.setForeground(Color.GREEN);

		statusButton.setBackground(Color.BLACK);
		statusButton.setForeground(Color.GREEN);

		menuPanel.add(resetButton);
		menuPanel.add(generateButton);
		menuPanel.add(statusButton);
	}

	// will search for the solution
	public State AStarSearch (State initialState) {
		LinkedList <State> openList = new LinkedList <State> ();
		LinkedList <State> closedList = new LinkedList <State> ();
		State currentState = null;


		// end as soon as board is unsolvable
		if (isUnsolvable(initialState) == true) {
			return null;
		}
		openList.add(initialState);
		while (openList.size() != 0) {
			currentState = removeMinF(openList);
			closedList.add(currentState);
			if (goalTest(currentState) == true) {
				System.out.println("WIN");
				return currentState;
			} else {
				for (Point tmpPoint : action(currentState)) {
					// if not in openlist of closedlist, insert result to openlist
					// else check if state has less cost than resulting state;
					// if resulting state is less expensive, insert resulting state
					if (
						(
							openList.contains(currentState) == false || 
							closedList.contains(currentState) == false
						) ||
						(
							(
								openList.contains(currentState) == true || 
								closedList.contains(currentState) == true
							) &&
							(
								currentState.getCost() > result(currentState, tmpPoint).getCost()
							)
						)
					) {
						openList.add(result(currentState, tmpPoint));
					}
				}
			}
		}
		return new State();
	}
	
	// given a state and a point, will output a state where the point is applied
	public State result (State currentState, Point actionPoint) {
		State localState = new State(currentState.getConfig(), currentState);
		Point tmpPoint = null;
		int x = 0, y = 0, tmp = 0, flag = 0;

		for (x = 0; x < 3; x += 1) {
			for (y = 0; y < 3; y += 1) {
				if (localState.getConfigValue(x, y) == 0) {
					tmpPoint = new Point(x, y);
					break;
				}
			}
			if (tmpPoint != null) break;
		}

		localState.toggleTile(tmpPoint, actionPoint);

		return new State(localState.getConfig(), currentState);
	}
	
	// will output all possible actions to the state
	public LinkedList <Point> action (State currentState) {
		LinkedList <Point> list = new LinkedList <Point> ();
		Point tmpPoint = null;
		int x = 0, y = 0;

		for (x = 0; x < 3; x += 1) {
			for (y = 0; y < 3; y += 1) {
				if (currentState.getConfig()[x][y] == 0) {
					tmpPoint = new Point(x, y);
					break;
				}
			}
			if (tmpPoint != null) break; 
		}

		if (tmpPoint.x != 2) list.add(new Point(x+1, y));
		if (tmpPoint.x != 0) list.add(new Point(x-1, y));
		if (tmpPoint.y != 2) list.add(new Point(x, y+1));
		if (tmpPoint.y != 0) list.add(new Point(x, y-1));

		return list;
	}
	
	// will output true if all tiles are off
	public boolean goalTest (State currentState) {
		if (currentState.getManhattanDistance() == 0) return true;
		return false;
	}
	
	// will get blank tile
	public Point getZeroTile (Tile[][] currentTiles) {
		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1) {
				if (currentTiles[x][y].value == 0) {
					return new Point(x, y);
				}
			}
		}
		
		return new Point(0, 0);
	}
	
	// remove minimum f
	public State removeMinF (LinkedList <State> openList) {
		State stateWithMinimumF = openList.get(0);
		for (State tmpState : openList) {
			if (tmpState.getFValue() < stateWithMinimumF.getFValue()) {
				stateWithMinimumF = tmpState;
			}
		}
		openList.remove(openList.indexOf(stateWithMinimumF));
		return stateWithMinimumF;
	}
	
	// check duplicate
	public boolean isDuplicate (State currentState, LinkedList <State> list) {
		if (list.size() == 0) return false; 
		for (State tmpState : list) {
			for (int a = 0; a < 3; a += 1) {
				for (int b = 0; b < 3; b += 1) {
					if (currentState.getConfigValue(a, b) != tmpState.getConfigValue(a, b)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	// will test if board is solvable
	public boolean isUnsolvable (State currentState) {
		int[] config = new int[9];
		int inversion = 0;
		
		for (int j = 0, x = 0; j < 3; j += 1) {
			for (int k = 0; k < 3; k += 1, x += 1) {
				config[x] = currentState.getConfigValue(j, k);
			}
		}
		
		for (int a = 0; a < 9; a += 1) {
			for (int b = a + 1; b < 9; b += 1) {
				if (config[a] == 0) break;
				if (config[b] == 0) b += 1;
				if (b != 9 && config[a] > config[b]) {
					inversion += 1;
				}
			}
		}
		
		if (inversion % 2 != 0) return true;
		return false;
	}

	// show unsolvable
	public void showUnsolvable (State bestState) {
		// print to text file
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("8puzzle.out"));
			State temporaryStateForWrite = bestState;
			writer.println("Initial State: ");
			for (int x = 0; x < 3; x += 1) {
				for (int y = 0; y < 3; y += 1) {
					writer.print(temporaryStateForWrite.getConfigValue(x, y) + " ");
				}
				writer.println("");
			}
			writer.println("Unsolvable puzzle!");			
			writer.close();
		} catch(Exception err){ }
		
		// create frame for answer
		JFrame outputFrame = new JFrame("8Puzzle");
		JPanel outputHeaderPanel = new JPanel();
		JPanel outputTilesPanel = new JPanel();
		JPanel outputMenuPanel = new JPanel();
		JLabel headerLabel = new JLabel("SOLUTION", SwingConstants.CENTER);

		// tile for new frame
		output = new Tile[3][3];

		// configure headers and labels
		headerLabel.setForeground(Color.GREEN);
		outputHeaderPanel.setBackground(Color.DARK_GRAY);
		outputHeaderPanel.add(headerLabel);
		outputTilesPanel.setLayout(new GridLayout(3, 3));

		// set new board for output
		for (int i = 0; i < 3; i += 1) {
			for (int j = 0; j < 3; j += 1) {
				output[i][j] = new Tile();
				
				// configure buttons
				output[i][j].setPreferredSize(new Dimension(125, 125));
				output[i][j].setBackground(Color.BLACK);
				output[i][j].setForeground(Color.WHITE);
				
				output[i][j].setLabel("");

				if (i == 1 && j == 1) {
					output[i][j].setEnabled(true);
					output[i][j].setLabel("UNSOLVABLE");
					output[i][j].setBackground(Color.DARK_GRAY);			
				} else {
					output[i][j].setEnabled(false);
				}
				
				outputTilesPanel.add(output[i][j]);
			}
		}
		
		outputMenuPanel.setBackground(Color.DARK_GRAY);
		
		// positioning elements
		outputFrame.getContentPane().add(outputHeaderPanel, BorderLayout.NORTH);
		outputFrame.getContentPane().add(outputTilesPanel, BorderLayout.CENTER);
		outputFrame.getContentPane().add(outputMenuPanel, BorderLayout.SOUTH);
		
		// show game
		outputFrame.setResizable(false);
		outputFrame.pack();
		outputFrame.setVisible(true);
	}
	
	// show solution
	public void showSolution (State bestState) {
		State currentState = bestState;
		int step = 0;
		tmpStates = new LinkedList <State> ();
		tmpStates.add(bestState);
		
		while (currentState.getParent() != null) {
			currentState = currentState.getParent();
			tmpStates.add(currentState);
		}
		
		// print to text file
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream("8puzzle.out"));
			for (int m = tmpStates.size() - 1; m >= 0; m -= 1) {
				State temporaryStateForWrite = tmpStates.get(m);
				if (step == 0) writer.println("Initial State: ");
				else writer.println("Step " + step + ": ");
				for (int x = 0; x < 3; x += 1) {
					for (int y = 0; y < 3; y += 1) {
						writer.print(temporaryStateForWrite.getConfigValue(x, y) + " ");
					}
					writer.println("");
				}
				step += 1;
			}
			writer.close();
		} catch(Exception err){ }
		
		// create frame for answer
		JFrame outputFrame = new JFrame("8Puzzle");
		JPanel outputHeaderPanel = new JPanel();
		JPanel outputTilesPanel = new JPanel();
		JPanel outputMenuPanel = new JPanel();
		JLabel headerLabel = new JLabel("SOLUTION", SwingConstants.CENTER);

		// tile for new frame
		output = new Tile[3][3];

		// configure headers and labels
		headerLabel.setForeground(Color.GREEN);
		outputHeaderPanel.setBackground(Color.DARK_GRAY);
		outputHeaderPanel.add(headerLabel);
		outputTilesPanel.setLayout(new GridLayout(3, 3));

		// set new board for output
		for (int i = 0; i < 3; i += 1) {
			for (int j = 0; j < 3; j += 1) {
				output[i][j] = new Tile();
				
				// configure buttons
				output[i][j].setPreferredSize(new Dimension(125, 125));
				output[i][j].setBackground(Color.BLACK);
				output[i][j].setForeground(Color.WHITE);
				
				output[i][j].value = tmpStates.get(tmpStates.size() - 1).getConfigValue(i, j);

				// add button to game
				output[i][j].xCoordinate = i;
				output[i][j].yCoordinate = j;
				
				if (output[i][j].value == 0) {
					output[i][j].setEnabled(false);
					output[i][j].setLabel("");
					output[i][j].setBackground(Color.DARK_GRAY);			
				} else {
					output[i][j].setEnabled(true);
					output[i][j].setLabel("" + tmpStates.get(tmpStates.size() - 1).getConfigValue(i, j));
				}
				
				outputTilesPanel.add(output[i][j]);
			}
		}
		
		outputMenuPanel.setBackground(Color.DARK_GRAY);
		
		// output menu panel
		pageFlag = tmpStates.size() - 1;
		
		// button to present previous step
		prevButton = new JButton("Prev");
		prevButton.setEnabled(false);
		
		prevButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				Object source = e.getSource();
				JButton btn = (JButton)source;
				
				pageFlag += 1;
				
				State tmpState = tmpStates.get(pageFlag);
				
				for (int i = 0; i < 3; i += 1) {
					for (int j = 0; j < 3; j += 1) {
						output[i][j].setLabel("" + tmpState.getConfigValue(i, j));
						output[i][j].value = tmpState.getConfigValue(i, j);
						
						if (output[i][j].value != 0) {
							output[i][j].setEnabled(true);
							output[i][j].setBackground(Color.BLACK);							
						} else {
							output[i][j].setLabel("");
							output[i][j].setEnabled(false);
							output[i][j].setBackground(Color.DARK_GRAY);
						}
					}
				}
				
				if (pageFlag != (tmpStates.size() - 1)) {
					btn.setEnabled(true);
				} else {
					btn.setEnabled(false);
				}
				nextButton.setEnabled(true);
			}
		});
		
		prevButton.setBackground(Color.BLACK);
		prevButton.setForeground(Color.GREEN);
		
		// button to present next step
		nextButton = new JButton("Next");
		if (tmpStates.size() == 1) nextButton.setEnabled(false);
		
		nextButton.addActionListener(new ActionListener () {
			public void actionPerformed (ActionEvent e) {
				Object source = e.getSource();
				JButton btn = (JButton)source;
				
				pageFlag -= 1;
				
				State tmpState = tmpStates.get(pageFlag);
				
				for (int i = 0; i < 3; i += 1) {
					for (int j = 0; j < 3; j += 1) {
						output[i][j].setLabel("" + tmpState.getConfigValue(i, j));
						output[i][j].value = tmpState.getConfigValue(i, j);
						
						if (output[i][j].value != 0) {
							output[i][j].setEnabled(true);
							output[i][j].setBackground(Color.BLACK);							
						} else {
							output[i][j].setEnabled(false);
							output[i][j].setLabel("");
							output[i][j].setBackground(Color.DARK_GRAY);
						}
					}
				}
				
				if (pageFlag != 0) {
					btn.setEnabled(true);
				} else {
					btn.setEnabled(false);
				}
				prevButton.setEnabled(true);					
			}
		});
		
		nextButton.setBackground(Color.BLACK);
		nextButton.setForeground(Color.GREEN);
		
		outputMenuPanel.add(prevButton);
		outputMenuPanel.add(nextButton);

		// positioning elements
		outputFrame.getContentPane().add(outputHeaderPanel, BorderLayout.NORTH);
		outputFrame.getContentPane().add(outputTilesPanel, BorderLayout.CENTER);
		outputFrame.getContentPane().add(outputMenuPanel, BorderLayout.SOUTH);
		
		// show game
		outputFrame.setResizable(false);
		outputFrame.pack();
		outputFrame.setVisible(true);
	}
}

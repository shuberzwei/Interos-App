package app;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * @author shuber2
 *
 */
public class MyneSweeper extends JFrame {
	private String gameData;

	/**
	 * Constructs a frame and adds a panel.
	 */
	MyneSweeper(String difficulty) {
		gameData = difficulty;		
		this.setData(difficulty);
		this.setTitle("MyneSweeper");
		int frameWidth = Data.cols * 40;
		int frameHeight = Data.rows * 40;
		Dimension d = new Dimension(frameWidth, frameHeight);
		this.setPreferredSize(d);
		//TODO format panel size
		this.getContentPane().add(new MynePanel(frameWidth));
		this.setJMenuBar(addMenuBar(this));
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	/**
	 * Creates a basic menu with New, Open, Save, Options, Quit, and Help menu items.
	 * 
	 * @return the menu
	 */
	private JMenuBar addMenuBar(final MyneSweeper frame) {
		JMenu file = new JMenu("File");
		
		JMenuItem newItem = new JMenuItem("New", KeyEvent.VK_N);
		newItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		newItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
					Driver.main(null);
				}
			}
		);
		file.add(newItem);
		
		JMenuItem openItem = new JMenuItem("Open", KeyEvent.VK_O);
		openItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		openItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						openGame();
					}
				}
			);
		file.add(openItem);
		
		JMenuItem saveItem = new JMenuItem("Save", KeyEvent.VK_S);
		saveItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						saveGame();
					}
				}
			);
		file.add(saveItem);
		
		file.addSeparator();
		
		JMenuItem optionsItem = new JMenuItem("Options");
		optionsItem.addActionListener(
				new ActionListener( ){
					public void actionPerformed(ActionEvent e) {
						System.out.println("Options"); //TODO
					}
				}
			);
		file.add(optionsItem);
		
		JMenuItem quitItem = new JMenuItem("Quit", KeyEvent.VK_Q);
		quitItem.setAccelerator(KeyStroke.getKeyStroke(
		        KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		quitItem.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			}
		);
		file.add(quitItem);
		
		JMenu help = new JMenu("Help");
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//TODO System.out.println("About");
					}
				}
			);
		help.add(aboutItem);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(file);
		menuBar.add(help);
		return menuBar;
	}
	
	/**
	 * Saves a string representation of the game state in a file. TODO Only saves one game.
	 */
	private void saveGame() {
		try {
			FileWriter fstream = new FileWriter("MyneSweeper/savedGame.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			if(!MyneButtonListener.isFirstClick()) { //if game was started, add state data
				gameData += "&" + MynePanel.buttonsToString();
				gameData += "&" + MyneButtonListener.getNumClicks();
			}
			out.write(gameData); //write difficulty, button states, numClicks
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void openGame() {
		//TODO
		try {
			File file = new File("MyneSweeper/out.txt");
			if(file.exists()) {
				FileInputStream fstream = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				String line;
				while ((line = br.readLine()) != null) {
					//set difficulty, if game was started then restore buttons array, set isFirstClick and NumClicks
				}
				in.close();
			} else {
				//error dialog
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Sets data constants to reflect chosen difficulty.
	 * 
	 * @param difficulty
	 */
	private void setData(String difficulty) {
		if(difficulty.startsWith("easy")) {
			Data.rows = Data.easyRows;
			Data.cols = Data.easyCols;
			Data.numMines = Data.easyNumMines;
		} else if(difficulty.startsWith("medium")) {
			Data.rows = Data.medRows;
			Data.cols = Data.medCols;
			Data.numMines = Data.medNumMines;
		} else {
			Data.rows = Data.hardRows;
			Data.cols = Data.hardCols;
			Data.numMines = Data.hardNumMines;
		}
	}
}

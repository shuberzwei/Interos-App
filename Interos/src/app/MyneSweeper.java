package app;

import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 * @author shuber2
 *
 */
public class MyneSweeper extends JFrame {

	private static final long serialVersionUID = -8048346619219793881L;
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
	 * Creates a basic menu with New, Open, Save, Quit, and Help menu items.
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
		
/* TODO
		JMenuItem optionsItem = new JMenuItem("Options");
		optionsItem.addActionListener(
				new ActionListener( ){
					public void actionPerformed(ActionEvent e) {
						
					}
				}
			);
		file.add(optionsItem);
*/
		
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
						openREADME();
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
				gameData += "\n" + MynePanel.buttonsToString();
				gameData += MyneButtonListener.getNumClicks();
			}
			out.write(gameData); //write difficulty, button states, numClicks
			out.close();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	/**
	 * Opens a saved game file and restores game state.
	 */
	private void openGame() {
		try {
			File file = new File("MyneSweeper/savedGame.txt");
			if(file.exists()) {
				FileInputStream fstream = new FileInputStream(file);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				
				//reset difficulty
				String line = br.readLine();
				int endIndex = line.indexOf(' ');
				String difficulty = line.substring(0, endIndex);
				setData(difficulty);
				
				if((line = br.readLine()) != null) { //game was started
					//restore buttons array
					for(int i = 0; i < Data.rows * Data.cols; i++) {
						String[] buttonData = line.split(" ");
						MynePanel.getButton(i).hasMine = Boolean.parseBoolean(buttonData[0]);
						MynePanel.getButton(i).proximity = Integer.parseInt(buttonData[1]);
						MynePanel.getButton(i).setEnabled(Boolean.parseBoolean(buttonData[2]));
						if(MynePanel.getButton(i).proximity != 0 && !MynePanel.getButton(i).isEnabled()) {
							JLabel label = new JLabel("" + MynePanel.getButton(i).proximity);
							int fontSize = 20;//TODO change size to match frame
							label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, fontSize));
							label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
							label.setAlignmentY(JLabel.CENTER_ALIGNMENT);
							
							//TODO max size of the label is what will fit in button
							int minWidth = (int) (label.getFontMetrics(label.getFont()).getStringBounds(label.getText(), null).getWidth());
							int minHeight = (int) (label.getFontMetrics(label.getFont()).getStringBounds(label.getText(), null).getHeight());
							Dimension min = new Dimension(minWidth, minHeight);
							label.setMinimumSize(min);
							label.setPreferredSize(min);

							MynePanel.getButton(i).add(label);
							MynePanel p = (MynePanel) MynePanel.getButton(i).getParent();
							p.validate();
						}
						line = br.readLine();
					}
					MyneButtonListener.setIsFirstClick(false);
					MyneButtonListener.setNumClicks(Integer.parseInt(line));
				}
				in.close();
			} else {
				JOptionPane.showMessageDialog(this,
						"No saved game to load.",
						"No Save Data",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}
	
	private void openREADME() {
		try {
			File file = new File("README");

			FileInputStream fstream = new FileInputStream(file);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String content = " ";
			String line;
			while((line = br.readLine()) != null) {
				content += line + '\n';
			}
			JOptionPane.showMessageDialog(this,
					content,
					"About",
					JOptionPane.INFORMATION_MESSAGE);

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

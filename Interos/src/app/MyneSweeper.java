package app;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author shuber2
 *
 */
public class MyneSweeper extends JFrame {

	/**
	 * Constructs a frame and adds a panel.
	 */
	MyneSweeper(String difficulty) {
		setData(difficulty);
		setTitle("MyneSweeper");
		int frameWidth = Data.cols * 40;
		int frameHeight = Data.rows * 40;
		Dimension d = new Dimension(frameWidth, frameHeight);
		setMinimumSize(d);
		//TODO format panel size
		getContentPane().add(new MynePanel(frameWidth));
		
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	//TODO add menu, save, load, change difficulty
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

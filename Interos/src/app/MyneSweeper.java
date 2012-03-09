package app;

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
		setSize(Data.rows * 40, Data.cols * 40);//TODO fix sizes, fit panel neatly in frame
		getContentPane().add(new MynePanel());
		setResizable(false);//TODO, resize font and bombs dynamically
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	private void setData(String difficulty) {
		if(difficulty.equals("easy")){
			Data.rows = 8;
			Data.cols = 8;
			Data.numBombs = 10;
		} else if(difficulty.equals("medium")) {
			Data.rows = 16;
			Data.cols = 16;
			Data.numBombs = 40;
		}else {
			Data.rows = 24;
			Data.cols = 24;
			Data.numBombs = 100;
		}
	}
}
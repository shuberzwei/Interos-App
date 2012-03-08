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
	MyneSweeper() {
		setTitle("MyneSweeper");
		setSize(Data.rows * 40, Data.cols * 40);
		getContentPane().add(new MynePanel());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
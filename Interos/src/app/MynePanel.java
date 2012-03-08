package app;

import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * @author shuber2
 *
 */
class MynePanel extends JPanel {
	private static MyneButton[] buttons;

	/**
	 * Constructs a panel and adds buttons with listeners.
	 */
	MynePanel() {
		buttons = new MyneButton[Data.rows * Data.cols];
		MyneButtonListener listener = new MyneButtonListener();
		setLayout(new GridLayout(Data.rows, Data.cols));
		
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			buttons[i] = new MyneButton();
			buttons[i].setActionCommand("" + i);
			buttons[i].addActionListener(listener);
			add(buttons[i]);
		}
	}
	
	protected static MyneButton getButton(int id) {
		return buttons[id];
	}
}
package app;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;

/**
 * @author shuber2
 *
 */
class MynePanel extends JPanel {
	private static MyneButton[] buttons;

	/**
	 * Constructs a panel and adds buttons with action listeners.
	 */
	MynePanel(int dimension) {
		buttons = new MyneButton[Data.rows * Data.cols];
		MyneButtonListener listener = new MyneButtonListener();
		setLayout(new GridLayout(Data.rows, Data.cols));
		//set panel size
		Dimension d1 = new Dimension(dimension, dimension);
		setMinimumSize(d1);
		
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			buttons[i] = new MyneButton(i);
			
			//TODO format button size
			//set button size
			int buttonDimension = dimension / Data.cols;
			Dimension d2 = new Dimension(buttonDimension, buttonDimension);
			buttons[i].setMinimumSize(d2);

			buttons[i].setActionCommand("" + i);
			buttons[i].addActionListener(listener);
			add(buttons[i]);
		}
	}
	
	/**
	 * Reset the buttons array
	 */
	protected void resetButtons(int answer) {
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			if(answer == 0) { //new game
				buttons[i].hasMine = false;
				buttons[i].proximity = 0;
			}
			buttons[i].removeAll(); //remove proximity labels
			buttons[i].setEnabled(true);
		}
	}
	
	/**
	 * @param id
	 * @return button with given id
	 */
	protected static MyneButton getButton(int id) {
		return buttons[id];
	}
	
	protected static String buttonsToString() {
		String buttonString = "";
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			buttonString += buttons[i].hasMine + " ";
			buttonString += buttons[i].proximity + " ";
			buttonString += buttons[i].isEnabled() + " ";
		}
		return buttonString;
	}
}

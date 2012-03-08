package app;

import javax.swing.JButton;

class MyneButton extends JButton {

	protected boolean hasBomb;
	protected int proximity;
	
	MyneButton() {
		hasBomb = false;
		proximity = 0;
		//TODO format buttons
	}
}
package app;

import javax.swing.JButton;

class MyneButton extends JButton {

	protected boolean hasBomb;
	protected int proximity;
	protected int id;
	
	MyneButton(int id) {
		hasBomb = false;
		proximity = 0;
		this.id = id;
		//TODO format buttons
	}
}
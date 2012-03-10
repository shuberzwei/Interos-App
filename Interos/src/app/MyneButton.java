package app;

import java.awt.Insets;

import javax.swing.JButton;

class MyneButton extends JButton {

	protected boolean hasMine;
	protected int proximity;
	protected int id;
	
	MyneButton(int id) {
		hasMine = false;
		proximity = 0;
		this.id = id;
		this.setMargin(new Insets(0, 0, 0, 0));
	}
}
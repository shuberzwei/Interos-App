package app;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import javax.swing.JOptionPane;

class MyneButtonListener implements ActionListener{
    private boolean isFirstClick;
	
	protected MyneButtonListener() {
		isFirstClick = true;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(isFirstClick) { //first click cannot be bomb
			isFirstClick = false;

			ArrayList<Integer> bombs = createBombs(e);
			initializeButtons(bombs);
		}
		showButton(e);
	}

	/**
	 * If button has a bomb, end game.
	 * If button has adjacent bombs show the number.
	 * Else show all adjacent buttons without bombs.
	 * 
	 * @param e used to get button that was pressed
	 */
	private void showButton(ActionEvent e) {
		MyneButton button = (MyneButton) e.getSource();
		if(button.hasBomb) {
			button.setEnabled(false);
			//show bomb
			int answer = JOptionPane.showConfirmDialog(button.getParent(), "Play again?", "Game Over", JOptionPane.YES_NO_OPTION);
			if(answer == 1)
				System.exit(0);
			else{
				MyneSweeper t = new MyneSweeper();
			}
		}
		else if(button.proximity != 0) {
			button.setEnabled(false);
			//show proximity			
			button.setText("" + button.proximity);
		}
		else {
			button.setEnabled(false);
			//depress all adjacent no bomb buttons
		}
	}

	/**
	 * Creates bombs, bomb position is 0 to (row * column) - 1.
	 * 
	 * @param e used to get first button pressed
	 * @return sorted ArrayList of bomb positions
	 */
	private ArrayList<Integer> createBombs(ActionEvent e) {
		ArrayList<Integer> bombs = new ArrayList<Integer>(Data.numBombs);

		Random rand = new Random();
		for(int i = 0; i < Data.numBombs; i++) {
			int pos = rand.nextInt(Data.rows * Data.cols);
			if(bombs.contains(pos) || pos == Integer.parseInt(e.getActionCommand())) //e.getActionCommand() is button number
				i--;
			else
				bombs.add(pos);
		}
		Collections.sort(bombs);
		return bombs;
	}
	
	/**
	 * Initializes buttons with the given bomb positions.
	 * 
	 * @param bombs
	 */
	private void initializeButtons(ArrayList<Integer> bombs) {
		int bombListPos = 0;
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			if(bombListPos < bombs.size()) {
				if(bombs.get(bombListPos) == i) {
					MynePanel.getButton(i).hasBomb = true;
					bombListPos++;
				}
			}
		}
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			if(MynePanel.getButton(i).hasBomb) {
				MynePanel.getButton(i).setBackground(Color.GRAY);//TODO

			}
			if(!MynePanel.getButton(i).hasBomb) {
				//TODO initialize proximities, only use getButton if prox != 0
				MynePanel.getButton(i).proximity = 1;
			}
		}
	}
}

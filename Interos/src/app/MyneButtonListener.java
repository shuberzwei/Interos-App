package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

class MyneButtonListener implements ActionListener{
    private boolean isFirstClick;
    private int numClicks;
	
	protected MyneButtonListener() {
		isFirstClick = true;
		numClicks = 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(isFirstClick) { //first click cannot be mine
			isFirstClick = false;
			ArrayList<Integer> mines = createMines(e);
			initializeButtons(mines);
		}
		showButton(e);
		if(numClicks == Data.rows * Data.cols - Data.numMines) {
			endGame("YOU WIN!", (MyneButton) e.getSource());
		}
	}

	/**
	 * If button has a mine, end game.
	 * If button has adjacent mines show the proximity number.
	 * Else show all adjacent buttons without mines.
	 * 
	 * @param e used to get button that was pressed
	 */
	private void showButton(ActionEvent e) {
		MyneButton button = (MyneButton) e.getSource();
		if(button.hasMine) {
			button.setEnabled(false);
			//show mine
			
			//TODO format size of mine icon to match size of frame
			JLabel label = new JLabel(new ImageIcon("mine30p.gif"), JLabel.CENTER);
			Dimension d = new Dimension();
			button.getParent().getParent().getSize(d);
			d.setSize((d.getWidth() / Data.cols) * 0.9, (d.getHeight() / Data.rows) * 0.9);
			label.setMinimumSize(d);
			label.setMaximumSize(d);
			label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
			label.setAlignmentY(JLabel.CENTER_ALIGNMENT);
			
			button.add(label);
			MynePanel p = (MynePanel) button.getParent();
			p.validate();
			
			endGame("GAME OVER", button);
		} else if(button.proximity != 0) {
			button.setEnabled(false);
			numClicks++;
			showProximity(button);
		} else
			depressButtons(button);
	}
	
	/**
	 * Shows dialog to end game
	 * @param message
	 * @param button used to get panel
	 */
	private void endGame(String message, MyneButton button) {
		Object[] options = {"Yes",
                			"No",
							"Replay"};
		int answer = JOptionPane.showOptionDialog(button.getParent(),
													"Play again?",
													message,
													JOptionPane.YES_NO_CANCEL_OPTION,
													JOptionPane.PLAIN_MESSAGE,
													null,
													options,
													options[2]);
		if(answer != 1) { //if answer not "No"
			if(answer == 0)
				isFirstClick = true; //new game
			else
				JOptionPane.showMessageDialog(button.getParent(),
											"The first click is no longer \"safe!\"",
											"Replay",
											JOptionPane.WARNING_MESSAGE);
			numClicks = 0;
			MynePanel p = (MynePanel) button.getParent();
			p.resetButtons(answer);//TODO menu option to reset Data
		}
		else
			System.exit(0);
	}
	
	/**
	 * Calculates mine proximity for a given button
	 * 
	 * @param button
	 */
	private void showProximity(MyneButton button) {
		JLabel label = new JLabel("" + button.proximity);
		label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 20));//TODO change size to match frame
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		label.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		
		//TODO max size of the label is what will fit in button
		int minWidth = (int) (label.getFontMetrics(label.getFont()).getStringBounds(label.getText(), null).getWidth());
		int minHeight = (int) (label.getFontMetrics(label.getFont()).getStringBounds(label.getText(), null).getHeight());
		Dimension min = new Dimension(minWidth, minHeight);
		label.setMinimumSize(min);
		label.setPreferredSize(min);

		button.add(label);
		MynePanel p = (MynePanel) button.getParent();
		p.validate();
	}
	
	/**
	 * Depresses all buttons adjacent to the given button, and all buttons adjacent to those, with proximity 0, and depresses all buttons encircling the depressed buttons.
	 * 
	 * @param button
	 */
	private void depressButtons(MyneButton button) { //TODO does Minesweeper depress buttons on the diagonal?
		if(button.isEnabled()) {
			button.setEnabled(false);
			numClicks++;
			if(button.proximity == 0) {
				int i = button.id;
				int cols = Data.cols, max = Data.rows * cols - 1;
				boolean top = false, bot = false, left = false, right = false; //refers to buttons position in grid

				if (i < cols)
					top = true;
				if ((i + cols) > max)
					bot = true;
				if (i % cols == 0)
					left = true;
				if (i % cols == (cols - 1))
					right = true;

				if (!top && !left)
					depressButtons(MynePanel.getButton(i - 1 - cols));
				if (!top)
					depressButtons(MynePanel.getButton(i - cols));
				if (!top && !right)
					depressButtons(MynePanel.getButton(i + 1 - cols));
				if (!left)
					depressButtons(MynePanel.getButton(i - 1));
				if (!right)
					depressButtons(MynePanel.getButton(i + 1));
				if (!left && !bot)
					depressButtons(MynePanel.getButton(i - 1 + cols));
				if (!bot)
					depressButtons(MynePanel.getButton(i + cols));
				if (!right && !bot)
					depressButtons(MynePanel.getButton(i + 1 + cols));
			}
			else
				showProximity(button);
		}
	}

	/**
	 * Creates mines, mine position is from 0 to (row * column) - 1.
	 * 
	 * @param e used to get first button pressed
	 * @return ArrayList of mine positions
	 */
	private ArrayList<Integer> createMines(ActionEvent e) {
		ArrayList<Integer> mines = new ArrayList<Integer>(Data.numMines);

		Random rand = new Random();
		for(int i = 0; i < Data.numMines; i++) {
			int pos = rand.nextInt(Data.rows * Data.cols);
			if(mines.contains(pos) || pos == Integer.parseInt(e.getActionCommand())) //e.getActionCommand() is button number
				i--;
			else
				mines.add(pos);
		}
		return mines;
	}
	
	/**
	 * Initializes buttons with the given mine positions.
	 * 
	 * @param mines
	 */
	private void initializeButtons(ArrayList<Integer> mines) {
		int buttonNum = 0;
		for(int i : mines) {
			buttonNum = i;
			MynePanel.getButton(buttonNum).hasMine = true;
		}
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			if(MynePanel.getButton(i).hasMine) {
				
				//MynePanel.getButton(i).setBackground(Color.GRAY);//TODO
			}
			if(!MynePanel.getButton(i).hasMine) { //calculate proximity for button i
				 /*
				  * i-1-cols  i-cols  i+1-cols
				  * i-1         i        i+1
				  * i-1+cols  i+cols  i+1+cols
				  */
				int count = 0, cols = Data.cols, max = Data.rows * cols - 1;
				boolean top = false, bot = false, left = false, right = false; //refers to buttons position in grid

				if(i < cols)
					top = true;
				if((i + cols) > max)
					bot = true;
				if(i % cols == 0)
					left = true;
				if(i % cols == (cols - 1))
					right = true;

				if(!top && !left && MynePanel.getButton(i - 1 - cols).hasMine)
					count++;
				if(!top && MynePanel.getButton(i - cols).hasMine)
					count++;
				if(!top && !right && MynePanel.getButton(i + 1 - cols).hasMine)
					count++;
				if(!left && MynePanel.getButton(i - 1).hasMine)
					count++;
				if(!right && MynePanel.getButton(i + 1).hasMine)
					count++;
				if(!left && !bot && MynePanel.getButton(i - 1 + cols).hasMine)
					count++;
				if(!bot && MynePanel.getButton(i + cols).hasMine)
					count++;
				if(!right && !bot && MynePanel.getButton(i + 1 + cols).hasMine)
					count++;

				if(count != 0)
					MynePanel.getButton(i).proximity = count;
			}
		}
	}
}

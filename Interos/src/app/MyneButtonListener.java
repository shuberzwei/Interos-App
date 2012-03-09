package app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

class MyneButtonListener implements ActionListener{
    private boolean isFirstClick;
    private int numClicks;
	
	protected MyneButtonListener() {
		isFirstClick = true;
		numClicks = 0;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(isFirstClick) { //first click cannot be bomb
			isFirstClick = false;
			ArrayList<Integer> bombs = createBombs(e);
			initializeButtons(bombs);
		}
		showButton(e);
		if(numClicks == Data.rows * Data.cols - Data.numBombs) {
			endGame("You win!", (MyneButton) e.getSource());
		}
	}

	/**
	 * If button has a bomb, end game.
	 * If button has adjacent bombs show the proximity number.
	 * Else show all adjacent buttons without bombs.
	 * 
	 * @param e used to get button that was pressed
	 */
	private void showButton(ActionEvent e) {
		MyneButton button = (MyneButton) e.getSource();
		if(button.hasBomb) {/*
			button.setEnabled(false);
			//show bomb
			
			
			
			//TODO format size of bomb icon to match size of frame
			
			JLabel label = new JLabel("T", new ImageIcon("tinyMine.gif"), JLabel.CENTER);
			Dimension d = new Dimension();
			button.getParent().getParent().getSize(d);
			d.setSize((d.getWidth() / Data.cols) * 0.7, (d.getHeight() / Data.rows) * 0.7);
			label.setPreferredSize(d);
			label.setMaximumSize(d);
			label.setSize(d);
			//System.out.println(d.getHeight());
			//System.out.println(d.getWidth());

			button.add(label);
			//button.setIcon(icon);
			//System.out.println(button.getComponent(0));

			MynePanel p = (MynePanel) button.getParent();
			p.validate();
			
label.setSize(200, 200);
			
			JFrame f = new JFrame();
			JButton j = new JButton();
			//Icon icon = new ImageIcon("tinyMine.gif");
			JPanel panel = new JPanel();
			j.add(label);
			System.out.println(j.getComponent(0));
			panel.add(j);
			f.add(panel);
			f.setSize(400, 400);
			
			//j.setSize(100, 100);
			panel.validate();
			f.setVisible(true);*/
			
			endGame("Game Over", button);
		}
		else if(button.proximity != 0) {
			button.setEnabled(false);
			numClicks++;
			//show proximity
			JLabel label = new JLabel("" + button.proximity);
			label.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 8));//TODO change size to match frame

			int minWidth = (int) (label.getFontMetrics(label.getFont()).getStringBounds(label.getText(), null).getWidth());
			int minHeight = (int) (label.getFontMetrics(label.getFont()).getStringBounds(label.getText(), null).getHeight());
			
			//TODO max size of the label is what will fit in button

			Dimension min = new Dimension(minWidth, minHeight);

			label.setMinimumSize(min);
			label.setPreferredSize(min);

			button.add(label);

			MynePanel p = (MynePanel) button.getParent();
			p.validate();
		}
		else
			depressButtons(button);
	}
	
	private void endGame(String message, MyneButton button) {
		int answer = JOptionPane.showConfirmDialog(button.getParent(), "Play again?", message, JOptionPane.YES_NO_OPTION);
		if(answer == 1)
			System.exit(0);
		else {
			isFirstClick = true;
			numClicks = 0;
			MynePanel p = (MynePanel) button.getParent();
			p.resetButtons();//TODO reset Data
		}
	}
	
	/**
	 * Depresses all buttons adjacent to the given button, and all buttons adjacent to those, with proximity 0.
	 * 
	 * @param button
	 */
	private void depressButtons(MyneButton button) {
		if(button.isEnabled()) {
			button.setEnabled(false);
			numClicks++;
			int i = button.id;
			int cols = Data.cols, max = Data.rows * cols - 1;
			boolean top = false, bot = false, left = false, right = false; //refers to buttons position in grid

			if(i < cols)
				top = true;
			if((i + cols) > max)
				bot = true;
			if(i % cols == 0)
				left = true;
			if(i % cols == (cols - 1))
				right = true;

			if(!top && !left && MynePanel.getButton(i - 1 - cols).proximity == 0)
				depressButtons(MynePanel.getButton(i - 1 - cols));
			if(!top && MynePanel.getButton(i - cols).proximity == 0)
				depressButtons(MynePanel.getButton(i - cols));
			if(!top && !right && MynePanel.getButton(i + 1 - cols).proximity == 0)
				depressButtons(MynePanel.getButton(i + 1 - cols));
			if(!left && MynePanel.getButton(i - 1).proximity == 0)
				depressButtons(MynePanel.getButton(i - 1));
			if(!right && MynePanel.getButton(i + 1).proximity == 0)
				depressButtons(MynePanel.getButton(i + 1));
			if(!left && !bot && MynePanel.getButton(i - 1 + cols).proximity == 0)
				depressButtons(MynePanel.getButton(i - 1 + cols));
			if(!bot && MynePanel.getButton(i + cols).proximity == 0)
				depressButtons(MynePanel.getButton(i + cols));
			if(!right && !bot && MynePanel.getButton(i + 1 + cols).proximity == 0)
				depressButtons(MynePanel.getButton(i + 1 + cols));
		}
	}

	/**
	 * Creates bombs, bomb position is from 0 to (row * column) - 1.
	 * 
	 * @param e used to get first button pressed
	 * @return ArrayList of bomb positions
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
		return bombs;
	}
	
	/**
	 * Initializes buttons with the given bomb positions.
	 * 
	 * @param bombs
	 */
	private void initializeButtons(ArrayList<Integer> bombs) {
		int buttonNum = 0;
		for(int i : bombs) {
			buttonNum = i;
			MynePanel.getButton(buttonNum).hasBomb = true;
		}
		for(int i = 0; i < Data.rows * Data.cols; i++) {
			if(MynePanel.getButton(i).hasBomb) {
				MynePanel.getButton(i).setBackground(Color.GRAY);//TODO
			}
			if(!MynePanel.getButton(i).hasBomb) { //calculate proximity for button i
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

				if(!top && !left && MynePanel.getButton(i - 1 - cols).hasBomb)
					count++;
				if(!top && MynePanel.getButton(i - cols).hasBomb)
					count++;
				if(!top && !right && MynePanel.getButton(i + 1 - cols).hasBomb)
					count++;
				if(!left && MynePanel.getButton(i - 1).hasBomb)
					count++;
				if(!right && MynePanel.getButton(i + 1).hasBomb)
					count++;
				if(!left && !bot && MynePanel.getButton(i - 1 + cols).hasBomb)
					count++;
				if(!bot && MynePanel.getButton(i + cols).hasBomb)
					count++;
				if(!right && !bot && MynePanel.getButton(i + 1 + cols).hasBomb)
					count++;

				if(count != 0)
					MynePanel.getButton(i).proximity = count;
			}
		}
	}
}

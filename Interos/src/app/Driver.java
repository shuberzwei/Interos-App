package app;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


/**
 * @author shuber2
 *
 */
public class Driver {
	public static void main(String[] args) {
		Object[] options = {"easy", "medium", "hard"};
		String choice = (String) JOptionPane.showInputDialog(
		                    null,
		                    "Choose your difficulty level:\n",
		                    "MyneSweeper",
		                    JOptionPane.PLAIN_MESSAGE,
		                    new ImageIcon("app/tinyMine.gif"),
		                    options,
		                    "easy");
		if(choice != null) {
			MyneSweeper m = new MyneSweeper(choice);
		}
	}
}

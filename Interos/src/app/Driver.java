package app;

import java.applet.Applet;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Driver extends Applet {
	public static void main(String[] args) {

		try {
			String dirName ="MyneSweeper";
			File file = new File(dirName);
			if(!file.exists())
				file.mkdir();
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}

		Object[] options = {"easy (" + Data.easyRows + "x" + Data.easyCols + ", " + Data.easyNumMines + " mines)",
							"medium (" + Data.medRows + "x" + Data.medCols + ", " + Data.medNumMines + " mines)",
							"hard (" + Data.hardRows + "x" + Data.hardCols + ", " + Data.hardNumMines + " mines)"};
		
		String choice = (String) JOptionPane.showInputDialog(
		                    null,
		                    "Choose your difficulty level:\n",
		                    "MyneSweeper",
		                    JOptionPane.PLAIN_MESSAGE,
		                    new ImageIcon("mine50p.gif"),
		                    options,
		                    options[0]);
		if(choice != null) {
			MyneSweeper m = new MyneSweeper(choice);
		}
	}
}

package levelBuilder;

import javax.swing.JOptionPane;

public class DialogMaker {
	public static void main(String[] args) {
		String fileName = "resources/dialogs/" + JOptionPane.showInputDialog(null, "Dialog tree name", "", JOptionPane.PLAIN_MESSAGE);
		
	}
}

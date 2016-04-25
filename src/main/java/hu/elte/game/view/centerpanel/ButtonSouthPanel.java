/**
 * 
 */
package hu.elte.game.view.centerpanel;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Dago
 *
 */
public class ButtonSouthPanel extends JPanel{
	public ButtonSouthPanel() {
		setLayout(new GridLayout(1, 4));
		createButtons();
	}
	
	private void createButtons() {
		add(new JButton("Sell"), 0);
		add(new JButton("Buy"), 1);
		add(new JButton("Mortage"), 2);
		add(new JButton("Unmortage"), 3);
	}
}

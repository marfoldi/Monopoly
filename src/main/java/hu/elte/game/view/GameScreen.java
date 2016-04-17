/**
 * 
 */
package hu.elte.game.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

/**
 * @author marfoldi
 *
 */
public class GameScreen extends JFrame {
	public GameScreen() {
		setTitle("Monpoly");
		setBackground(Color.decode("#c0e2ca"));
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout(0,0));
	}
}

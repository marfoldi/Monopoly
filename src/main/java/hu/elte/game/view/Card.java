/**
 * 
 */
package hu.elte.game.view;

import javax.swing.JButton;
import javax.swing.JDialog;

/**
 * @author marfoldi
 *
 */
public class Card extends JDialog {
	private String title;
	private String message;
	private static final JButton okButton = new JButton("OK");
	
	/**
	 * @param title
	 * @param message
	 */
	public Card(String title, String message) {
		super();
		this.title = title;
		this.message = message;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}

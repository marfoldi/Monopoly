/**
 * 
 */
package hu.elte.game.view;

import java.awt.Image;

/**
 * @author marfoldi
 *
 */
public class Player {
	private Image img;

	/**
	 * @param img
	 */
	public Player(Image img) {
		super();
		this.img = img;
	}

	/**
	 * @return the img
	 */
	public Image getImg() {
		return img;
	}

	/**
	 * @param img the img to set
	 */
	public void setImg(Image img) {
		this.img = img;
	}
}

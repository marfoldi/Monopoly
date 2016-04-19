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
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

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

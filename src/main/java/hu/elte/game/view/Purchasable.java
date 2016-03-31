/**
 * 
 */
package hu.elte.game.view;

import java.awt.Image;

import javax.swing.GrayFilter;

/**
 * @author marfoldi
 *
 */
public class Purchasable extends Field {
	private Player owner;
	private int price;
	private Image oldImage;

	/**
	 * 
	 * @param name
	 * @param img
	 * @param owner
	 * @param price
	 */
	public Purchasable(String name, Image img, Player owner, int price) {
		super(name, img);
		this.owner = owner;
		this.price = price;
	}
	
	public void displayOwner(Player owner) {
		//TODO: Manipulate the UI here...
	}
	
	/**
	 * Applies a gray filter on the image if the field is under mortgage
	 * @param isMortage
	 */
	public void setMortgage(boolean isMortage) {
		if(isMortage) {
			oldImage = img;
			GrayFilter.createDisabledImage(img);
		}
		else {
			img = oldImage;
		}
	}

	/**
	 * @return the owner
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(Player owner) {
		this.owner = owner;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}
}

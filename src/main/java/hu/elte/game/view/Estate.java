/**
 * 
 */
package hu.elte.game.view;

import java.awt.Color;
import java.awt.Image;

/**
 * @author marfoldi
 *
 */
public class Estate extends Purchasable {
	private Color city;
	private int houseCount;

	/**
	 * 
	 * @param name
	 * @param img
	 * @param owner
	 * @param price
	 * @param city
	 */
	public Estate(String name, Image img, Player owner, int price, Color city) {
		super(name, img, owner, price);
		this.city = city;
		this.houseCount = 0;
	}
	
	/**
	 * Adds a building to the estate
	 */
	public void addBuilding() {
		houseCount++;
		if(houseCount>=5) {
			//TODO: Should display a hotel
		}
	}
	
	/**
	 * Removes a building from the estate
	 */
	public void removeBuilding() {
		houseCount--;
	}

}

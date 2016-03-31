/**
 * 
 */
package hu.elte.game.view;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

/**
 * @author marfoldi
 *
 */
public class Field {
	protected String name;
	protected Image img;
	protected List<Player> players;
	
	/**
	 * Constructor for the Start field
	 * @param name
	 * @param img
	 * @param players
	 */
	public Field(String name, Image img, List<Player> players) {
		super();
		this.name = name;
		this.img = img;
		this.players = players;
	}
	
	/**
	 * Constructor for the fields except the Start
	 * @param name
	 * @param img
	 */
	public Field(String name, Image img) {
		super();
		this.name = name;
		this.img = img;
		this.players = new ArrayList<>();
	}
	
	/**
	 * Adds a player to the field
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	
	/**
	 * Removes a player from the field
	 * @param player
	 */
	public void removePlayer(Player player) {
		this.players.remove(player);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}
}

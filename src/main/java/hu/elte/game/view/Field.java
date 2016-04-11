/**
 * 
 */
package hu.elte.game.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author marfoldi
 *
 */
public class Field extends JButton {
	protected String name;
	protected Icon img;
	protected List<Player> players;
	
	/**
	 * Constructor for the Start field
	 * @param name
	 * @param img
	 * @param players
	 */
	public Field(String name, Icon img, List<Player> players) {
		super();
		this.name = name;
		this.img = img;
		this.players = players;
		this.setText(name);
		this.setIcon(img);
	}
	
	/**
	 * Constructor for the fields except the Start
	 * @param name
	 * @param img
	 */
	public Field(String name, Icon img) {
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

package hu.elte.game.logic;

import java.util.ArrayList;

/**
 * This class represents a set of changes made in the model
 * Used to communicate with the Controller and the other Players through the network
 *
 */
public class ChangeSet {
	// However the class' name indicates it is a set, there can be duplicate elements in one 'changeSet'
	private ArrayList<Change> changeSet;
	
	/**
	 * This enumeration determines the structure of the data it contains
	 * 
	 * PLAYER:
	 *  - action can be: MONEY
	 *  #MONEY
	 *  - detail is    : the name of the Player (string)
	 *  - extra is     : the amount of change (integer)
	 *  
	 *  FIELD:
	 *  - action can be: OWNER, MORTGAGE
	 *  #OWNER
	 *  - detail is    : the new owner's name (string)
	 *  - extra is     : the estate's name (string)
	 *  #MORTGAGE
	 *  - detail is    : the estate's name (string)
	 *  - extra is     : (boolean)
	 *  
	 *  SYSTEM:
	 *  - action can be: ROLL, CARD
	 *  #ROLL
	 *  - detail is    : the numbers rolled in a comma separated format (string) example with 2 dices: "5,2"
	 *  #CARD
	 *  - detail is    : the id of the card
	 *
	 */
	public enum ACTOR {
		PLAYER, FIELD, SYSTEM
	}
	
	public enum ACTION {
		ROLL, MONEY, OWNER, CARD, MORTGAGE
	}
	
	public ChangeSet() {
		this.changeSet = new ArrayList<Change>();
	}
	
	public void add(ACTOR actor, ACTION action, String detail) {
		this.changeSet.add(new Change(actor, action, detail));
	}
	
	public void add(Change change) {
		this.changeSet.add(change);
	}
	
	public static class Change {
		private ACTOR actor;
		private ACTION action;
		private String detail;
		private String extra;
		
		public Change (ACTOR actor, ACTION action, String detail) {
			this.actor = actor;
			this.action = action;
			this.detail = detail;
		}
		
		public Change (ACTOR actor, ACTION action, String detail, String extra) {
			this(actor, action, detail);
			this.extra = extra;
		}
	}
}

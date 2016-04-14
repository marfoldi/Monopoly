package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.javatuples.Pair;

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
	 *  - action can be: MONEY, STEP
	 *  #MONEY
	 *  - detail is    : the name of the Player (string)
	 *  - extra is     : the amount of change (integer)
	 *  #STEP
	 *  - detail is    : the player's new position on the table (integer)
	 *  
	 *  FIELD:
	 *  - action can be: OWNER, HOUSE, MORTGAGE
	 *  #OWNER
	 *  - detail is    : the new owner's name (string)
	 *  - extra is     : the estate's name (string)
	 *  #MORTGAGE
	 *  - detail is    : the estate's name (string)
	 *  - extra is     : (boolean)
	 *  #HOUSE
	 *  - detail is    : the estate's name (string)
	 *  - extra is     : unit change (integer) ideally +1 / -1
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
		ROLL, MONEY, OWNER, CARD, MORTGAGE, HOUSE, STEP
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
	
	/**
	 * Gets all the changes contained in the changeSet
	 * @return
	 */
	public ArrayList<Change> getChangeSet() {
		return this.changeSet;
	}
	
	/**
	 * Gets the changes that matches the parameters
	 * @param actor
	 * @param action
	 * @return Tuple<String, String>
	 */
	public ArrayList<Pair<String, String>> getChangesFor(ACTOR actor, ACTION action) {
		ArrayList<Pair<String, String>> retVal = new ArrayList<Pair<String, String>>();
		
		// Filter the changes according to the parameters
		Stream<Change> changeStream = this.changeSet.stream();
		Stream<Change> filteredStream = changeStream.filter(change -> change.getActor().equals(actor) && change.getAction().equals(action));
		
		// Add the filtered changes to the retVal in TUPLE format 
		filteredStream.forEach(item -> retVal.add(new Pair<String, String>(item.getDetail(), item.getExtra())));
		
		return retVal;		
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
		
		public ACTOR getActor() {
			return this.actor;
		}
		
		public ACTION getAction() {
			return this.action;
		}
		
		public String getDetail() {
			return this.detail;
		}
		
		public String getExtra() {
			return this.extra;
		}
	}
}

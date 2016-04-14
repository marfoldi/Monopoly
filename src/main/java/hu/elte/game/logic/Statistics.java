package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang.StringUtils;

import hu.elte.game.logic.ChangeSet.ACTION;
import hu.elte.game.logic.ChangeSet.ACTOR;

public class Statistics {
	
	public enum Key {
		RENT, PURCHASE, PAYMENT, SALE, INCOME, MORTGAGE, CARD, HOUSE, ROLL, STEP
	}
	
	private HashMap<String, ArrayList<Statistic>> playerStatistics;
	private HashMap<String, ArrayList<Statistic>> fieldStatistics;
	
	private ArrayList<ChangeSet.Change> changes;
	
	public Statistics() {
		this.playerStatistics = new HashMap<String, ArrayList<Statistic>>();
		this.fieldStatistics = new HashMap<String, ArrayList<Statistic>>();
		this.changes = new ArrayList<ChangeSet.Change>();
	}
	
	/**
	 * Logs an  estate's owner change.
	 * Does not log the income / payment that comes with the change,
	 * for that use the 'transaction' method 
	 * @param oldOwner
	 * @param newOwner
	 * @param estate
	 * @param value
	 */
	public void estateOwnerChange(String oldOwner, String newOwner, String estate, int value) {
		Statistic statistic;
		
		// Log the oldOwner's SALE action
		checkPS(oldOwner);
		statistic = new Statistic(Key.SALE, newOwner, Integer.toString(value));
		this.playerStatistics.get(oldOwner).add(statistic);
		
		// Log the newOwner's PURCHASE action
		checkPS(newOwner);
		statistic = new Statistic(Key.PURCHASE, oldOwner, Integer.toString(value));
		this.playerStatistics.get(newOwner).add(statistic);
		
		// Reset the field's action history
		checkFS(estate);
		this.fieldStatistics.get(estate).clear();
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.FIELD, ACTION.OWNER, newOwner, estate));
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.MONEY, newOwner, Integer.toString(value * -1)));
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.MONEY, oldOwner, Integer.toString(value)));
	}
	
	/**
	 * Logs a rent action.
	 * @param guest
	 * @param owner
	 * @param estate
	 * @param value
	 */
	public void rentPayment(String guest, String owner, String estate, int value) {
		Statistic statistic;
		
		// Log the guest's RENT action
		checkPS(guest);
		statistic = new Statistic(Key.RENT, owner, estate, Integer.toString(value));
		this.playerStatistics.get(guest).add(statistic);
		
		// Log the owner's INCOME action
		checkPS(owner);
		statistic = new Statistic(Key.INCOME, guest, estate, Integer.toString(value));
		this.playerStatistics.get(owner).add(statistic);
		
		// Log the field's INCOME action
		checkFS(estate);
		statistic = new Statistic(Key.INCOME, guest, Integer.toString(value));
		this.fieldStatistics.get(estate).add(statistic);
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.MONEY, guest, Integer.toString(value * -1)));
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.MONEY, owner, Integer.toString(value)));
	}
	
	/**
	 * Logs a field's mortgage change
	 * @param owner
	 * @param estate
	 * @param isUnderMortgage
	 */
	public void mortgageSet(String owner, String estate, boolean isUnderMortgage, int value) {
		Statistic statistic;
		
		// Log the owner's MORTGAGE action
		checkPS(owner);
		statistic = new Statistic(Key.MORTGAGE, estate, Boolean.toString(isUnderMortgage));
		this.playerStatistics.get(owner).add(statistic);
		
		// Log the owner's INCOME / PAYMENT action depending on the state of 'isUnderMortgage'
		String from = isUnderMortgage ? null : owner;
		String to = isUnderMortgage ? owner : null;
		transaction(from, to, "Mortgage on: " + estate, value);
		
		// Log the field's MORTGAGE action
		checkFS(estate);
		statistic = new Statistic(Key.MORTGAGE, owner, Boolean.toString(isUnderMortgage));
		this.fieldStatistics.get(estate).add(statistic);
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.FIELD, ACTION.MORTGAGE, estate, Boolean.toString(isUnderMortgage)));
	}
	
	/**
	 * Logs a field's house count property change
	 * @param estate
	 * @param diff
	 */
	public void houseChange(String estate, int diff) {
		Statistic statistic;
		
		// Log the field's HOUSE action
		checkFS(estate);
		statistic = new Statistic(Key.HOUSE, Integer.toString(diff));
		this.fieldStatistics.get(estate).add(statistic);
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.FIELD, ACTION.HOUSE, estate, Integer.toString(diff)));
	}
	
	/**
	 * Logs a dice roll action
	 * @param player
	 * @param dice
	 */
	public void rollDice(String player, IDice dice) {
		Statistic statistic;
		
		// Get the comma separated numbers from dice
		String formattedDice = StringUtils.join(dice.getValues(), ",");
		
		// Log the player's ROLL action
		checkPS(player);
		statistic = new Statistic(Key.ROLL, formattedDice);
		this.playerStatistics.get(player).add(statistic);
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.SYSTEM, ACTION.ROLL, formattedDice));
	}
	
	/**
	 * Logs a position change action
	 * @param player
	 * @param position
	 */
	public void positionChange(String player, int position) {
		Statistic statistic;
		
		// Log the player's STEP action
		checkPS(player);
		statistic = new Statistic(Key.STEP, Integer.toString(position));
		this.playerStatistics.get(player).add(statistic);
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.STEP, Integer.toString(position)));
	}
	
	/**
	 * Logs a transaction between two Players
	 * (in special case one of the Player can be null thus indicating that is the BANK)
	 * @param from
	 * @param to
	 * @param message
	 * @param value
	 */
	public void transaction(String from, String to, String message, int value) {
		if (from == null) from = "BANK";
		if (to == null) to = "BANK";
		Statistic statistic;
		
		checkPS(from);
		statistic = new Statistic(Key.PAYMENT, to, message, Integer.toString(value));
		this.playerStatistics.get(from).add(statistic);
		
		checkPS(to);
		statistic = new Statistic(Key.INCOME, from, message, Integer.toString(value));
		this.playerStatistics.get(to).add(statistic);
		
		// Update the changeSet
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.MONEY, from, Integer.toString(value * -1)));
		this.changes.add(new ChangeSet.Change(ACTOR.PLAYER, ACTION.MONEY, to, Integer.toString(value)));
	}
	
	/**
	 * Gets the total income for the specified field name
	 * @param fieldName
	 * @return
	 */
	public int getIncomeForField(String fieldName) {
		if (!this.fieldStatistics.containsKey(fieldName)) {
			return 0;
		}
		
		Stream<Statistic> fieldStream = this.fieldStatistics.get(fieldName).stream();
		Stream<Integer> incomeStream = fieldStream.map(field -> Integer.parseInt(field.getValue()));
		
		Optional<Integer> sum = incomeStream.reduce((a, b) -> a + b);
		return sum.isPresent() ? sum.get() : 0;
	}
	
	/**
	 * Clears the current ChangeSet
	 */
	public void checkpoint() {
		this.changes.clear();
	}
	
	/**
	 * Gets all changes between two checkpoints
	 * @return
	 */
	public ChangeSet getChangeSet() {
		ChangeSet retVal = new ChangeSet();
		this.changes.stream().forEach(change -> retVal.add(change));
		
		return retVal;
	}
	
	/**
	 * Creates an empty log for the given key, if there isn't any
	 * @param key
	 */
	private void checkPS(String key) {
		if (!this.playerStatistics.containsKey(key)) {
			this.playerStatistics.put(key, new ArrayList<Statistic>());
		}
	}
	
	/**
	 * Creates an empty log for the given key, if there isn't any
	 * @param key
	 */
	private void checkFS(String key) {
		if (!this.fieldStatistics.containsKey(key)) {
			this.fieldStatistics.put(key, new ArrayList<Statistic>());
		}
	}
	
	/**
	 * Represents a statistic element
	 *
	 */
	private class Statistic {
		private Key key;
		private String value;
		private String message;
		private String participant;
		
		public Statistic(Key key, String value) {
			this.key = key;
			this.value = value;
		}
		
		public Statistic(Key key, String participant, String value) {
			this(key, value);
			this.participant = participant;
		}
		
		public Statistic(Key key, String participant, String message, String value) {
			this(key, participant, value);
			this.message = message;
		}
		
		
		public Key getKey() {
			return this.key;
		}
		
		public String getParticipant() {
			return this.participant;
		}
		
		public String getValue() {
			return this.value;
		}
		
		public String getMessage() {
			return this.message;
		}
	}
}

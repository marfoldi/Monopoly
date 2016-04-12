package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.HashMap;

public class Statistics {
	
	public enum Key {
		PURCHASE, RENT, SALE, INCOME, MORTGAGE, CARD
	}
	
	private HashMap<String, ArrayList<Statistic>> playerStatistics;
	private HashMap<String, ArrayList<Statistic>> fieldStatistics;
	
	public Statistics() {
		this.playerStatistics = new HashMap<String, ArrayList<Statistic>>();
		this.fieldStatistics = new HashMap<String, ArrayList<Statistic>>();
	}
	
	/**
	 * Logs an  estate's owner change.
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
		statistic = new Statistic(Key.RENT, owner, Integer.toString(value));
		this.playerStatistics.get(guest).add(statistic);
		
		// Log the owner's INCOME action
		checkPS(owner);
		statistic = new Statistic(Key.INCOME, guest, Integer.toString(value));
		this.playerStatistics.get(owner).add(statistic);
		
		// Log the field's INCOME action
		checkFS(estate);
		statistic = new Statistic(Key.INCOME, guest, Integer.toString(value));
		this.fieldStatistics.get(estate).add(statistic);
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

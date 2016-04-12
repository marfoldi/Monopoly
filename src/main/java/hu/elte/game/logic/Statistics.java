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
		statistic = new Statistic(Key.SALE, newOwner, value);
		this.playerStatistics.get(oldOwner).add(statistic);
		
		// Log the newOwner's PURCHASE action
		checkPS(newOwner);
		statistic = new Statistic(Key.PURCHASE, oldOwner, value);
		this.playerStatistics.get(newOwner).add(statistic);
		
		// Reset the field's action history
		checkFS(estate);
		statistic = new Statistic(Key.PURCHASE, value);
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
		statistic = new Statistic(Key.RENT, owner, value);
		this.playerStatistics.get(guest).add(statistic);
		
		// Log the owner's INCOME action
		checkPS(owner);
		statistic = new Statistic(Key.INCOME, guest, value);
		this.playerStatistics.get(owner).add(statistic);
		
		// Log the field's INCOME action
		checkFS(estate);
		statistic = new Statistic(Key.INCOME, guest, value);
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
		private int value;
		private String player;
		
		public Statistic(Key key, int value) {
			this.key = key;
			this.value = value;
		}
		
		public Statistic(Key key, String player, int value) {
			this(key, value);
			this.player = player;
		}
		
		public Key getKey() {
			return this.key;
		}
		
		public int getValue() {
			return this.value;
		}
		
		public String getPlayer() {
			return this.player;
		}
	}
}

package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.stream.Stream;

public class MonopolyGame {
	private ArrayList<IField> table;
	private ArrayList<Player> players;
	private ArrayList<Card> chanceCards;
	private ArrayList<Card> chestCards;
	
	private Player currentPlayer;
	
	public void buildHouse() {
		IField field = table.get(currentPlayer.getPosition());
		if (!field.getClass().equals(LandField.class)) {
			// exception
			return;
		}
		
		LandField landField = (LandField) field;
		if (!ownsAll(landField) || !(landField.getHousePrice() <= currentPlayer.getMoney())) {
			// exception
			return;
		}
		
		if (!landField.buildHouse()) {
			// exception
			return;
		}
		
		// Build successful
		
		if (!currentPlayer.decreaseWithHouse(landField)) {
			// internal error
			landField.sellHouse();
			return;
		}
	}
	
	public void sellHouse() {
		IField field = table.get(currentPlayer.getPosition());
		if (!field.getClass().equals(LandField.class)) {
			// exception
			return;
		}
		
		LandField landField = (LandField) field;
		if (!landField.getOwner().equals(currentPlayer)) {
			// exception
			return;
		}
		
		if (!landField.sellHouse()) {
			// exception
			return;
		}
		
		currentPlayer.increaseWithHouse(landField);
	}
	
	/**
	 * Helper method, checks that the current Player owns every LandField
	 * in the argument's city.
	 * @param landField
	 * @return boolean whether or not the current Player owns all the LandFields
	 * in the given city.
	 */
	private boolean ownsAll(LandField landField) {
		
		// Create IField stream
		Stream<IField> fieldStream = table.stream();
		
		// Filter by the same city as the argument's city
		// Aftermath: after the filtration, the elements of the stream are all LandFields 
		Stream<IField> filteredStream = fieldStream.filter(field -> {
			if (!field.getClass().equals(LandField.class)) {
				return false;
			}
			
			LandField currentField = (LandField) field;
			return currentField.getCity().equals(landField.getCity());
		});
		
		// Check if all of the LandFields are owned by the current Player
		// Unchecked type cast is now allowed, because of the previous filter's aftermath 
		return filteredStream.allMatch(field -> {
			return ((LandField) field).getOwner().equals(currentPlayer);
		});
	}
}

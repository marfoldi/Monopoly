package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.stream.Stream;

import hu.elte.game.logic.GameRuleException.CODE;

public class MonopolyGame {
	private ArrayList<IField> table;
	private ArrayList<Player> players;
	private ArrayList<Card> chanceCards;
	private ArrayList<Card> chestCards;
	
	private Player currentPlayer;
	
	public void buildHouse() throws InvalidFieldException, GameRuleException {
		
		IField field = table.get(currentPlayer.getPosition());
		
		if (!field.getClass().equals(LandField.class)) {
			throw new InvalidFieldException(LandField.class, field.getClass());
		}
		
		LandField landField = (LandField) field;
		
		if (!ownsAll(landField)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns every LandField in the city.");
		}
		
		if (!(landField.getHousePrice() <= currentPlayer.getMoney())) {
			throw new GameRuleException(CODE.INSUFFICIENT_FUNDS, "The Player does not have enough money to complete this action.");
		}
		
		if (!landField.buildHouse()) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player can not build house on this field");
		}
		
		// Build successful
		
		if (!currentPlayer.decreaseWithHouse(landField)) {
			throw new RuntimeException("decreaseWithHouse failed");
		}
	}
	
	public void sellHouse() throws InvalidFieldException, GameRuleException {
		IField field = table.get(currentPlayer.getPosition());
		if (!field.getClass().equals(LandField.class)) {
			throw new InvalidFieldException(LandField.class, field.getClass());
		}
		
		LandField landField = (LandField) field;
		if (!landField.getOwner().equals(currentPlayer)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns this field.");
		}
		
		if (!landField.sellHouse()) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player can not sell house from this field");
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

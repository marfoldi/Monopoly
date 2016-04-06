package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import java.util.stream.Stream;

import hu.elte.game.logic.GameRuleException.CODE;

public class MonopolyGame {
	private ArrayList<IField> table;
	private ArrayList<Player> players;
	
	private ArrayList<Card> chanceCards;
	private Stack<Card> chanceCardsStack;
	
	private ArrayList<Card> chestCards;
	private Stack<Card> chestCardsStack;
	
	private Player currentPlayer;
	
	public MonopolyGame(ArrayList<IField> table, ArrayList<Player> players, ArrayList<Card> chanceCards, ArrayList<Card> chestCards) {
		this.table = table;
		this.players = players;
		this.chanceCards = chanceCards;
		this.chestCards = chestCards;
		this.currentPlayer = this.players.get(0);
		this.chanceCardsStack = new Stack<Card>();
		this.chestCardsStack = new Stack<Card>();
		
		// Shuffle the cards
		Collections.shuffle(this.chanceCards);
		Collections.shuffle(this.chestCards);
		
		// Fill up the card stacks with the shuffled decks
		this.chanceCardsStack.addAll(this.chanceCards);
		this.chestCardsStack.addAll(this.chestCards);
		
	}
	
	/**
	 * Gets a random chance Card from the deck.
	 * Also performs the modifications on the Player object
	 * Note: After this method there is a chance that the Player's money
	 * is negative amount. The model can not continue until this is fixed
	 * (by selling or taking up mortgages)
	 * @return the random Card from the deck
	 */
	public Card getChanceCard() {
		
		// If the deck is empty, we shuffle and restock it 
		if (this.chanceCardsStack.isEmpty()) {
			Collections.shuffle(this.chanceCards);
			this.chanceCardsStack.addAll(this.chanceCards);
		}
		
		Card card = this.chanceCardsStack.pop();
		currentPlayer.modifyWithCard(card);
		
		return card;
	}
	
	/**
	 * Gets a random chest Card from the deck.
	 * Also performs the modifications on the Player object
	 * Note: After this method there is a chance that the Player's money
	 * is negative amount. The model can not continue until this is fixed
	 * (by selling or taking up mortgages)
	 * @return the random Card from the deck
	 */
	public Card getChestCard() {
		
		// If the deck is empty, we shuffle and restock it 
		if (this.chestCardsStack.isEmpty()) {
			Collections.shuffle(this.chestCards);
			this.chestCardsStack.addAll(this.chestCards);
		}
		
		Card card = this.chestCardsStack.pop();
		currentPlayer.modifyWithCard(card);
		
		return card;
	}
	
	/**
	 * Builds a house on the field where the current Player is standing.
	 * @throws InvalidFieldException if the Player is not standing on a LandField
	 * @throws GameRuleException
	 *  - if the Player does not own every LandField in the city
	 *  - if the Player does not have enough money
	 *  - if the number of houses on the LandField is reached the maximum amount
	 */
	public void buildHouse() throws InvalidFieldException, GameRuleException {
		
		// Get the IField where the current Player is standing.
		IField field = table.get(currentPlayer.getPosition());
		
		// Check if the field is LandField
		if (!field.getClass().equals(LandField.class)) {
			throw new InvalidFieldException(LandField.class, field.getClass());
		}
		
		// After the type check we can cast it
		LandField landField = (LandField) field;
		
		// Check if the Player owns every LandField in the city
		if (!ownsAll(landField)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns every LandField in the city.");
		}
		
		// Check if the Player has enough money
		// Aftermath: After this point the Player object's 'decreaseWithHouse' method has to return TRUE
		if (!(landField.getHousePrice() <= currentPlayer.getMoney())) {
			throw new GameRuleException(CODE.INSUFFICIENT_FUNDS, "The Player does not have enough money to complete this action.");
		}
		
		// Check if we can build a house on this field.
		// Side effect: method also increases the 'houseCount' on success
		if (!landField.buildHouse()) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player can not build house on this field");
		}
		
		// After this point, we were able to build the house
		
		// Check if we can decrease the Player's money
		// Side effect: method also decreases the Player's money on success
		if (!currentPlayer.decreaseWithHouse(landField)) {
			// This method could not fail, because of the 'money check's aftermath.
			throw new RuntimeException("decreaseWithHouse failed");
		}
	}
	
	/**
	 * Sells a house from the field where the current Player is standing. 
	 * @throws InvalidFieldException if the Player is not standing on a LandField
	 * @throws GameRuleException
	 *  - if the Player does not own the field
	 *  - if the Player does not has houses on the field
	 */
	public void sellHouse() throws InvalidFieldException, GameRuleException {
		
		// Get the IField where the current Player is standing.
		IField field = table.get(currentPlayer.getPosition());
		
		// Check if the field is LandField
		if (!field.getClass().equals(LandField.class)) {
			throw new InvalidFieldException(LandField.class, field.getClass());
		}
		
		// After the type check we can cast it
		LandField landField = (LandField) field;
		
		// Check if the Player owns this field
		if (!landField.getOwner().equals(currentPlayer)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns this field.");
		}
		
		// Check if we can sell a house from this field.
		// Side effect: method also decreases the 'houseCount' on success
		if (!landField.sellHouse()) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player can not sell house from this field");
		}
		
		// After this point, we were able to sell the house
		
		// Increase the Player's money
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

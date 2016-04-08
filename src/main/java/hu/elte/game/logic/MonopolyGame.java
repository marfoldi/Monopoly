package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
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
	 * @param fieldName the name of the Field which we want to build
	 * @throws InvalidFieldException
	 *  - if there is no IField associated with the 'fieldName' parameter
	 *  - if the associated IField is not an instance of LandField
	 * @throws GameRuleException
	 *  - if the Player does not own every LandField in the city
	 *  - if the Player does not have enough money
	 *  - if the number of houses on the LandField is reached the maximum amount
	 */
	public void buildHouse(String fieldName) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		
		// Check if the field is exists
		if (field == null) {
			throw new InvalidFieldException("Field not found: " + fieldName);
		}
		
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
	 * @param fieldName the name of the Field which we want to sell from
	 * @throws InvalidFieldException
	 *  - if there is no IField associated with the 'fieldName' parameter
	 *  - if the associated IField is not an instance of LandField
	 * @throws GameRuleException
	 *  - if the Player does not own the field
	 *  - if the Player does not has houses on the field
	 */
	public void sellHouse(String fieldName) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		
		// Check if the field is exists
		if (field == null) {
			throw new InvalidFieldException("Field not found: " + fieldName);
		}
		
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
	 * Changes a PurchasableField's (can also be LandField) mortgage property.
	 * If the mortgage's new value is true, the Player's money is increased by the PurchasableField's mortgage value
	 * If the mortgage's new value is false, the Player's money is decreased by the PurchasableField's mortgage value 
	 * Does nothing if the field's current mortgage property is equals to the new one
	 * @param fieldName the field which we want to apply the new mortgage property
	 * @param isUnderMortgage whether or not this field is getting under mortgage
	 * @throws InvalidFieldException
	 *  - if there is no IField associated with the 'fieldName' parameter
	 *  - if the associated IField is not an instance of PurchasableField
	 * @throws GameRuleException
	 *  - if the Player does not owns the field
	 *  - if the Player does not have enough money to set the mortgage to false
	 */
	public void setMortgage(String fieldName, boolean isUnderMortgage) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		
		// Check if the field is exists
		if (field == null) {
			throw new InvalidFieldException("Field not found: " + fieldName);
		}
		
		// Check if the field is PurchasableField
		if (!field.getClass().equals(PurchasableField.class)) {
			throw new InvalidFieldException(PurchasableField.class, field.getClass());
		}
		
		// After the type check we can cast it
		PurchasableField purchasableField = (PurchasableField) field;
		
		// Check if the Player owns this field
		if (!purchasableField.getOwner().equals(currentPlayer)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns this field.");
		}
		
		// Check the current 'mortgage' property
		// If its the same as the parameter, don't do anything
		if (purchasableField.isMortgage() == isUnderMortgage) {
			return;
		}
		
		// Toggle the 'mortgage' property
		// Also increase / decrease the Player's money
		if (isUnderMortgage) {
			this.currentPlayer.increaseWithEstate(purchasableField);
			purchasableField.setMortgage(true);
		} else {
			// decreaseWithEstate returns true if the Player had enough money
			// Note: also decreases the Player's money on success
			if (this.currentPlayer.decreaseWithEstate(purchasableField)) {
				purchasableField.setMortgage(false);
			} else {
				throw new GameRuleException(CODE.INSUFFICIENT_FUNDS, "The Player does not have enough money to complete this action.");
			}
		}
	}
	
	/**
	 * Rolls the dice (2 dices)
	 * @return
	 */
	public Dice rollDice() {
		Random rand = new Random();
		int first = rand.nextInt(6) + 1;
		int second = rand.nextInt(6) + 1;
		
		return new Dice(first, second);
	}
	
	/**
	 * Sets a Player position on the Table, with a Dice parameter
	 * If the new position is greater than the Table's size, it loops over
	 * @param player
	 * @param dice
	 */
	public void advancePlayerWithDice(Player player, IDice dice) {
		int newPosition = (player.getPosition() + dice.getSum()) % this.table.size();
		player.setPosition(newPosition);
	}
	
	/**
	 * Helper method, gets an IField object based on the 'name' property 
	 * If multiple matches found (should not happen), then the first is returned
	 * @param fieldName
	 * @return IField the field matching 'name', null if there is no field matching 'name'
	 */
	private IField getFieldForName(String fieldName) {
		Stream<IField> fieldStream = this.table.stream();
		Optional<IField> matchingField = fieldStream.filter(field -> field.getClass().equals(fieldName)).findFirst();

		return (matchingField.isPresent()) ? matchingField.get() : null;
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

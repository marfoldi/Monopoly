package hu.elte.game.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

import org.javatuples.Pair;

import hu.elte.game.logic.ChangeSet.ACTION;
import hu.elte.game.logic.ChangeSet.ACTOR;
import hu.elte.game.logic.data.Card;
import hu.elte.game.logic.data.CardField;
import hu.elte.game.logic.data.Dice;
import hu.elte.game.logic.data.Field;
import hu.elte.game.logic.data.LandField;
import hu.elte.game.logic.data.Player;
import hu.elte.game.logic.data.PurchasableField;
import hu.elte.game.logic.data.TaxField;
import hu.elte.game.logic.exceptions.GameRuleException;
import hu.elte.game.logic.exceptions.InvalidFieldException;
import hu.elte.game.logic.exceptions.GameRuleException.CODE;
import hu.elte.game.logic.interfaces.IDice;
import hu.elte.game.logic.interfaces.IField;
import hu.elte.game.logic.interfaces.IFlowController;
import hu.elte.game.logic.interfaces.IFlowControllerListener;
import hu.elte.game.logic.interfaces.IPlayer;

public class MonopolyGame {
	private final int FINISH_AWARD = 20000;
	
	private ArrayList<IField> table;
	private ArrayList<Player> players;
	
	private ArrayList<Card> chanceCards;
	private Stack<Card> chanceCardsStack;
	
	private ArrayList<Card> chestCards;
	private Stack<Card> chestCardsStack;
	
	private Statistics log;
	
	/**
	 * Set of statuses for the fields
	 * The model can choose an action for players based on this
	 *  - SELF_OWNED   : the field belongs to the Player, now the Player can make transactions
	 *  - PLAYER_OWNED : the field belong to another Player, the current Player has to pay rent
	 *  - BANK_OWNED   : the field does not belong to anybody, the current Player can buy it
	 *  - NEUTRAL      : this is a neutral field, no action takes place
	 *  - TAX_FIELD    : this is a field where the player must pay tax
	 *  - CHANCE_CARD  : the Player gets a chance card, and the properties on it
	 *  - CHEST_CARD   : the Player gets a chest card, and the properties on it
	 *  - JAIL         : this field is not going to be in the game...
	 * @author I321357
	 *
	 */
	public enum FIELD_STATUS {
		SELF_OWNED, PLAYER_OWNED, BANK_OWNED, NEUTRAL, CHANCE_CARD, CHEST_CARD, JAIL, TAX_FIELD
	}
	
	public MonopolyGame(ArrayList<IField> table, ArrayList<Player> players, ArrayList<Card> chanceCards, ArrayList<Card> chestCards, IFlowController flowController) {
		this.table = table;
		this.players = players;
		this.chanceCards = chanceCards;
		this.chestCards = chestCards;
		this.chanceCardsStack = new Stack<Card>();
		this.chestCardsStack = new Stack<Card>();
		this.log = new Statistics();
		
		// Shuffle the cards
		Collections.shuffle(this.chanceCards);
		Collections.shuffle(this.chestCards);
		
		// Fill up the card stacks with the shuffled decks
		this.chanceCardsStack.addAll(this.chanceCards);
		this.chestCardsStack.addAll(this.chestCards);
		
		flowController.setModelListener(new IFlowControllerListener() {

			@Override
			public void onCurrentPlayerChange(String playerName) {
				// The model does not care about the current player,
				// it is the FlowController's and Controller's business
				return;
				
			}

			@Override
			public void onGameStateChange(ChangeSet changeSet) {
				updateWithChangeSet(changeSet);
				return;
				
			}
			
		});
		
	}
	
	/**
	 * Gets the status for a Field by it's name
	 * The status is relative to a Player
	 * @param playerName
	 * @param fieldName
	 * @return
	 */
	public FIELD_STATUS getStatusForField(String playerName, String fieldName) {
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		// Get the Field with the name fieldName
		IField field = getFieldForName(fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Field not found: " + fieldName);
		}
		Class<?> clazz = field.getClass();
		
		// Check the type of the IField
		if (clazz.equals(Field.class)) {
			return FIELD_STATUS.NEUTRAL;
		} else if (clazz.equals(CardField.class)) {
			CardField cardField = (CardField) field;
			return ("chance".equals(cardField.getSubType())) ? FIELD_STATUS.CHANCE_CARD : FIELD_STATUS.CHEST_CARD;
		} else if (clazz.equals(TaxField.class)) {
			return FIELD_STATUS.TAX_FIELD;
		}
		// If it's not a plain Field, it must be an instance of PurchasableField
		else if (field instanceof PurchasableField) {
			IPlayer owner = ((PurchasableField) field).getOwner();
			if (owner == null) {
				return FIELD_STATUS.BANK_OWNED;
			} else {
				// Relative status to a Player
				return owner.getName().equals(playerName) ? FIELD_STATUS.SELF_OWNED : FIELD_STATUS.PLAYER_OWNED;
			}
		} else {
			throw new RuntimeException("Undhandled field type in getStatus: " + clazz);
		}
	}
	
	/**
	 * Gets the status for a Field by it's index
	 * The status is relative to a Player
	 * @param playerName
	 * @param index
	 * @return
	 */
	public FIELD_STATUS getStatusForField(String playerName, int index) {
		return getStatusForField(playerName, getFieldNameForIndex(index));
	};
	
	/**
	 * Gets a random chance Card from the deck.
	 * Also performs the modifications on the Player object
	 * Note: After this method there is a chance that the Player's money
	 * is negative amount. The model can not continue until this is fixed
	 * (by selling or taking up mortgages)
	 * @param playerName
	 * @return the random Card from the deck
	 */
	public Card getChanceCardForPlayer(String playerName) {
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		// If the deck is empty, we shuffle and restock it 
		if (this.chanceCardsStack.isEmpty()) {
			Collections.shuffle(this.chanceCards);
			this.chanceCardsStack.addAll(this.chanceCards);
		}
		
		Card card = this.chanceCardsStack.pop();
		// Log the card action
		// Note: this also adds this to the changeSet
		// TODO: implement card.getId()
		log.card(playerName, "card-id");
				
		int money = card.getMoney();
		if (money != 0) {
			// The Player's money can be negative after this action
			// so we use the manual setMoney method that does not throw error
			player.setMoney(player.getMoney() + money);
		}
		
		return card;
	}
	
	/**
	 * Gets a random chest Card from the deck.
	 * Also performs the modifications on the Player object
	 * Note: After this method there is a chance that the Player's money
	 * is negative amount. The model can not continue until this is fixed
	 * (by selling or taking up mortgages)
	 * @param playerName
	 * @return the random Card from the deck
	 */
	public Card getChestCardForPlayer(String playerName) {
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		// If the deck is empty, we shuffle and restock it 
		if (this.chestCardsStack.isEmpty()) {
			Collections.shuffle(this.chestCards);
			this.chestCardsStack.addAll(this.chestCards);
		}
		
		Card card = this.chestCardsStack.pop();
		// Log the card action
		// Note: this also adds this to the changeSet
		// TODO: implement card.getId()
		log.card(playerName, "card-id");
		
		int money = card.getMoney();
		if (money != 0) {
			// The Player's money can be negative after this action
			// so we use the manual setMoney method that does not throw error
			player.setMoney(player.getMoney() + money);
			
			// Log the transaction
			// Note: this also adds this to the changeSet
			String from = money < 0 ? playerName : null;
			String to = money < 0 ? null : playerName; 
			log.transaction(from, to, card.getText(), money);
		}
		
		return card;
	}
	
	/**
	 * Builds a house on the field where the current Player is standing.
	 * @param playerName the name of the Player who wants to build
	 * @param fieldName the name of the Field which we want to build
	 * @throws InvalidFieldException
	 *  - if there is no IField associated with the 'fieldName' parameter
	 *  - if the associated IField is not an instance of LandField
	 * @throws GameRuleException
	 *  - if the Player does not own every LandField in the city
	 *  - if the Player does not have enough money
	 *  - if the number of houses on the LandField is reached the maximum amount
	 */
	public void buildHouse(String playerName, String fieldName) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Field not found: " + fieldName);
		}
		
		// Check if the field is LandField
		if (!field.getClass().equals(LandField.class)) {
			throw new InvalidFieldException(LandField.class, field.getClass());
		}
		LandField landField = (LandField) field;
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		// Check if the Player owns every LandField in the city
		if (!ownsAll(playerName, landField)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns every LandField in the city.");
		}
				
		// Check if we can build a house on this field.
		if (landField.getHouseCount() >= 5) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player can not build house on this field");
		}
		
		// Decrease the Player's money and build the house
		// Note: doTransaction throws error if the Player does not have enough money
		player.doTransaction(landField.getHousePrice() * -1);
		landField.buildHouse();
		
		// Log the +1 house build action
		// Note: this also adds this to the change set
		log.houseChange(fieldName, 1);
	}

	/**
	 * Sells a house from the field where the current Player is standing.
	 * @param playerName the name of the Player who wants to sell 
	 * @param fieldName the name of the Field which we want to sell from
	 * @throws InvalidFieldException
	 *  - if there is no IField associated with the 'fieldName' parameter
	 *  - if the associated IField is not an instance of LandField
	 * @throws GameRuleException
	 *  - if the Player does not own the field
	 *  - if the Player does not has houses on the field
	 */
	public void sellHouse(String playerName, String fieldName) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		if (field == null) {
			throw new InvalidFieldException("Field not found: " + fieldName);
		}
		
		// Check if the field is LandField
		if (!field.getClass().equals(LandField.class)) {
			throw new InvalidFieldException(LandField.class, field.getClass());
		}
		LandField landField = (LandField) field;
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		// Check if the Player owns this field
		if (landField.getOwner() == null || !landField.getOwner().getName().equals(playerName)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns this field.");
		}
		
		// Check if we can sell a house from this field.
		// Side effect: method also decreases the 'houseCount' on success
		if (!landField.sellHouse()) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player can not sell house from this field");
		}
		
		// After this point, we were able to sell the house
		
		// Increase the Player's money
		// houseValue is always positive, so the doTransaction won't fail
		int houseValue = landField.getHousePrice() / 2;
		player.doTransaction(houseValue);
		
		// Log the -1 house sell action
		// Note: this also adds this to the change set
		log.houseChange(fieldName, -1);
	}
	
	/**
	 * Sets the owner of a PurchasableField (can also be LandField) to the Player
	 * Also decreases the Player's money with the estate's price
	 * @param playerName
	 * @param fieldName
	 * @throws InvalidFieldException
	 *  - If the IField for the String 'fieldName' is not an instance of PurchasableField
	 * @throws GameRuleException 
	 *  - If someone already owns the estate
	 *  - If the Player does not have enough money to buy the estate
	 */
	public void buyEstate(String playerName, String fieldName) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Field not found: " + fieldName);
		}
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
				
		// Check if the field is LandField
		if (!(field instanceof PurchasableField)) {
			throw new InvalidFieldException(PurchasableField.class, field.getClass());
		}
		PurchasableField purchasableField = (PurchasableField) field;
		
		// Check if no one owns the field
		if (purchasableField.getOwner() != null) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The estate is not bank owned");
		}
		
		// Decrease the Player's money and set the new owner
		// Note: doTransaction throws error if the Player does not have enough money
		player.doTransaction(purchasableField.getPrice() * -1);
		purchasableField.setOwner(player);
		
		// Log the estate buy action
		// Note: this also adds this to the change set
		log.estateOwnerChange(null, playerName, fieldName, purchasableField.getPrice());
	}
	
	/**
	 * Sets the owner of a PurchasableField (can also be LandField) to the bank (null)
	 * Also increases the Player's money with the half of the estate's price
	 * @param playerName
	 * @param fieldName
	 * @throws InvalidFieldException
	 *  - If the IField for the String 'fieldName' is not an instance of PurchasableField
	 * @throws GameRuleException 
	 *  - If the Player does not owns the estate
	 */
	public void sellEstate(String playerName, String fieldName) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		if (field == null) {
			throw new IllegalArgumentException("Field not found: " + fieldName);
		}
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
			
		// Check if the field is LandField
		if (!(field instanceof PurchasableField)) {
			throw new InvalidFieldException(PurchasableField.class, field.getClass());
		}
		PurchasableField purchasableField = (PurchasableField) field;
				
		// Check if no one owns the field
		if (purchasableField.getOwner() == null || !purchasableField.getOwner().getName().equals(playerName)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns the field");
		}
		
		// Increase the Player's money and set the new owner to bank (null)
		// estateValue is always positive, so the doTransaction won't fail
		int estateValue = purchasableField.getPrice() / 2;
		player.doTransaction(estateValue);
		purchasableField.setOwner(null);
		
		// Log the estate sell action
		// Note: this also adds this to the change set
		log.estateOwnerChange(playerName, null, fieldName, estateValue);
	}

	/**
	 * Changes a PurchasableField's (can also be LandField) mortgage property.
	 * If the mortgage's new value is true, the Player's money is increased by the PurchasableField's mortgage value
	 * If the mortgage's new value is false, the Player's money is decreased by the PurchasableField's mortgage value 
	 * Does nothing if the field's current mortgage property is equals to the new one
	 * @param playerName
	 * @param fieldName the field which we want to apply the new mortgage property
	 * @param isUnderMortgage whether or not this field is getting under mortgage
	 * @throws InvalidFieldException
	 *  - if there is no IField associated with the 'fieldName' parameter
	 *  - if the associated IField is not an instance of PurchasableField
	 * @throws GameRuleException
	 *  - if the Player does not owns the field
	 *  - if the Player does not have enough money to set the mortgage to false
	 */
	public void setMortgage(String playerName, String fieldName, boolean isUnderMortgage) throws InvalidFieldException, GameRuleException {
		
		// Get the IField with the name 'fieldName'
		IField field = getFieldForName(fieldName);
		if (field == null) {
			throw new InvalidFieldException("Field not found: " + fieldName);
		}
		
		// Check if the field is PurchasableField
		if (!field.getClass().equals(PurchasableField.class)) {
			throw new InvalidFieldException(PurchasableField.class, field.getClass());
		}
		PurchasableField purchasableField = (PurchasableField) field;
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		// Check if the Player owns this field
		if (purchasableField.getOwner() == null || !purchasableField.getOwner().getName().equals(playerName)) {
			throw new GameRuleException(CODE.CONDITION_FAILURE, "The Player does not owns this field.");
		}
		
		// Check the current 'mortgage' property
		// If its the same as the parameter, don't do anything
		if (purchasableField.isMortgage() == isUnderMortgage) {
			return;
		}
		
		// Toggle the 'mortgage' property
		// Also increase / decrease the Player's money
		int mortgageValue = purchasableField.getPrice() / 2;
		if (isUnderMortgage) {
			
			player.doTransaction(mortgageValue);
			purchasableField.setMortgage(true);
			
		} else {
			
			// Note: doTransaction throws error if the Player does not have enough money
			player.doTransaction(mortgageValue * -1);
			purchasableField.setMortgage(false);
		}
		
		// Log the estate mortgage action
		// Note: this also adds this to the change set
		log.mortgageSet(playerName, fieldName, isUnderMortgage, mortgageValue);
	}
	
	/**
	 * Rolls the dice (2 dices)
	 * @return
	 */
	public Dice rollDice(String playerName) {
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		
		Random rand = new Random();
		int first = rand.nextInt(6) + 1;
		int second = rand.nextInt(6) + 1;
		Dice dice = new Dice(first, second);
		
		// Log the roll action
		// Note: this also adds this to the change set
		log.rollDice(playerName, dice);
		
		return dice;
	}
	
	/**
	 * Sets a Player position on the Table, with a Dice parameter
	 * If the new position is greater than the Table's size, it loops over
	 * @param player
	 * @param dice
	 * @return Pair
	 */
	public Pair<FIELD_STATUS, Object> advancePlayerWithDice(String playerName, IDice dice) {
		
		// Get the Player with the name 'playerName'
		Player player = getPlayerForName(playerName);
		if (player == null) {
			throw new IllegalArgumentException("Player not found: " + playerName);
		}
		 
		int newPosition = (player.getPosition() + dice.getSum()) % this.table.size();
		player.setPosition(newPosition);
		
		// Check if the Player has crossed the finish line
		if (newPosition < dice.getSum()) {
			try {
				player.doTransaction(FINISH_AWARD);
			}
			catch (GameRuleException e) {
				// Should never fail
				throw new RuntimeException("Internal error");
			}
		}
		
		// Log the position change action
		// Note: this also adds this to the change set
		log.positionChange(playerName, newPosition);
		
		// Obtain the new field by the new position
		String newFieldName = getFieldNameForIndex(newPosition);
		IField newField = getFieldForName(newFieldName);
		
		// Get the status for the new field the player is standing
		FIELD_STATUS status = getStatusForField(playerName, newFieldName);
		
		switch (status) {
			case BANK_OWNED:
			case SELF_OWNED:
			case NEUTRAL: {
				break;
			}
			case PLAYER_OWNED: {
				PurchasableField field = (PurchasableField) newField;
				String ownerName = field.getOwner().getName();
				
				// Check if it is under mortgage
				// If so, we do not have to pay a rent fee
				if (field.isMortgage()) {
				
				}
			
				int price;
				// If it is LandField, get the price according the field's house count
				if (newField.getClass().equals(LandField.class)) {
					LandField landField = (LandField) newField;
					price = landField.getIncomings().get(landField.getHouseCount() - 1);
				}
				// If it is PurchasableField, get the price according how many same fields the player has
				else {
					PurchasableField purchasableField = (PurchasableField) newField;
					int numberOfEstates = (int) this.table.stream().filter(item -> {
						if (!item.getClass().equals(PurchasableField.class)) {
							return false;
						}
						PurchasableField castedItem = (PurchasableField) item;
						// FIX ME: missing getter: getSubType()
						// return castedItem.getOwner().getName().equals(ownerName) && castedItem.getSubType().equals(purchasableField.getSubType());
						return true;
					}).count();
					price = purchasableField.getIncomings().get(numberOfEstates - 1);
				}
				
				// Decrease the player's money with the rental fee
				player.setMoney(player.getMoney() - price);
				
				// Log the rental action
				log.rentPayment(playerName, ownerName, field.getName(), price);
				
				return new Pair<FIELD_STATUS, Object>(status, player.getMoney());
			}
			case CHEST_CARD:
			case CHANCE_CARD: {
				break;
			}
			default: throw new RuntimeException("Unhandled status case: " + status);
		}
		// TODO: remove this after switch is done
		return null;
	}
	
	/**
	 * Gets an IField's name by the given index
	 * @param index
	 * @throws IllegalArgumentException
	 *  - If the table is null
	 *  - If the given index is out of range
	 * @return
	 */
	public String getFieldNameForIndex(int index) {
		if (this.table == null || index < 0 || index >= this.table.size()) {
			throw new IllegalArgumentException("Invalid index or the table is null");
		}
		
		return this.table.get(index).getName();
	}
	
	/**
	 * Gets a Player object for a String name
	 * @param playerName
	 * @return
	 */
	private Player getPlayerForName(String playerName) {
		Stream<Player> playerStream = this.players.stream();
		Stream<Player> filteredStream = playerStream.filter(player -> player.getName().equals(playerName));
		
		Optional<Player> player = filteredStream.findFirst();
		return (player.isPresent()) ? player.get() : null;
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
	 * Helper method, checks that the Player owns every LandField
	 * in the argument's city.
	 * @param playerName
	 * @param landField
	 * @return boolean whether or not the current Player owns all the LandFields
	 * in the given city.
	 */
	private boolean ownsAll(String playerName, LandField field) {
		
		// Create IField stream
		Stream<IField> fieldStream = table.stream();
		
		// Filter by the same city as the argument's city
		// Aftermath: after the filtration, the elements of the stream are all LandFields 
		Stream<IField> filteredStream = fieldStream.filter(iField -> {
			if (!iField.getClass().equals(LandField.class)) {
				return false;
			}
			
			LandField currentField = (LandField) iField;
			return currentField.getCity().equals(field.getCity());
		});
		
		// Check if all of the LandFields are owned by the current Player
		// Unchecked type cast is now allowed, because of the previous filter's aftermath 
		return filteredStream.allMatch(iField -> {
			LandField landField = (LandField) iField;
			return (landField.getOwner() != null) && (landField.getOwner().getName().equals(playerName));
		});
	}
	
	/**
	 * Updates the model properties with the changes in the parameter
	 * @param changeSet
	 */
	private void updateWithChangeSet(ChangeSet changeSet) {
		// we are only interested in the changes with ACTOR = FIELD now
		// *  FIELD:
		// *  - action can be: OWNER, HOUSE, MORTGAGE
		
		// # OWNER
		ArrayList<Pair<String, String>> ownerChanges = changeSet.getChangesFor(ACTOR.FIELD, ACTION.OWNER);
		for (Pair<String, String> change : ownerChanges) {
			String owner = change.getValue0();
			String estate = change.getValue1();
			
			// Check if the new owner is the bank (null), or it is a valid player
			Player player = owner == null ? null : getPlayerForName(owner);
			if (owner != null && player == null) {
				// TODO: error handling
				return;
			}
			
			// Check if the field exists
			IField field = getFieldForName(estate);
			if (field == null) {
				// TODO: error handling
				return;
			}
			
			// Check if it is a PurchasableField (van also be LandField)
			if (!(field instanceof PurchasableField)) {
				// TODO: error handling
				return;
			}
			PurchasableField purchasableField = (PurchasableField) field;
			
			// Set the new owner
			purchasableField.setOwner(player);
		}
		
		// # HOUSE
		ArrayList<Pair<String, String>> houseChanges = changeSet.getChangesFor(ACTOR.FIELD, ACTION.HOUSE);
		for (Pair<String, String> change : houseChanges) {
			String estate = change.getValue0();
			int diff = Integer.parseInt(change.getValue1());
			
			// Check if the field exists
			IField field = getFieldForName(estate);
			if (field == null) {
				// TODO: error handling
				return;
			}
						
			// Check if it is a LandField
			if (!(field.getClass().equals(LandField.class))) {
				// TODO: error handling
				return;
			}
			LandField landField = (LandField) field;
			
			// Build / Sell houses
			// If the diff is < 0, we need to sell 'diff' amount of houses
			if (diff < 0) {
				for (int i = diff; i != 0; i++) {
					landField.sellHouse();
				}
			}
			// If the diff is > 0, we need to build 'diff' amount of houses
			else if (diff > 0) {
				for (int i = diff; i != 0; i--) {
					landField.buildHouse();
				}
			} else {
				// This should never happen
				return;
			}
		}
		
		// # MORTGAGE
		ArrayList<Pair<String, String>> mortgageChanges = changeSet.getChangesFor(ACTOR.FIELD, ACTION.MORTGAGE);
		for (Pair<String, String> change : mortgageChanges) {
			String estate = change.getValue0();
			boolean isUnderMortgage = Boolean.parseBoolean(change.getValue1());
			
			// Check if the field exists
			IField field = getFieldForName(estate);
			if (field == null) {
				// TODO: error handling
				return;
			}
			
			// Check if it is a PurchasableField (van also be LandField)
			if (!(field instanceof PurchasableField)) {
				// TODO: error handling
				return;
			}
			PurchasableField purchasableField = (PurchasableField) field;
			
			// Set the new mortgage value
			purchasableField.setMortgage(isUnderMortgage);
		}
		
	}

	/**
	 * @return the table
	 */
	public ArrayList<IField> getTable() {
		return table;
	}
}

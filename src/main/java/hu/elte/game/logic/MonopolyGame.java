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
	
	private boolean ownsAll(LandField landField) {
		Stream<IField> fieldStream = table.stream();
		Stream<IField> filteredStream = fieldStream.filter(field -> {
			if (!field.getClass().equals(LandField.class)) {
				return false;
			}
			
			LandField currentField = (LandField) field;
			return currentField.getCity().equals(landField.getCity());
		});
		
		
		return filteredStream.allMatch(field -> {
			return ((LandField) field).getOwner().equals(currentPlayer);
		});
	}
}

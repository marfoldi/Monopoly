package hu.elte.game;

import java.util.ArrayList;
import java.util.List;

import hu.elte.game.logic.Card;
import hu.elte.game.logic.CardParser;
import hu.elte.game.logic.ChangeSet;
import hu.elte.game.logic.FieldParser;
import hu.elte.game.logic.IDice;
import hu.elte.game.logic.IField;
import hu.elte.game.logic.IFlowController;
import hu.elte.game.logic.IFlowControllerListener;
import hu.elte.game.logic.IPlayer;
import hu.elte.game.logic.JSONParser;
import hu.elte.game.logic.MonopolyGame;
import hu.elte.game.logic.MonopolyGame.FIELD_STATUS;
import hu.elte.game.logic.Player;
import hu.elte.game.logic.LocalFlowController;
import hu.elte.game.logic.Statistics;
import net.sf.json.JSONArray;

public class Controller {

	private final int START_MONEY = 70000;
	
	private MonopolyGame game;
	private IFlowController flowController;
	private String currentPlayer;
	
	public void createGame(List<String> playerNames) {
		
		// Parse the fields from JSON
		JSONArray jsonFields = JSONParser.parse("fields.json");
		ArrayList<IField> fields = new ArrayList<IField>(FieldParser.parse(jsonFields));
				
		// Parse the chance-cards from JSON
		JSONArray jsonChanceCards = JSONParser.parse("chanceCards.json");
		ArrayList<Card> chanceCards = new ArrayList<Card>(CardParser.parseCards(jsonChanceCards));
				
		// Parser the chest-cards from JSON
		JSONArray jsonChestCards = JSONParser.parse("commCards.json");
		ArrayList<Card> chestCards = new ArrayList<Card>(CardParser.parseCards(jsonChestCards));
				
		// Create the Players from their names
		ArrayList<Player> players = new ArrayList<Player>();
		for (String playerName : playerNames) {
			players.add(new Player(playerName, START_MONEY));
		}
				
		// Set the flow controller
		this.flowController = new LocalFlowController();
		this.flowController.setPlayers(playerNames);
		this.flowController.setControllerListener(new IFlowControllerListener() {

			@Override
			public void onCurrentPlayerChange(String playerName) {
				currentPlayer = playerName;
				startPlayerFlow();
				
			}

			@Override
			public void onGameStateChange(ChangeSet changeSet) {
				// TODO: Make the necessary UI changes according to the received Statistics
				
			}
			
		});

		
		// Create the game
		this.game = new MonopolyGame(fields, players, chanceCards, chestCards, this.flowController);
		
	}

	private void startPlayerFlow() {
		// TODO: Enable UI elements 
	}
}

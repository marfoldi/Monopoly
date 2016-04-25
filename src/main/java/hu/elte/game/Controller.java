package hu.elte.game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import hu.elte.game.logic.ChangeSet;
import hu.elte.game.logic.MonopolyGame;
import hu.elte.game.logic.data.Card;
import hu.elte.game.logic.data.Player;
import hu.elte.game.logic.interfaces.IField;
import hu.elte.game.logic.interfaces.IFlowController;
import hu.elte.game.logic.interfaces.IFlowControllerListener;
import hu.elte.game.logic.parsers.CardParser;
import hu.elte.game.logic.parsers.FieldParser;
import hu.elte.game.logic.parsers.JSONParser;
import hu.elte.game.view.Field;
import hu.elte.game.logic.LocalFlowController;
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
		JSONArray jsonChestCards = JSONParser.parse("commChestCards.json");
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
	
	/**
	 * @return the table
	 */
	public List<hu.elte.game.view.Field> getTableAsUITable() {
		List<hu.elte.game.view.Field> uiFields = new ArrayList<>();
		Stream<IField> fieldStream = game.getTable().stream();
		fieldStream.forEach(field -> uiFields.add(new Field(field.getName(), null, null)));
		return uiFields;
	}

	private void startPlayerFlow() {
		// TODO: Enable UI elements 
	}
	
	public List<IField> getFields(){
		return game.getTable();
	}
}

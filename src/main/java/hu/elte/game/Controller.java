package hu.elte.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import hu.elte.game.logic.ChangeSet;
import hu.elte.game.logic.MonopolyGame;
import hu.elte.game.logic.data.Card;
import hu.elte.game.logic.data.CardField;
import hu.elte.game.logic.data.LandField;
import hu.elte.game.logic.data.Player;
import hu.elte.game.logic.data.PurchasableField;
import hu.elte.game.logic.data.TaxField;
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
		ArrayList<IField> fields = new ArrayList<IField>(
				FieldParser.parse(jsonFields));

		// Parse the chance-cards from JSON
		JSONArray jsonChanceCards = JSONParser.parse("chanceCards.json");
		ArrayList<Card> chanceCards = new ArrayList<Card>(
				CardParser.parseCards(jsonChanceCards));

		// Parser the chest-cards from JSON
		JSONArray jsonChestCards = JSONParser.parse("commChestCards.json");
		ArrayList<Card> chestCards = new ArrayList<Card>(
				CardParser.parseCards(jsonChestCards));

		// Create the Players from their names
		ArrayList<Player> players = new ArrayList<Player>();
		for (String playerName : playerNames) {
			players.add(new Player(playerName, START_MONEY));
		}

		// Set the flow controller
		this.flowController = new LocalFlowController();
		this.flowController.setPlayers(playerNames);
		this.flowController
				.setControllerListener(new IFlowControllerListener() {

					@Override
					public void onCurrentPlayerChange(String playerName) {
						currentPlayer = playerName;
						startPlayerFlow();

					}

					@Override
					public void onGameStateChange(ChangeSet changeSet) {
						// TODO: Make the necessary UI changes according to the
						// received Statistics

					}

				});

		// Create the game
		this.game = new MonopolyGame(fields, players, chanceCards, chestCards,
				this.flowController);

	}

	/**
	 * @return the table
	 */
	public List<hu.elte.game.view.Field> getTableAsUITable() {
		List<hu.elte.game.view.Field> uiFields = new ArrayList<>();
		Stream<IField> fieldStream = game.getTable().stream();
		fieldStream.forEach(field -> uiFields.add(new Field(field.getName(),
				null, null)));
		return uiFields;
	}

	private void startPlayerFlow() {
		// TODO: Ez még dummy fields

	}

	public List<Field> getFields() throws IOException {
		HashMap<String, Color> cities = new HashMap<>();
		cities.put("Eger", Color.blue);
		cities.put("Székesfehérvár", Color.green);
		cities.put("Kecskemét", Color.red);
		cities.put("Pécs", Color.orange);
		cities.put("Debrecen", Color.pink);
		cities.put("Szeged", Color.yellow);
		cities.put("Sopron", Color.cyan);
		cities.put("Budapest", Color.magenta);
		
		Image train =ImageIO.read(new File("./src/main/java/resources/img/transport.png"));
		Image powerPlant = ImageIO.read(new File("./src/main/java/resources/img/buildings.png"));
		Image question = ImageIO.read(new File("./src/main/java/resources/img/signs.png"));
		Image chest = ImageIO.read(new File("./src/main/java/resources/img/tool.png"));
		// public Field(String name, Image img, Color city, String price)
		ArrayList<IField> logicFields = game.getTable();
		int FIELDSSIZE = 40;
		List<Field> fields = new ArrayList<>();
		for (int i = 0; i < FIELDSSIZE; ++i) {
			if (logicFields.get(i).getClass() == PurchasableField.class) {
				PurchasableField purchasableField = (PurchasableField) logicFields.get(i);
				if(purchasableField.getSubType().equals("Eromu")){
					fields.add(new Field(purchasableField.getName(), powerPlant, null,
							purchasableField.getPrice() + " Ft"));
				}else{
					fields.add(new Field(purchasableField.getName(), train, null,
							purchasableField.getPrice() + " Ft"));
				}
				
			}else if(logicFields.get(i).getClass() == TaxField.class){
				TaxField taxField = (TaxField) logicFields.get(i);
				fields.add(new Field(taxField.getName(), null, null,
						taxField.getPrice() + " Ft"));
			}else if(logicFields.get(i).getClass() == LandField.class){
				LandField landField = (LandField) logicFields.get(i);
				fields.add(new Field(landField.getName(),  null, cities.get(landField.getCity()), landField.getPrice() + " Ft"));
			}else if(logicFields.get(i).getClass() == CardField.class){
				CardField cardField = (CardField) logicFields.get(i);
				if(cardField.getSubType().equals("chance")) {
					fields.add(new Field(cardField.getName(), question, null, ""));
				}else{
					fields.add(new Field(cardField.getName(), chest, null, ""));
				}
			}else{
				hu.elte.game.logic.data.Field field = (hu.elte.game.logic.data.Field) logicFields.get(i);
				fields.add(new Field(field.getName(), null, null, ""));
			}
		}
		return fields;
	}
}

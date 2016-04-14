package hu.elte.game.logic.parsers;

import java.util.ArrayList;
import java.util.List;

import hu.elte.game.logic.data.Card;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Parses the Card objects from JSON
 * @author I321357
 *
 */

public class CardParser {
	
	/**
	 * Parses the Card objects from the JSONArray
	 * @param cards
	 * @return
	 * @throws JSONException
	 */
	public static List<Card> parseCards(JSONArray cards) {
		List<Card> retVal = new ArrayList<Card>();
		for (int i=0; i<cards.size(); i++) {
			Card field = parseCardFromJSONObject(cards.getJSONObject(i));
			retVal.add(field);
		}
		
		return retVal;
	}

	private static Card parseCardFromJSONObject(JSONObject card) {
		int money = card.getInt("money");
		int step = card.getInt("step");
		String text = card.getString("text");
		
		return new Card(money, step, text);
	}

}
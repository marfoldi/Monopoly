package hu.elte.game.logic.parsers;

import java.util.ArrayList;
import java.util.List;

import hu.elte.game.logic.data.Field;
import hu.elte.game.logic.data.LandField;
import hu.elte.game.logic.data.PurchasableField;
import hu.elte.game.logic.interfaces.IField;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Parses the IField objects from JSON
 * @author I321357
 *
 */

public class FieldParser {
	
	/**
	 * Parses the IField objects from the JSONArray
	 * @param fields
	 * @return
	 * @throws JSONException
	 */
	public static List<IField> parse(JSONArray fields) {
		List<IField> retVal = new ArrayList<IField>();
		for (int i=0; i<fields.size(); i++) {
			IField field = parseFieldFromJSONObject(fields.getJSONObject(i));
			retVal.add(field);
		}
		
		return retVal;
	}

	private static IField parseFieldFromJSONObject(JSONObject field) {
		String type = field.getString("type");
		String name = field.getString("name");
		
		if ("Field".equals(type)) {
			return new Field(name);
		} else if ("TaxField".equals(type)) {
			int price = field.getInt("price");
			//return new TaxField(name, price);
		} else if ("CardField".equals(type)) {
			String subType = field.getString("subType");
			// TODO: return new CardField(name, subType);
		}
		// After this point, the type must be Land or Purchasable -Field
		else {
			int price = field.getInt("price");
			
			if ("LandField".equals(type)) { 
				return parseLandFieldFromJSONObject(field, name, price);
			} else if ("PurchasableField".equals(type)) {
				return parsePurchasableFieldFromJSONObject(field, name, price);
			}
		}
		
		return null;
	}

	private static IField parsePurchasableFieldFromJSONObject(JSONObject field, String name, int price) {
		String subType = field.getString("subType");
		ArrayList<Integer> incomings = JSONParser.parseJSONArray(field.getJSONArray("incomings"));
		
		return new PurchasableField(name, subType, price, incomings);
	}

	private static LandField parseLandFieldFromJSONObject(JSONObject field, String name, int price) {
		int housePrice = field.getInt("housePrice");
		String city = field.getString("city");
		ArrayList<Integer> rentalFee = JSONParser.parseJSONArray(field.getJSONArray("rentalFee"));
		
		return new LandField(name, price, city, housePrice, rentalFee);
	}
	
}

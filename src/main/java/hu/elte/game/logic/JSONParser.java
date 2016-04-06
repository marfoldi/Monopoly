package hu.elte.game.logic;

import java.io.InputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONSerializer;

public class JSONParser {
	
	public static <T> T parse(String fileName) {
		return parse(fileName, JSONParser.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T parse(String fileName, Class<?> clazz) {
		InputStream is = clazz.getResourceAsStream("../../../../resources/" + fileName);
        try {
        	String jsonTxt = IOUtils.toString(is);
        	Object json = JSONSerializer.toJSON( jsonTxt );
        	return (T) json;
        } catch (Exception e) {
        	return null;
        }
	}

	@SuppressWarnings("unchecked")
	public static <T> ArrayList<T> parseJSONArray(JSONArray array) {
		//ArrayList<T> retVal = JSONArray.toList(array); -- deprecated
		ArrayList<T> retVal = new ArrayList<T>();
		for (int i=0; i<array.size(); i++) {
			try {
				retVal.add((T) array.get(i));
			} catch (Exception e) {
				// no-operation
			}
		}
		return retVal;
	}
}


/*public class JSONParser {
	
	

//	private void addLandField() throws IOException {
//		reader.nextName();
//		String fieldName = reader.nextString();
//		reader.nextName();
//		Integer price = reader.nextInt();
//		reader.nextName();
//		String city = reader.nextString();
//		reader.nextName();
//		Integer housePrice = reader.nextInt();
//		reader.nextName();
//		reader.beginArray();
//		List<Integer> rentalFee = new ArrayList<>();
//		while (reader.hasNext()) {
//			rentalFee.add(reader.nextInt());
//		}
//		reader.endArray();
//		fields.add(new LandField(fieldName, price, city, housePrice, rentalFee));
//		return;
//	}
//
//	private void addPurchasableField() throws IOException {
//		reader.nextName();
//		String fieldName = reader.nextString();
//		reader.nextName();
//		Integer price = reader.nextInt();
//		reader.nextName();
//		reader.beginArray();
//		List<Integer> incomings = new ArrayList<>();
//		while (reader.hasNext()) {
//			incomings.add(reader.nextInt());
//		}
//		reader.endArray();
//		fields.add(new PurchasableField(fieldName, price, incomings));
//		return;
//	}
//
//	private void addField() throws IOException {
//		reader.nextName();
//		String fieldName = reader.nextString();
//		fields.add(new Field(fieldName));
//		return;
//	}

	// private void egyMezotHozzaad() throws IOException {
	// String name = reader.nextName();
	// String fieldType = reader.nextString();
	// name = reader.nextName();
	// String fieldName = reader.nextString();
	// if (fieldType.equals("Field")) {
	// fields.add(new Field(fieldName));
	// return;
	// } else if (fieldType.equals("PurchasableField")) {
	// reader.nextName();
	// Integer price = reader.nextInt();
	//
	// reader.nextName();
	// reader.beginArray();
	// List<Integer> incomings = new ArrayList<>();
	// while (reader.hasNext()) {
	// incomings.add(reader.nextInt());
	// }
	// reader.endArray();
	// fields.add(new PurchasableField(fieldName, price, incomings));
	// return;
	// } else if (fieldType.equals("LandField")) {
	// reader.nextName();
	// Integer price = reader.nextInt();
	// reader.nextName();
	// String city = reader.nextString();
	// reader.nextName();
	// Integer housePrice = reader.nextInt();
	// reader.nextName();
	// reader.beginArray();
	// List<Integer> rentalFee = new ArrayList<>();
	// while (reader.hasNext()) {
	// rentalFee.add(reader.nextInt());
	// }
	// reader.endArray();
	// fields.add(new LandField(fieldName, price, city, housePrice, rentalFee));
	// return;
	// }
	// }

}
*/
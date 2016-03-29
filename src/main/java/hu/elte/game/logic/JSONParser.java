package hu.elte.game.logic;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;

public class JSONParser {
	private JsonReader reader;
	private String fileName;
	private List<IField> fields;

	public JSONParser(String fn) {
		this.fileName = fn;
		fields = new ArrayList<>();
	}

	public List<IField> start() throws IOException {
		fields.clear();
		reader = new JsonReader(new FileReader(fileName));
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			// egyMezotHozzaad();
			reader.nextName();
			String fieldType = reader.nextString();
			if (fieldType.equals("Field")) {
				addField();
			} else if (fieldType.equals("PurchasableField")) {
				addPurchasableField();
			} else if (fieldType.equals("LandField")) {
				addLandField();
			}
			//////////////////
			reader.endObject();
		}
		reader.endArray();
		reader.close();
		return fields;
	}

	private void addLandField() throws IOException {
		reader.nextName();
		String fieldName = reader.nextString();
		reader.nextName();
		Integer price = reader.nextInt();
		reader.nextName();
		String city = reader.nextString();
		reader.nextName();
		Integer housePrice = reader.nextInt();
		reader.nextName();
		reader.beginArray();
		List<Integer> rentalFee = new ArrayList<>();
		while (reader.hasNext()) {
			rentalFee.add(reader.nextInt());
		}
		reader.endArray();
		fields.add(new LandField(fieldName, price, city, housePrice, rentalFee));
		return;
	}

	private void addPurchasableField() throws IOException {
		reader.nextName();
		String fieldName = reader.nextString();
		reader.nextName();
		Integer price = reader.nextInt();
		reader.nextName();
		reader.beginArray();
		List<Integer> incomings = new ArrayList<>();
		while (reader.hasNext()) {
			incomings.add(reader.nextInt());
		}
		reader.endArray();
		fields.add(new PurchasableField(fieldName, price, incomings));
		return;
	}

	private void addField() throws IOException {
		reader.nextName();
		String fieldName = reader.nextString();
		fields.add(new Field(fieldName));
		return;
	}

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

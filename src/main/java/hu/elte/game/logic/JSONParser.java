package hu.elte.game.logic;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.stream.JsonReader;

public class JSONParser {

	public static List<IField> readFields(String fileName) throws IOException {
		List<IField> fields=new ArrayList<>();
		JsonReader reader = new JsonReader(new FileReader(fileName));
		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			// egyMezotHozzaad();
			reader.nextName();
			String fieldType = reader.nextString();
			if (fieldType.equals("Field")) {
				reader.nextName();
				String fieldName = reader.nextString();
				fields.add(new Field(fieldName));
			} else if (fieldType.equals("PurchasableField")) {
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
			} else if (fieldType.equals("LandField")) {
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
			}
			//////////////////
			reader.endObject();
		}
		reader.endArray();
		reader.close();
		return fields;
	}
	
	public static List<Card> readCards(String fileName) throws IOException, ParseException {
		List<Card> fields=new ArrayList<>();
		JsonReader reader = new JsonReader(new FileReader(fileName));
		reader.beginObject();
		String name=reader.nextName();
		if(name.equals("ChanceCards")||name.equals("CommunityChestCards")){
			reader.beginArray();
			while(reader.hasNext()){
				reader.beginObject();
				Card card=new Card();
				String next="";
				while(reader.hasNext()){
					if(reader.nextName().equals("money")){
						next=reader.nextString();
						if(next.equals(""))
							card.setMoneyToNull();
						else
							card.setMoney(Integer.parseInt(next));
					}
					if(reader.nextName().equals("step")){
						next=reader.nextString();
						if(next.equals(""))
							card.setStepToNull();
						else
							card.setStep(Integer.parseInt(next));
					}
					if(reader.nextName().equals("text")){
						card.setText(reader.nextString());
					}
				}
				reader.endObject();
			}
			reader.endArray();
			reader.endObject();
			reader.close();
		}else{
			reader.endObject();
			reader.close();
			throw new ParseException("Nem megfelelõ ChanceCard JSON!", 0);
		}
		return fields;
	}

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

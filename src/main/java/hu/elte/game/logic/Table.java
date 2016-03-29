package hu.elte.game.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Table {
	private List<IField> fields;

	public Table() throws IOException {
		fields = new ArrayList<>();
		init();
	}

	private void init() throws IOException {
		JSONParser parser = new JSONParser("fields.json");
		fields = parser.start();
	}

	public IField getField(int i) {
		return fields.get(i);
	}

	public void setOwner(int i, Player p) {
		((PurchasableField) fields.get(i)).setOwner(p);
	}

	public void buildHouse(int i) {
		if (((LandField) fields.get(i)).getHouseCount() < 5) {
			((LandField) fields.get(i)).buildHouse();
		}
	}

	// public void buildHotel(int i) {
	// if (((LandField) fields.get(i)).getHouseCount() == 4) {
	// ((LandField) fields.get(i)).buildHouse();
	// }
	// }

	public void sellHouse(int i) {
		if (((LandField) fields.get(i)).getHouseCount() > 0) {
			((LandField) fields.get(i)).sellHouse();
		}
	}
}

package hu.elte.game.logic;

import java.util.List;

public class LandField extends PurchasableField {
	private String city;
	private int houseCount;
	private int housePrice;

	public LandField(String fieldName, Integer price, String city2, Integer housePrice2, List<Integer> rentalFee) {
		super(fieldName, price, rentalFee);
		houseCount = 0;
		this.city = city2;
		this.housePrice = housePrice2;
	}

	@Override
	public String toString() {
		return "LandField [" + super.toString() + "city=" + city + ", houseCount=" + houseCount + ", housePrice="
				+ housePrice + "]";
	}

	public String getCity() {
		return city;
	}

	public int getHouseCount() {
		return houseCount;
	}

	public int getHousePrice() {
		return housePrice;
	}

	public void buildHouse() {
		houseCount++;
	}

	public void sellHouse() {
		houseCount--;
	}

}

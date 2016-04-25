package hu.elte.game.logic.data;

import java.util.List;

public class LandField extends PurchasableField {
	private int houseCount;
	private int housePrice;
	private String city;

	public LandField(String fieldName, Integer price, String city, Integer housePrice2, List<Integer> rentalFee) {
		super(fieldName, price, rentalFee);
		this.houseCount = 0;
		this.housePrice = housePrice2;
		this.city = city;
	}

	@Override
	public String toString() {
		return "LandField [" + super.toString() + "city=" + this.city + ", houseCount=" + houseCount + ", housePrice="
				+ housePrice + "]";
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object _other) {
		if (!(_other instanceof LandField)) {
			return false;
		}
		
		PurchasableField other = (PurchasableField) _other;
		return super.equals(other);
	}

	public String getCity() {
		return this.city;
	}

	public int getHouseCount() {
		return houseCount;
	}

	public int getHousePrice() {
		return housePrice;
	}

	public boolean buildHouse() {
		if (houseCount >= 5) {
			houseCount = 5;
			return false;
		}
		
		houseCount++;
		return true;
	}

	public boolean sellHouse() {
		if (houseCount <= 0) {
			houseCount = 0;
			return false;
		}
		
		houseCount--;
		return true;
	}

}

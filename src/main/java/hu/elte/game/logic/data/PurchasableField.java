package hu.elte.game.logic.data;

import java.util.List;

public class PurchasableField extends Field {
	private Player owner;
	private int price;
	private List<Integer> incomings;
	private boolean mortgage;
	private String subType;

	public PurchasableField(String fieldName, String subType, Integer price2, List<Integer> incomings) {
		super(fieldName);
		this.owner = null;
		this.subType = subType;
		this.mortgage = false;
		this.price = price2;
		this.incomings = incomings;
	}

	@Override
	public String toString() {
		return "PurchasableField [" + super.toString() + "price=" + price + ", incomings=" + incomings + "]";
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object _other) {
		if (!(_other instanceof PurchasableField)) {
			return false;
		}
		
		Field other = (Field) _other;
		return super.equals(other);
	}
	
	public String getSubType() {
		return this.subType;
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}

	public boolean isMortgage() {
		return mortgage;
	}

	public void setMortgage(boolean mortgage) {
		this.mortgage = mortgage;
	}

	public Player getOwner() {
		return owner;
	}

	public int getPrice() {
		return price;
	}

	public List<Integer> getIncomings() {
		return incomings;
	}

}

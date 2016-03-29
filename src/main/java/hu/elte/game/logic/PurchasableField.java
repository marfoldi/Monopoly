package hu.elte.game.logic;

import java.util.List;

public class PurchasableField extends Field {
	private Player owner;
	private int price;
	private List<Integer> incomings;
	private boolean mortgage;

	public PurchasableField(String fieldName, Integer price2, List<Integer> incomings) {
		super(fieldName);
		this.owner = null;
		this.mortgage = false;
		this.price = price2;
		this.incomings = incomings;
	}

	@Override
	public String toString() {
		return "PurchasableField [" + super.toString() + "price=" + price + ", incomings=" + incomings + "]";
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

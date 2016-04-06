package hu.elte.game.logic;

public class Player {
	private final int START_MONEY = 70000;
	
	private String name;
	private int position;
	private int money;
	
	public Player(String name) {
		this.name = name;
		this.position = 0;
		this.money = START_MONEY;
	}
	
	public int getPosition() {
		return this.position;
	}

	public int getMoney() {
		return this.money;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean decreaseWithHouse(LandField field) {
		if (this.money < field.getHousePrice()) {
			return false;
		}
		
		this.money -= field.getHousePrice();
		return true;
	}
	
	public boolean decreaseWithEstate(PurchasableField field) {
		if (this.money < field.getPrice()) {
			return false;
		}
		
		this.money -= field.getPrice();
		return true;
	}
	
}

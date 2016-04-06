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
	
	/**
	 * Decreases the Player's money by the argument's house price.
	 * @param field
	 * @return boolean, whether or not the Player had enough money to do the transfer
	 */
	public boolean decreaseWithHouse(LandField field) {
		if (this.money < field.getHousePrice()) {
			return false;
		}
		
		this.money -= field.getHousePrice();
		return true;
	}
	
	/**
	 * Decreases the Player's money by the argument's price.
	 * @param field A PurchasableField (can be LandField)
	 * @return boolean, whether or not the Player had enough money to do the transfer 
	 */
	public boolean decreaseWithEstate(PurchasableField field) {
		if (this.money < field.getPrice()) {
			return false;
		}
		
		this.money -= field.getPrice();
		return true;
	}

	/**
	 * Increases the Player's money by the half of the argument's house price.
	 * @param field
	 */
	public void increaseWithHouse(LandField field) {
		this.money += field.getHousePrice() / 2;		
	}
	
	/**
	 * Increases the Player's money by the half of the argument's price.
	 * @param field
	 */
	public void increaseWithEstate(PurchasableField field) {
		this.money += field.getPrice() / 2;
	}
	
}

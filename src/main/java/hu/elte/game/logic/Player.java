package hu.elte.game.logic;

public class Player implements IPlayer {
	private final int START_MONEY = 70000;
	
	private String name;
	private int position;
	private int money;
	
	public Player(String name) {
		this.name = name;
		this.position = 0;
		this.money = START_MONEY;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public int getPosition() {
		return this.position;
	}
	
	public void setPosition(int position) {
		this.position = (position < 0) ? 0 : position;
	}

	public int getMoney() {
		return this.money;
	}
	
	public void setMoney(int money) {
		this.money = money;
	}
	
	/**
	 * Modifies the Player's money by the amount of the argument
	 * Use negative value to indicate purchases
	 * Use positive value to indicate sales
	 * @param value
	 * @throws GameRuleException
	 *  - If the Player does not have enough money to do a purchase
	 */
	public void doTransaction(int value) throws GameRuleException {
		if (value == 0) return;
		if (value < 0 && this.money < value) {
			throw new GameRuleException(GameRuleException.CODE.INSUFFICIENT_FUNDS, "The Player does not have enough money to complete this transaction.");
		}
		
		this.money += value;
	}
}

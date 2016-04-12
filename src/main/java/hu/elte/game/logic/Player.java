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
}

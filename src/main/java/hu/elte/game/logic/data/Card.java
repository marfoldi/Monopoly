package hu.elte.game.logic.data;

public class Card {
	private Integer money;
	private Integer step;
	private String text;
	
	public Card(){
		super();
	}
	
	public Card(Integer money, Integer step, String text) {
		super();
		this.money = money;
		this.step = step;
		this.text = text;
	}
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getStep() {
		return step;
	}
	public void setStep(int step) {
		this.step = step;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public void setStepToNull(){
		this.step=null;
	}
	public void setMoneyToNull(){
		this.money=null;
	}

	@Override
	public String toString() {
		return "Money:"+money+" Step:"+step+" Text:"+text;
	}
	
}

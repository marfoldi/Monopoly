package hu.elte.game.logic.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import hu.elte.game.logic.interfaces.IDice;

public class SimpleDice implements IDice {

	private int value;
	
	public SimpleDice() {
		Random rand = new Random();
		this.value = rand.nextInt(6) + 1;
	}
	@Override
	public int getSum() {
		return this.value;
	}

	@Override
	public List<Integer> getValues() {
		return new ArrayList<Integer>(this.value);
	}

}

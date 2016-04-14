package hu.elte.game.logic.data;

import java.util.ArrayList;
import java.util.List;

import hu.elte.game.logic.interfaces.IDice;

public class Dice implements IDice {

	int sum;
	ArrayList<Integer> values;
	
	public Dice(Integer ...values) {
		this.sum = 0;
		this.values = new ArrayList<Integer>();
		
		for (int i = 0; i < values.length; i++) {
			this.sum += values[i];
			this.values.add(values[i]);
		}
	}
	
	@Override
	public int getSum() {
		return this.sum;
	}

	@Override
	public List<Integer> getValues() {
		return this.values;
	}

}

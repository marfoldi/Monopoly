package hu.elte.game.logic.data;

import java.util.ArrayList;
import java.util.List;

import hu.elte.game.logic.interfaces.IDice;

public class DoubleDice implements IDice {
	
	private SimpleDice first;
	private SimpleDice second;
	
	public DoubleDice() {
		this.first = new SimpleDice();
		this.second = new SimpleDice();
	}

	@Override
	public int getSum() {
		return this.first.getSum() + this.second.getSum();
	}

	@Override
	public List<Integer> getValues() {
		ArrayList<Integer> retVal = new ArrayList<Integer>();
		retVal.addAll(this.first.getValues());
		retVal.addAll(this.second.getValues());
		return retVal;
	}

}

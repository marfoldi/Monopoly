package hu.elte.game.logic.data;

import hu.elte.game.logic.interfaces.IField;

public class Field implements IField {
	private String name;
	
	public Field(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + "]";
	}
	
	@Override
	public int hashCode() {
		int sum = 0;
		for (int i=0; i<this.name.length(); i++) {
			sum += this.name.charAt(i);
		}
		
		return sum;
	}
	
	@Override
	public boolean equals(Object _other) {
		if (!(_other instanceof Field)) {
			return false;
		}
		
		Field other = (Field) _other;
		return this.name.equals(other.getName());
	}

	@Override
	public String getName() {
		return this.name;
	}

}

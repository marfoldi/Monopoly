package hu.elte.game.logic;

public class Field implements IField {
	private String name;

	public Field(String fieldName) {
		name = fieldName;
	}

	@Override
	public String toString() {
		return "Field [name=" + name + "]";
	}

	public String getName() {
		return name;
	}

}

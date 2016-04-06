package hu.elte.game.logic;

public class InvalidFieldException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidFieldException(Class<?> expected, Class<?> received) {
		super("Expected class: " + expected.toString() + ", received: " + received.toString());
	}
}

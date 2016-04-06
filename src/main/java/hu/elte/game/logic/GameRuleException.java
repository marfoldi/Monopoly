package hu.elte.game.logic;

public class GameRuleException extends Exception {
	private static final long serialVersionUID = 1L;
	private CODE code;

	public enum CODE {
		CONDITION_FAILURE, INSUFFICIENT_FUNDS
	}
	
	public GameRuleException(CODE code, String message) {
		super(message);
		this.code = code;
	}
	
	public CODE getCode() {
		return this.code;
	}
}

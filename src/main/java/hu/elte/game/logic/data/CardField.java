package hu.elte.game.logic.data;

public class CardField extends Field{

	private String subType;
	
	public CardField(String fieldName, String subType) {
		super(fieldName);
		this.subType = subType;
	}
	
	@Override
	public String toString() {
		return "CardField [" + super.toString() + "subType=" + subType + "]";
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object _other) {
		if (!(_other instanceof CardField)) {
			return false;
		}
		
		Field other = (Field) _other;
		return super.equals(other);
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
	
	

}


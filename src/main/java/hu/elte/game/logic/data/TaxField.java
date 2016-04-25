package hu.elte.game.logic.data;

public class TaxField  extends Field{

	private int price;
	
	public TaxField(String fieldName, Integer price) {
		super(fieldName);
		this.price = price;
	}
	
	@Override
	public String toString() {
		return "TaxField [" + super.toString() + "price=" + price + "]";
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object _other) {
		if (!(_other instanceof TaxField)) {
			return false;
		}
		
		Field other = (Field) _other;
		return super.equals(other);
	}
	
	public int getPrice() {
		return price;
	}

}

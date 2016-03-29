/**
 * 
 */
package hu.elte.game.view;

import java.util.List;

import javax.swing.JFrame;

/**
 * @author marfoldi
 *
 */
public class MonopolyTable extends JFrame {
	private List<Field> fields;

	/**
	 * @param fields
	 */
	public MonopolyTable(List<Field> fields) {
		super();
		this.setFields(fields);
	}

	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
}

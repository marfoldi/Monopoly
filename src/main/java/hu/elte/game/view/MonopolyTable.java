/**
 * 
 */
package hu.elte.game.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * @author marfoldi
 *
 */
public class MonopolyTable extends JPanel {
	private static final int TABLESIZE = 768;
	private static final int FIELDWIDTH = 70;
	private static final int FIELDHEIGHT = 70;
	private JPanel north, south, east, west;
	private List<Field> fields;

	/**
	 * @param fields
	 */
	public MonopolyTable(List<Field> fields) {
		super();
		this.setFields(fields);
		
		this.setSize(TABLESIZE, TABLESIZE);
		this.setLayout(new BorderLayout(0, 0));
		
		createFieldHolders();
		
		this.setVisible(true);
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
	
	/**
	 * Adds JPanels (holders) for the fields
	 */
	private void createFieldHolders() {
		GridLayout northSouthLayout = new GridLayout(1, 11);
		northSouthLayout.setHgap(0);
		northSouthLayout.setVgap(0);
		north = new JPanel();
		south = new JPanel();
		north.setPreferredSize(new Dimension(TABLESIZE, FIELDHEIGHT));
		north.setLayout(northSouthLayout);
		north.setBorder(null);
		south.setPreferredSize(new Dimension(TABLESIZE, FIELDHEIGHT));
		south.setLayout(northSouthLayout);
		south.setBorder(null);
		
		GridLayout eastWestLayout = new GridLayout(9, 1);
		eastWestLayout.setHgap(0);
		eastWestLayout.setVgap(0);
		east = new JPanel();
		west = new JPanel();
		east.setPreferredSize(new Dimension(FIELDHEIGHT, TABLESIZE - 2 * FIELDWIDTH));
		east.setLayout(eastWestLayout);
		east.setBorder(null);
		west.setPreferredSize(new Dimension(FIELDHEIGHT, TABLESIZE - 2 * FIELDWIDTH));
		west.setLayout(eastWestLayout);
		west.setBorder(null);
	
		addFieldsToHolders();
		
		this.add(north, BorderLayout.NORTH);
		this.add(south, BorderLayout.SOUTH);
		this.add(east, BorderLayout.EAST);
		this.add(west, BorderLayout.WEST);
	}
	
	/**
	 * Adds the Fields to the holders
	 */
	private void addFieldsToHolders() {
		for(int i=0; i<fields.size()/4+1; ++i) {
			north.add(fields.get(i));
		}
		for(int i=fields.size()/4+1; i<fields.size()/2; ++i) {
			east.add(fields.get(i));
		}
		for(int i=fields.size()/2; i<fields.size()-(fields.size()/4)+1; ++i) {
			south.add(fields.get(i));
		}
		for(int i=fields.size()-(fields.size()/4)+1; i<fields.size(); ++i) {
			west.add(fields.get(i));
		}
	}
}

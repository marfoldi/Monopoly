/**
 * 
 */
package hu.elte.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * @author marfoldi
 *
 */
public class MonopolyTable extends JPanel {
	private static final Dimension TABLESIZE = new Dimension(568, 568);
	private static final int FIELDWIDTH = 40;
	private static final int FIELDHEIGHT = 60;
	private List<Field> fields;

	/**
	 * @param fields
	 */
	public MonopolyTable(List<Field> fields) {
		super();
		this.setFields(fields);

		setLayout(new MigLayout("fill, gap 2 2"));
		
		try {
			displayFields();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	 * @throws IOException 
	 */
	private void displayFields() throws IOException {
		// top row
        add(new Field("Start", null, new Dimension(FIELDHEIGHT, FIELDHEIGHT)));
        displayHorizontalFields(fields.size()/4-1);
        add(new Field("Jail", null, new Dimension(FIELDHEIGHT, FIELDHEIGHT)), "wrap");

        // left column
        displayVerticalFields(fields.size()/4-1, "wrap");
        add(new Field("Parking", null, new Dimension(FIELDHEIGHT, FIELDHEIGHT)));

        // bottom row
        displayHorizontalFields(fields.size()/4-1);
        add(new Field("GOTO", null, new Dimension(FIELDHEIGHT, FIELDHEIGHT)));

        // right column
        displayVerticalFields(fields.size()/4-1, "cell");

        // center picture
        Field centerField = new Field("Picture", ImageIO.read(new File("./src/main/java/resources/img/Monopoly_pack_logo.png")), new Dimension(FIELDHEIGHT, FIELDHEIGHT));
        displayMonopolyImage(centerField);
        add(centerField, "cell 1 1 9 9, grow");
	}
	   
	private void displayHorizontalFields(int size) {
		for(int i=0; i<size; ++i) {
			add(new Field("Field", null, new Dimension(FIELDWIDTH, FIELDHEIGHT)));
		}
	}
	   
	private void displayVerticalFields(int size, String id) {
		for(int i=0; i<size; ++i) {
			if(id.equals("wrap")) { // It's the left column
				add(new Field("Field", null, new Dimension(FIELDHEIGHT, FIELDWIDTH)), id);
			}
			else {
				add(new Field("Field", null, new Dimension(FIELDHEIGHT, FIELDWIDTH)), "cell " + (size+1) + " " + (i+1));
			}
		}
	}
	
	@Override 
	protected void paintComponent(Graphics g)
	{
	   super.paintComponent(g);
	   setBackground(Color.decode("#c0e2ca"));
	}
	
	private void displayMonopolyImage(Field field) {
		ImageIcon image = new ImageIcon("/Monopoly_pack_logo.png");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		field.add(label);
	}
	
	@Override
    public Dimension getPreferredSize() {
        return TABLESIZE;
    }

	/**
	 * Probably does something important
	 */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }        
}

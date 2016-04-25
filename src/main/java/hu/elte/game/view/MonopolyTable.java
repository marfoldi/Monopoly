/**
 * 
 */
package hu.elte.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
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
	private final Dimension FIELDSIZE = getPreferedFieldSize();
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
        add(new Field("Start", null, new Dimension(FIELDSIZE.height, FIELDSIZE.height)));
        displayHorizontalFields(fields.size()/4+2);
        add(new Field("Jail", null, new Dimension(FIELDSIZE.height, FIELDSIZE.height)), "wrap");

        // left column
        displayVerticalFields(fields.size()/4-4, "wrap");
        add(new Field("Parking", null, new Dimension(FIELDSIZE.height, FIELDSIZE.height)));

        // bottom row
        displayHorizontalFields(fields.size()/4+2);
        add(new Field("GOTO", null, new Dimension(FIELDSIZE.height, FIELDSIZE.height)));

        // right column
        displayVerticalFields(fields.size()/4-4, "cell");

        // center picture
        Field centerField = new Field("Picture", ImageIO.read(new File("./src/main/java/resources/img/Monopoly_pack_logo.png")), new Dimension(FIELDSIZE.height, FIELDSIZE.height));
        displayMonopolyImage(centerField);
        add(centerField, "cell 1 1 " + (fields.size()/4+2) + " " + (fields.size()/4-4) +", grow");
	}
	   
	private void displayHorizontalFields(int size) {
		for(int i=0; i<size; ++i) {
			add(new Field("Field", null, new Dimension(FIELDSIZE.width, FIELDSIZE.height)));
		}
	}
	   
	private void displayVerticalFields(int size, String id) {
		for(int i=0; i<size; ++i) {
			if(id.equals("wrap")) { // It's the left column
				add(new Field("Field", null, new Dimension(FIELDSIZE.height, FIELDSIZE.width)), id);
			}
			else {
				add(new Field("Field", null, new Dimension(FIELDSIZE.height, FIELDSIZE.width)), "cell " + (size+7) + " " + (i+1));
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
	
	public Dimension getPreferedFieldSize() {
		Dimension tableSize = this.getPreferredSize();
		return new Dimension((int) (tableSize.width*0.065), (int) (tableSize.height*0.13));
	}
	
	@Override
    public Dimension getPreferredSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();		
		return new Dimension((int) (screenSize.getWidth()*0.85), (int) (screenSize.getHeight()*0.9));
    }

	/**
	 * Probably does something important
	 */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }        
}

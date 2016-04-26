/**
 * 
 */
package hu.elte.game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hu.elte.game.Controller;
import hu.elte.game.view.centerpanel.ButtonSouthPanel;
import hu.elte.game.view.centerpanel.CenterPanel;
import hu.elte.game.view.centerpanel.NorthPlayersPanel;
import net.miginfocom.swing.MigLayout;

/**
 * @author marfoldi
 *
 */
public class MonopolyTable extends JPanel {
	private final Dimension FIELDSIZE = getPreferedFieldSize();
	private List<Field> fields;
	private Controller gameController;

	/**
	 * @param fields
	 * @param gameController 
	 * @throws IOException 
	 */
	public MonopolyTable(Controller gameController) throws IOException {
		super();
		this.gameController=gameController;
		this.setFields(gameController.getFields());

		setLayout(new MigLayout("fill, gap 2 2"));
		
		try {
			displayFields();
			displayMenu();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.setVisible(true);
	}

	private void displayMenu() {
		
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
		for(Field field:fields)
			field.setSize(new Dimension(FIELDSIZE.height, FIELDSIZE.width));
		
		// top row
        add(fields.get(0));
        displayHorizontalFields(1,12);
        add(fields.get(13), "wrap");

        // left column
        displayVerticalFields(14,19, "wrap");
        add(fields.get(20));

        // bottom row
        displayHorizontalFields(21,32);
        add(fields.get(33));

        // right column
        displayVerticalFields(34,39, "cell");

        // center picture
        CenterPanel centerPanel = new CenterPanel(ImageIO.read(new File("./src/main/java/resources/img/Monopoly_pack_logo.png")), new Dimension(FIELDSIZE.height, FIELDSIZE.height));
        displayMonopolyImage(centerPanel);
        add(centerPanel, "cell 1 1 " + (fields.size()/4+2) + " " + (fields.size()/4-4) +", grow");
	}
	   
	private void displayHorizontalFields(int start,int end) {
		for(int i=start; i<=end; ++i) {
			add(fields.get(i));
		}
	}
	   
	private void displayVerticalFields(int start,int end, String id) {
		for(int i=start; i<=end; ++i) {
			if(id.equals("wrap")) { // It's the left column
				add(fields.get(i), id);
			}
			else {
				add(fields.get(i), "cell " + (end-start+8) + " " + (i-start+1));
			}
		}
	}
	
	@Override 
	protected void paintComponent(Graphics g)
	{
	   super.paintComponent(g);
	   setBackground(Color.decode("#c0e2ca"));
	}
	
	private void displayMonopolyImage(CenterPanel centerPanel) {
		ImageIcon image = new ImageIcon("/Monopoly_pack_logo.png");
		JLabel label = new JLabel("", image, JLabel.CENTER);
		centerPanel.add(label);
		centerPanel.add(new ButtonSouthPanel(), BorderLayout.SOUTH);
		centerPanel.add(new NorthPlayersPanel(Arrays.asList("Jeno : 1400FT","Erzsi : 1400FT","Bözsi : 1400FT")), BorderLayout.NORTH);
	}
	
	public Dimension getPreferedFieldSize() {
		Dimension tableSize = this.getPreferredSize();
		return new Dimension((int) (tableSize.height*0.12), (int) (tableSize.height*0.12));
	}
	
	@Override
    public Dimension getPreferredSize() {
		Rectangle screenSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		double widthMultiplier = 0.85;
		if(screenSize.getWidth()/screenSize.getHeight() <= 1.7) {
			widthMultiplier = 0.95;
		}
		return new Dimension((int) (screenSize.getWidth()*widthMultiplier), (int) (screenSize.getHeight()*0.9));
    }

	/**
	 * Probably does something important
	 */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }        
}

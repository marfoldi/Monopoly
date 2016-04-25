/**
 * 
 */
package hu.elte.game.view.centerpanel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * @author marfoldi
 *
 */
public class CenterPanel extends JPanel {
	private Image img;
	private Dimension size;
	
	/**
	 * CenterPanel constructor
	 * @param img
	 * @param size
	 */
	public CenterPanel(Image img,Dimension size) {
		super();
		this.img = img;
		this.size = size;
		setLayout(new BorderLayout());
		
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	@Override 
	protected void paintComponent(Graphics g)
	{
	   super.paintComponent(g);
	   setBackground(Color.decode("#c0e2ca"));
	   if(img != null) {
		   Graphics2D g2d = (Graphics2D) g;
		    int x = (this.getWidth() - img.getWidth(null)) / 2;
		    int y = (this.getHeight() - img.getHeight(null)) / 2;
		    g2d.drawImage(img, x, y, null);
	   }
	}
	
	/**
	 * Returns the displayed size of the field
	 */
	@Override
    public Dimension getPreferredSize() {
        return size;
    }

	/**
	 * Probably does something important
	 */
    @Override
    public Dimension getMinimumSize() {
        return getPreferredSize();
    }        
}

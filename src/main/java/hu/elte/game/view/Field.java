/**
 * 
 */
package hu.elte.game.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * @author marfoldi
 *
 */
public class Field extends JPanel {
	protected String name;
	protected Image img;
	protected List<Player> players;
	protected Dimension size;
	protected Color city;
	protected String price;
	protected JLabel owner;
	
	
	/**
	 * Constructor for the Start field
	 * @param name
	 * @param img
	 * @param players
	 */
	public Field(String name, Image img, List<Player> players, Dimension size) {
		super();
		this.name = name;
		this.img = img;
		this.players = players;
		this.size = size;
		
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Constructor for the Controller
	 * @param name
	 * @param img
	 * @param players
	 */
	public Field(String name, Image img, Color city, String price) {
		super();
		this.name = name;
		this.img = img;
		this.players = new ArrayList<>();
		this.size = new Dimension();
		this.city = city;
		this.price = price;
		displayData();
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	private void displayData() {
		JLabel tempLabel=new JLabel();
		//Landfield
		if(img==null&&name!=null&&city!=null&&price!=null){
			setLayout(new GridLayout(4,1));
			add(owner=new JLabel());
			tempLabel=new JLabel(name);
			add(tempLabel);
			tempLabel=new JLabel();
			tempLabel.setBackground(city);
			tempLabel.setOpaque(true);
			add(tempLabel);
			tempLabel=new JLabel(price);
			add(tempLabel);
		}
		//purchasable
		if(img!=null&&name!=null&&city==null&&price!=null){
			setLayout(new GridLayout(4,1));
			add(owner=new JLabel());
			tempLabel=new JLabel(name);
			add(tempLabel);
			tempLabel=new JLabel(new ImageIcon(img));
			add(tempLabel);
			tempLabel=new JLabel(price);
			add(tempLabel);
		}
		//taxfield
		if(img==null&&name!=null&&city==null&&price!=null){
			setLayout(new GridLayout(2,1));
			tempLabel=new JLabel(name);
			add(tempLabel);
			tempLabel=new JLabel(price);
			add(tempLabel);
		}
		//card
		if(img!=null&&name!=null&&city==null&&price==null){
			setLayout(new GridLayout(2,1));
			tempLabel=new JLabel(name);
			add(tempLabel);
			tempLabel=new JLabel(new ImageIcon(img));
			add(tempLabel);
		}
		//sima field
		if(img==null&&name!=null&&city==null&&price==null){
			setLayout(new GridLayout(1,1));
			tempLabel=new JLabel(name);
			add(tempLabel);
		}
	}

//	@Override 
//	protected void paintComponent(Graphics g)
//	{
//	   super.paintComponent(g);
//	   setBackground(Color.decode("#c0e2ca"));
//	   if(img != null) {
//		   Graphics2D g2d = (Graphics2D) g;
//		    int x = (this.getWidth() - img.getWidth(null)) / 2;
//		    int y = (this.getHeight() - img.getHeight(null)) / 2;
//		    g2d.drawImage(img, x, y, null);
//	   }
//	}
	
	/**
	 * Constructor for the fields except the Start
	 * @param name
	 * @param img
	 */
	public Field(String name, Image img, Dimension size) {
		super();
		this.name = name;
		this.img = img;
		this.players = new ArrayList<>();
		this.size = size;
		
		setBorder(BorderFactory.createEtchedBorder());
	}
	
	/**
	 * Adds a player to the field
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
	}
	
	/**
	 * Removes a player from the field
	 * @param player
	 */
	public void removePlayer(Player player) {
		this.players.remove(player);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
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
    
    public void setSize(Dimension size) {
    	this.size = size;
    }
}

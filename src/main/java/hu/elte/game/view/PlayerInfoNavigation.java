package hu.elte.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hu.elte.game.logic.data.Player;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PlayerInfoNavigation extends JPanel{

	private JButton leftArrow,rightArrow;
	private JLabel player;
	private JPanel navigationPanel,actDataPanel;
	private List<JPanel> dataPanels;

	ActionListener arrowAction=new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==leftArrow){
				Collections.rotate(dataPanels, -1);
			}else{
				Collections.rotate(dataPanels, 1);
			}
			remove(actDataPanel);
			add(actDataPanel=dataPanels.get(0));
			player.setText(actDataPanel.getName());
		}
	};
		
	public PlayerInfoNavigation() throws IOException{
		setLayout(new GridLayout(2,1));
		setBorder(BorderFactory.createLineBorder(Color.black));
		
		navigationPanel=new JPanel(new GridLayout(1, 3));
		leftArrow=createButton("./src/main/java/resources/img/leftRedArrow.png");
		rightArrow=createButton("./src/main/java/resources/img/rightRedArrow.png");
		player=new JLabel("Player");
		player.setMinimumSize(new Dimension(100, 40));
		player.setPreferredSize(new Dimension(100, 40));
		player.setMaximumSize(new Dimension(100, 40));
		player.setFont(new Font("Serif", Font.PLAIN, 30));
		player.setBackground(Color.WHITE);
		player.setOpaque(true);
		navigationPanel.add(leftArrow);
		navigationPanel.add(player);
		navigationPanel.add(rightArrow);
		add(navigationPanel);
	}

	private JButton createButton(String fileName) throws IOException {
		BufferedImage buttonIcon = ImageIO.read(new File(fileName));
		buttonIcon=resize(buttonIcon, 80, 40);
		JButton button = new JButton(new ImageIcon(buttonIcon));
		button.setBackground(Color.white);
		button.setBorder(BorderFactory.createEmptyBorder());
		button.addActionListener(arrowAction);
		return button;
	}
	
	private BufferedImage resize(BufferedImage img, int newW, int newH) { 
	    Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
	    BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2d = dimg.createGraphics();
	    g2d.drawImage(tmp, 0, 0, null);
	    g2d.dispose();
	    return dimg;
	}  
	
	public void setPlayers(List<Player> players){
		if(players==null || players.isEmpty())
			return;
		if(actDataPanel!=null)
			remove(actDataPanel);
		dataPanels=new ArrayList<>();
		for(Player player:players){
			JPanel dataPanel=new JPanel(new GridLayout(1, 1));
			dataPanel.setBorder(BorderFactory.createLineBorder(Color.black));
			dataPanel.add(new JLabel("Money: "+player.getMoney()));
			dataPanel.setName(player.getName());
			dataPanels.add(dataPanel);
		}
		add(actDataPanel=dataPanels.get(0));
		player.setText(actDataPanel.getName());
	}
}

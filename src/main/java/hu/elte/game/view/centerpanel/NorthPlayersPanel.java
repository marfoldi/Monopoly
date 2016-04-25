package hu.elte.game.view.centerpanel;

import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class NorthPlayersPanel extends JPanel{
	public NorthPlayersPanel(List<String> players){
		setLayout(new GridLayout(1, players.size()));
		for(String player:players)
			add(new JLabel(player));
	}
}

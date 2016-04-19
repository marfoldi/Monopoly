package hu.elte.game.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hu.elte.game.logic.data.Player;
/**
 * 
 * @author Dago
 *
 */
public class MenuBar extends JPanel{

	private static final long serialVersionUID = -5844689375546142702L;
	private JButton mortgageButton,unmortgageButton,sellButton,buyButton,rollDiceButton;
	private PlayerInfoNavigation pni;
	private PlayerInfo playerInfo;

	public MenuBar() throws IOException{
		setLayout(new GridLayout(7,1));
		initButton(mortgageButton=new JButton("Mortgage"));
		
		initButton(unmortgageButton=new JButton("UnMortgage"));
		initButton(sellButton=new JButton("SELL"));
		initButton(buyButton=new JButton("BUY"));
		initButton(rollDiceButton=new JButton("RollDice"));
		pni=new PlayerInfoNavigation();
		pni.setPlayers(Arrays.asList(new Player("Jenõ", 1000),new Player("Béla", 1500),new Player("Kálmán", 2000)));
		playerInfo=new PlayerInfo();
		playerInfo.setPlayerName("Gábor");
		playerInfo.setMoney("Money: 1100");
		add(pni);
		add(playerInfo);
		add(mortgageButton);
		add(unmortgageButton);
		add(sellButton);
		add(buyButton);
		add(rollDiceButton);
	}

	private void initButton(JButton jButton) {
		jButton.setFont(new Font("Serif", Font.PLAIN, 30));
		jButton.setForeground(Color.WHITE);
		jButton.setBackground(new Color(161, 8, 8));
		jButton.setMinimumSize(new Dimension(20, 20));
		jButton.setPreferredSize(new Dimension(20, 20));
		jButton.setMaximumSize(new Dimension(20, 20));
	}
}

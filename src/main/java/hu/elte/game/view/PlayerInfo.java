package hu.elte.game.view;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class PlayerInfo extends JPanel{
	private static final String ACTPLAYER="Actual Player";
	private JLabel playerName,playerMoney;
	
	public PlayerInfo(){
		setLayout(new GridLayout(3, 1));
		setBorder(BorderFactory.createLineBorder(Color.black));
		add(new JLabel(ACTPLAYER, SwingConstants.CENTER));
		add(playerName=new JLabel("Player", SwingConstants.CENTER));
		add(playerMoney=new JLabel("Money: ", SwingConstants.CENTER));
		playerMoney.setFont(new Font("Serif", Font.PLAIN, 30));
		playerName.setFont(new Font("Serif", Font.PLAIN, 30));
	}
	
	public String getPlayerName() {
		return playerName.getText();
	}
	public void setPlayerName(String playerName) {
		this.playerName.setText(playerName);
	}
	public String getMoney() {
		return playerMoney.getText();
	}
	public void setMoney(String money) {
		playerMoney.setText(money);
	}
	
	

}

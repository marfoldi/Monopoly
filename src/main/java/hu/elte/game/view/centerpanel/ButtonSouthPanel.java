/**
 * 
 */
package hu.elte.game.view.centerpanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;

import hu.elte.game.view.estatemanager.BuyTableModel;
import hu.elte.game.view.estatemanager.EstateManager;
import hu.elte.game.view.estatemanager.MortageTableModel;
import hu.elte.game.view.estatemanager.SellTableModel;
import hu.elte.game.view.estatemanager.UnMortageTableModel;

/**
 * @author Dago
 *
 */
public class ButtonSouthPanel extends JPanel{
	JButton sellButton;
	JButton buyButton;
	JButton mortageButton;
	JButton unMortageButton;
	
	public ButtonSouthPanel() {
		setLayout(new GridLayout(1, 4));
		initButtons();
	}
	
	private void initButtons() {
		JButton buttons[] = {new JButton("Elad"), new JButton("Vásárol"), new JButton("Zálogosít"), new JButton("Zálogból kivesz")};
		AbstractTableModel tableModels[] = {new SellTableModel(), new BuyTableModel(), new MortageTableModel(), new UnMortageTableModel()};
		
		for(int i=0; i<buttons.length; ++i) {
			addListenerToButton(buttons[i], tableModels[i]);
		}
		
		for(int i=0; i<buttons.length; ++i) {
			add(buttons[i], i);
		}
	}
	
	private void addListenerToButton(JButton button, AbstractTableModel tableModel) {
		button.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
		        SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		          new EstateManager(tableModel);
		        }
		        });
		    }
		 });
	}
}

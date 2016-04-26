/**
 * 
 */
package hu.elte.game.view.centerpanel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		List<AbstractTableModel> tableModels = new ArrayList<>();
		tableModels.add(new SellTableModel());
		tableModels.add(new BuyTableModel());
		tableModels.add(new MortageTableModel());
		tableModels.add(new UnMortageTableModel());
		
		for(JButton button : buttons) {
			button.addActionListener(new ActionListener(){
			    public void actionPerformed(ActionEvent e){
			        SwingUtilities.invokeLater(new Runnable() {
			        public void run() {
			          new EstateManager(tableModels.get(0));
			          tableModels.remove(0);
			        }
			        });

			    }
			 });
		}
		
		for(int i=0; i<buttons.length; ++i) {
			add(buttons[i], i);
		}
	}
}

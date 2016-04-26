package hu.elte.game.view.estatemanager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

public class EstateManager extends JFrame {
	private JTable table;
	private JScrollPane pane;
	
	public static void main(String[] args) {
        new EstateManager(new SellTableModel());
    }

    public EstateManager(AbstractTableModel tableModel) {
    	table = new JTable(tableModel);
    	table.setBackground(Color.decode("#c0e2ca"));
    	pane = new JScrollPane(table);
    	pane.getViewport().setBackground(Color.decode("#c0e2ca"));
    	centerTableContent(tableModel);
    	
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
                setTitle("Ingatlan menedzser");
                setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                setLayout(new BorderLayout());
                add(pane);
                add(new JButton("Ok"), BorderLayout.SOUTH);
                pack();
                setVisible(true);
            }
        });
    }
    
    private void centerTableContent(AbstractTableModel tableModel) {
    	for(int i=0; i<tableModel.getColumnCount()-1; ++i) {
	    	DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    	rightRenderer.setHorizontalAlignment(JLabel.CENTER);
	    	table.getColumnModel().getColumn(i).setCellRenderer(rightRenderer);
    	}
    }
}
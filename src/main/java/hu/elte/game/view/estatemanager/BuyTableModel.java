/**
 * 
 */
package hu.elte.game.view.estatemanager;

import javax.swing.table.AbstractTableModel;

/**
 * @author marfoldi
 *
 */
public class BuyTableModel extends AbstractTableModel {
    private Object[][] data;
    private String[] header = {"Név", "Épületek száma", "Érték", "Vásárol"};

    public BuyTableModel() {
        data = new Object[3][4];
        data[0] = new Object[]{"Egyetem utca", 1, 1000, false};
        data[1] = new Object[]{"Színház tér", 0, 1500, false};
        data[2] = new Object[]{"Petofi tér", 0, 2000, false};
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
    	return col == data[0].length-1;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public Class getColumnClass(int column) {
    	switch (column) {
    		case 0: return String.class;
    		case 1: return Integer.class;
    		case 2: return Integer.class;
    		case 3: return Boolean.class;
    		default: return String.class;
    	}
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == data[0].length-1) {
            if (aValue instanceof Boolean) {
                data[rowIndex][columnIndex] = (Boolean)aValue;
                fireTableCellUpdated(rowIndex, columnIndex);
            }
        }
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }
}

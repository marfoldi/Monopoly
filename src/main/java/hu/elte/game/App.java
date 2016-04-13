package hu.elte.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import hu.elte.game.view.Field;
import hu.elte.game.view.MonopolyTable;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	JFrame frame = new JFrame();
    	frame.setTitle("Monopoly");
    	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	frame.setLayout(new BorderLayout(0,0));
    	frame.add(new MonopolyTable(createDummyFields()), BorderLayout.CENTER);
    	
    	frame.setVisible(true);
    	
    	frame.pack();
    }
    
    private static List<Field> createDummyFields() {
    	int FIELDSSIZE = 40;
    	List<Field> fields = new ArrayList<>();
    	for(int i=0; i<FIELDSSIZE; ++i) {
    		fields.add(new Field("Field" + i+1, null, new Dimension())); //It's empty atm...
    	}
    	return fields;
    }
}

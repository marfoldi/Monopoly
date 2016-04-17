package hu.elte.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import hu.elte.game.view.Field;
import hu.elte.game.view.GameScreen;
import hu.elte.game.view.MonopolyTable;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	GameScreen gs = new GameScreen();
    	gs.add(new MonopolyTable(createDummyFields()), BorderLayout.CENTER);
    	gs.pack(); //Recalculate the frame size
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

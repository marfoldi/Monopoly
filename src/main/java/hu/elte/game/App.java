package hu.elte.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

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
    	Controller gameController = new Controller();
    	GameScreen gameScreen = new GameScreen();
    	/* TODO: GOT A NULLPTR HERE:
    	 * gameScreen.add(new MonopolyTable(gameController.getTableAsUITable()), BorderLayout.CENTER);
    	 */
    	gameScreen.add(new MonopolyTable(createDummyFields()), BorderLayout.CENTER);
    	gameScreen.pack(); //Recalculate the frame size
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

package hu.elte.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.elte.game.view.Field;
import hu.elte.game.view.GameScreen;
import hu.elte.game.view.MenuBar;
import hu.elte.game.view.MonopolyTable;
import hu.elte.game.view.Player;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws IOException
    {
    	Controller gameController = new Controller();
    	gameController.createGame(Arrays.asList("Jeno","Jozsi"));
    	GameScreen gameScreen = new GameScreen();
    	/* TODO: GOT A NULLPTR HERE:
    	 * gameScreen.add(new MonopolyTable(gameController.getTableAsUITable()), BorderLayout.CENTER);
    	 */
    	gameScreen.add(new MonopolyTable(createDummyFields()), BorderLayout.WEST);
    	gameScreen.add(new MenuBar(),BorderLayout.EAST);
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

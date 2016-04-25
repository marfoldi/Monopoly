package hu.elte.game;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
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
    	List<String> players=Arrays.asList("Jozsi","Erzsi","Pista","Joska");
    	Controller gameController = new Controller();
//    	gameController.createGame(players);
    	GameScreen gameScreen = new GameScreen();
    	/* TODO: GOT A NULLPTR HERE:
    	 * gameScreen.add(new MonopolyTable(gameController.getTableAsUITable()), BorderLayout.CENTER);
    	 */
    	gameScreen.add(new MonopolyTable(gameController.getFields(),gameController), BorderLayout.CENTER);
    	gameScreen.pack(); //Recalculate the frame size
    }
}

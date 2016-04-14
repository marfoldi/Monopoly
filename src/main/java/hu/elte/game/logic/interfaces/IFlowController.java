package hu.elte.game.logic.interfaces;

import java.util.List;

import hu.elte.game.logic.ChangeSet;

/**
 * An implementation of this interface must be able to handle the turn based player switch analogy,
 * and also communicate the taken actions between players.
 *
 */
public interface IFlowController {
	/**
	 * Sets the listener which will be called to make the necessary changes in the controller - user interface
	 * @param listener
	 */
	public void setControllerListener(IFlowControllerListener listener);
	
	/**
	 * Sets the listener which will be called to make the necessary changes in the model
	 * @param listener
	 */
	public void setModelListener(IFlowControllerListener listener);
	
	/**
	 * Sets the players, the 'nextPlayer' will choose among these names
	 * @param playerNames
	 */
	public void setPlayers(List<String> playerNames);
	
	/**
	 * Call this when the player's turn is over, and the control must be transferred to the next player
	 * @param changeSet (optional) the final actions the player took before handling over the control 
	 */
	public void nextPlayer(ChangeSet changeSet);
	
	/**
	 * Call this when the player's took actions that requires other players to see it 
	 * @param changeSet the actions took by the player
	 */
	public void sendChangeSet(ChangeSet changeSet);
	
}

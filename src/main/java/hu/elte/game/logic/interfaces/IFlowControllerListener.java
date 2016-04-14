package hu.elte.game.logic.interfaces;

import hu.elte.game.logic.ChangeSet;

/**
 * Listener used in the IFlowController
 *
 */
public interface IFlowControllerListener {
	/**
	 * Called when the current player changes
	 * @param playerName the now acting player's name
	 */
	public void onCurrentPlayerChange(String playerName);
	
	/**
	 * Called when changes happened at a player, and they need to be displayed / stored this side
	 * @param changeSet
	 */
	public void onGameStateChange(ChangeSet changeSet);
}

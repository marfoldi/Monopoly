package hu.elte.game.logic;

import java.util.List;

import hu.elte.game.logic.interfaces.IFlowController;
import hu.elte.game.logic.interfaces.IFlowControllerListener;

/**
 * This controller is able to handle the turn based player switch analogy on a single computer
 * That means more than one player use the same computer, there is no network communication needed
 *
 */
public class LocalFlowController implements IFlowController {

	private IFlowControllerListener controllerListener;
	private IFlowControllerListener modelListener;
	private List<String> players;
	private int currentPlayerIndex;
	
	public LocalFlowController() {
		currentPlayerIndex = 0;
	}
	
	@Override
	public void setControllerListener(IFlowControllerListener listener) {
		this.controllerListener = listener;
		
	}

	@Override
	public void setModelListener(IFlowControllerListener listener) {
		this.modelListener = listener;
		
	}
	
	@Override
	public void setPlayers(List<String> playerNames) {
		this.players = playerNames;
		
	}

	@Override
	public void nextPlayer(ChangeSet changeSet) {
		if (this.players == null || this.players.size() < 2) {
			// throw error
		}
		if (this.controllerListener == null || this.modelListener == null) {
			// throw error
		}
		
		currentPlayerIndex = (currentPlayerIndex + 1) % this.players.size();
		controllerListener.onCurrentPlayerChange(this.players.get(currentPlayerIndex));
		
		controllerListener.onGameStateChange(changeSet);
		modelListener.onGameStateChange(changeSet);
		
	}

	@Override
	public void sendChangeSet(ChangeSet changeSet) {
		controllerListener.onGameStateChange(changeSet);
		modelListener.onGameStateChange(changeSet);
		
	}	

}

package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;

public abstract class AI implements BattleObserver {
	
	protected BattleController battleController;
	protected BattleMap map;
	protected boolean done;
	
	public AI(BattleController battleController) {
		this.battleController = battleController;
		this.battleController.registerObserver(this);
		this.map = battleController.getBattleMap();
		done = false;
	}
	
	/**
	 * Indicates that the AI may start calculating its turn moves. Initialization starts here
	 */
	public void startTurn() {
		done = false;
	}
	
	/**
	 * Called when the AI has finished performing all of its moves.
	 */
	protected void endTurn() {
		done = true;
		this.battleController.endTurn();
	}
	
	public boolean isDone() {
		return done;
	}
	
	/**
	 * Performs either a single move, or calls endTurn()
	 */
	public abstract void performMove();
	
	@Override
	public void notifyBattleEvent(BattleEvent ev) {
		// Inherited classes can override this if they want to only use information gained through notifications 
	}
	
}

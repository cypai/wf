package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.BattleObserver;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;

public abstract class AI implements BattleObserver {

	private BattleController battleController;
	private BattleConfiguration battleConfiguration;
	private BattleMap battleMap;
	private boolean done;

	public AI(BattleController battleController) {
		this.battleController = battleController;
		this.battleController.registerObserver(this);
		battleConfiguration = battleController.getBattleConfiguration();
		battleMap = battleController.getBattleMap();
		done = false;
	}

	public final BattleController getBattleController() {
		return battleController;
	}

	public final BattleConfiguration getBattleConfiguration() {
		return battleConfiguration;
	}

	public final BattleMap getBattleMap() {
		return battleMap;
	}

	/**
	 * Indicates that the AI may start calculating its turn moves. Initialization starts here
	 */
	public final void startTurn() {
		done = false;
		startTurnInit();
	}

	protected void startTurnInit() {
	}

	/**
	 * Called when the AI has finished performing all of its moves.
	 */
	protected void endTurn() {
		done = true;
		battleController.endTurn();
	}

	public final boolean isDone() {
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

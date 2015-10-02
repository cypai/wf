package com.pipai.wf.battle.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.Rng;

public class RandomAI extends AI {

	private static final Logger logger = LoggerFactory.getLogger(RandomAI.class);

	private LinkedList<Agent> enemyAgents, playerAgents, toAct;

	public RandomAI(BattleController battleController) {
		super(battleController);
		this.enemyAgents = new LinkedList<Agent>();
		this.playerAgents = new LinkedList<Agent>();
		for (Agent a : getBattleMap().getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				this.enemyAgents.add(a);
			} else if (a.getTeam() == Team.PLAYER) {
				this.playerAgents.add(a);
			}
		}
	}

	@Override
	protected void startTurnInit() {
		this.toAct = new LinkedList<Agent>();
		for (Agent a : this.enemyAgents) {
			if (!a.isKO()) {
				this.toAct.add(a);
			}
		}
	}

	@Override
	public void performMove() {
		Agent a = getFirstMovableAgent();
		if (a == null) {
			this.endTurn();
			return;
		}
		if (a.getAP() > 0) {
			Action act = generateRandomAction(a);
			try {
				getBattleController().performAction(act);
			} catch (IllegalActionException e) {
				logger.error("AI tried to perform illegal move: " + e.getMessage());
			}
		}
	}

	/*
	 * Returns null if there are no more Agents with AP
	 */
	protected Agent getFirstMovableAgent() {
		for (Agent a : this.enemyAgents) {
			if (!a.isKO() && a.getAP() > 0) {
				return a;
			}
		}
		return null;
	}

	protected Action generateRandomAction(Agent a) {
		Rng rng = getBattleConfiguration().getRng();
		ArrayList<Action> list = ActionListGenerator.generateWeaponActionList(a);
		int r = rng.nextInt(list.size() * 2 + 1);
		if (r < list.size()) {
			return list.get(r);
		} else {
			MapGraph graph = new MapGraph(getBattleMap(), a.getPosition(), a.getEffectiveMobility(), 1, 2);
			ArrayList<GridPosition> potentialTiles = graph.getMovableCellPositions(1);
			GridPosition destination = potentialTiles.get(rng.nextInt(potentialTiles.size()));
			LinkedList<GridPosition> path = graph.getPath(destination);
			return new MoveAction(a, path, 1);
		}
	}

}

package com.pipai.wf.battle.ai;

import java.util.ArrayList;
import java.util.LinkedList;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.UtilFunctions;

public class RandomAI extends AI {
	
	protected LinkedList<Agent> enemyAgents, playerAgents, toAct;
	
	public RandomAI(BattleController battleController) {
		super(battleController);
		this.enemyAgents = new LinkedList<Agent>();
		this.playerAgents = new LinkedList<Agent>();
		for (Agent a : this.map.getAgents()) {
			if (a.getTeam() == Team.ENEMY) {
				this.enemyAgents.add(a);
			} else if (a.getTeam() == Team.PLAYER) {
				this.playerAgents.add(a);
			}
		}
	}
	
	@Override
	public void startTurn() {
		super.startTurn();
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
				this.battleController.performAction(act);
			} catch (IllegalActionException e) {
				System.out.println("AI tried to perform illegal move: " + e.getMessage());
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
		int r = UtilFunctions.rng.nextInt(3);
		switch (r) {
		case 0:
			return new OverwatchAction(a, new SimpleRangedAttack());
		case 1:
			if (a.getCurrentWeapon().currentAmmo() == 0) {
				return new ReloadAction(a);
			} else {
				Agent target = playerAgents.get(UtilFunctions.rng.nextInt(playerAgents.size()));
				return new RangeAttackAction(a, target, new SimpleRangedAttack());
			}
		case 2:
			MapGraph graph = new MapGraph(this.map, a.getPosition(), a.getMobility(), 1);
			ArrayList<GridPosition> potentialTiles = graph.getMovableCellPositions();
			GridPosition destination = potentialTiles.get(UtilFunctions.rng.nextInt(potentialTiles.size()));
			LinkedList<GridPosition> path = graph.getPath(destination);
			return new MoveAction(a, path);
		default:
			return new OverwatchAction(a, new SimpleRangedAttack());
		}
	}
	
}
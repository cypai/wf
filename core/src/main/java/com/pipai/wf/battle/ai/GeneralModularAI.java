package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.WaitAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.AgentCoverCalculator;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;

public class GeneralModularAI extends ModularAI {

	private AgentCoverCalculator coverCalc;
	// private AgentVisionCalculator agentVisionCalc;
	private ArrayList<Agent> playerAgents;
	private MapGraph mapgraph;

	public GeneralModularAI(BattleController controller, Agent a) {
		super(controller, a);
		coverCalc = new AgentCoverCalculator(getBattleMap(), getBattleConfiguration());
		// agentVisionCalc = new AgentVisionCalculator(getBattleMap(), getBattleConfiguration());
		mapgraph = new MapGraph(getBattleMap(), getAiAgent().getPosition(), getAiAgent().getEffectiveMobility(), 1, 2);
		playerAgents = getAgentsInTeam(Team.PLAYER);
	}

	@Override
	public ActionScore getBestMove() {
		Agent a = getAiAgent();
		ActionScore best;
		if (coverCalc.isFlanked(a)) {
			best = getBestMoveAction();
		} else {
			best = getBestAttackAction();
		}
		if (best.getAction() == null) {
			return new ActionScore(new WaitAction(getBattleController(), getAiAgent()), 0);
		}
		return best;
	}

	private ActionScore getBestAttackAction() {
		return new ActionScore(new WaitAction(getBattleController(), getAiAgent()), 0);
	}

	private ActionScore getBestMoveAction() {
		ArrayList<GridPosition> potentialTiles = mapgraph.getMovableCellPositions(1);
		ActionScore best = new ActionScore(null, Float.MIN_NORMAL);
		for (GridPosition pos : potentialTiles) {
			float score = scorePosition(pos);
			MoveAction moveAction = new MoveAction(getBattleController(), getAiAgent(), mapgraph.getPath(pos), 1);
			best = best.compareAndReturnBetter(new ActionScore(moveAction, score));
		}
		return best;
	}

	private float scorePosition(GridPosition pos) {
		BattleMap map = getBattleMap();
		float min = Float.MAX_VALUE;
		for (Agent a : playerAgents) {
			if (a.isKO()) {
				continue;
			}
			DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
			float current = coverSystem.getBestCoverAgainstAttack(pos, a.getPosition()).getDefense();
			min = (current < min) ? current : min;
		}
		return min;
	}

}

package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapGraph;

public class GeneralModularAI extends ModularAI {

	private ArrayList<Agent> playerAgents;
	private MapGraph mapgraph;

	public GeneralModularAI(BattleMap map, Agent a) {
		super(map, a);
		mapgraph = new MapGraph(getBattleMap(), getAgent().getPosition(), getAgent().getMobility(), 1, 2);
		playerAgents = getAgentsInTeam(Team.PLAYER);
	}

	@Override
	public ActionScore getBestMove() {
		return getBestMoveAction();
	}

	private ActionScore getBestMoveAction() {
		ArrayList<GridPosition> potentialTiles = mapgraph.getMovableCellPositions(1);
		ActionScore best = new ActionScore(null, Float.MIN_VALUE);
		for (GridPosition pos : potentialTiles) {
			float score = scorePosition(pos);
			best.compareAndReturnBetter(new ActionScore(new MoveAction(getAgent(), mapgraph.getPath(pos), 1), score));
		}
		return best;
	}

	private float scorePosition(GridPosition pos) {
		BattleMap map = getBattleMap();
		float min = Float.MAX_VALUE;
		for (Agent a : playerAgents) {
			float current = DirectionalCoverSystem.getBestCoverAgainstAttack(map, pos, a.getPosition()).getDefense();
			min = (current < min) ? current : min;
		}
		return min;
	}

}

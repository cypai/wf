package com.pipai.wf.battle.vision;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.PeekingSquaresCalculator;
import com.pipai.wf.util.UtilFunctions;

public class AgentVisionCalculator {

	private BattleMap map;
	private BattleConfiguration config;
	private PeekingSquaresCalculator peekingCalc;

	public AgentVisionCalculator(BattleMap map, BattleConfiguration config) {
		this.map = map;
		this.config = config;
		peekingCalc = new PeekingSquaresCalculator(map);
	}

	public boolean canSee(Agent agent, Agent other) {
		for (GridPosition peekSquare : peekingCalc.getPeekingSquares(agent)) {
			for (GridPosition otherPeekSquare : peekingCalc.getPeekingSquares(other)) {
				if (UtilFunctions.gridPositionDistance(peekSquare, otherPeekSquare) < config.sightRange()) {
					if (LineOfSightCalculator.lineOfSight(map, peekSquare, otherPeekSquare)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public ArrayList<Agent> enemiesInRangeOf(Agent a) {
		ArrayList<Agent> l = new ArrayList<>();
		for (Agent mapAgent : map.getAgents()) {
			if (mapAgent.getTeam() != a.getTeam() && !mapAgent.isKO() && canSee(a, mapAgent)) {
				l.add(mapAgent);
			}
		}
		return l;
	}

}

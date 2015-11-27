package com.pipai.wf.battle.map;

import java.util.List;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.vision.AgentVisionCalculator;

public class AgentCoverCalculator {

	private AgentVisionCalculator visionCalc;
	private DirectionalCoverSystem coverSystem;
	private PeekingSquaresCalculator peekingCalc;

	public AgentCoverCalculator(BattleMap map, BattleConfiguration config) {
		visionCalc = new AgentVisionCalculator(map, config);
		coverSystem = new DirectionalCoverSystem(map);
		peekingCalc = new PeekingSquaresCalculator(map);
	}

	public CoverType getCoverType(Agent a) {
		return coverSystem.getCover(a.getPosition());
	}

	public boolean isFlanked(Agent a) {
		for (Agent enemy : visionCalc.enemiesInRangeOf(a)) {
			if (isFlankedBy(a, enemy)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFlankedBy(Agent target, Agent potentialFlanker) {
		List<GridPosition> otherPosList = peekingCalc.getPeekingSquares(potentialFlanker);
		for (GridPosition otherPos : otherPosList) {
			if (coverSystem.isFlankedBy(target.getPosition(), otherPos)) {
				return true;
			}
		}
		return false;
	}

	public boolean isOpen(Agent a) {
		return coverSystem.isOpen(a.getPosition());
	}

}

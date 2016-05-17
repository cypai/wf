package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.PeekingSquaresCalculator;
import com.pipai.wf.util.GridPosition;

public class DefenseCalculator {

	private BattleMap map;
	private PeekingSquaresCalculator peekingCalc;

	public DefenseCalculator(BattleMap map) {
		this.map = map;
		peekingCalc = new PeekingSquaresCalculator(map);
	}

	public int getDefenseAgainst(Agent attacker, Agent defender) {
		int lowest = Integer.MAX_VALUE;
		for (GridPosition pos : peekingCalc.getPeekingSquares(attacker)) {
			int curr = getDefense(pos, defender.getPosition(), defender.getDefense());
			lowest = (curr < lowest) ? curr : lowest;
		}
		return lowest;
	}

	private int getDefense(GridPosition attackerPos, GridPosition defenderPos, int baseDefense) {
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		int situationalDef = baseDefense + coverSystem.getBestCoverAgainstAttack(defenderPos, attackerPos).getDefense();
		return situationalDef;
	}

}

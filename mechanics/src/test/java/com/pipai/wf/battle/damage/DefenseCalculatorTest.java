package com.pipai.wf.battle.damage;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.util.GridPosition;

public class DefenseCalculatorTest {

	@Test
	public void testVerticalFlankDefense() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 1 0
		 * 0 0 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 2 2";

		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		BattleMap map = new BattleMap(new MapString(rawMapString));
		AgentFactory factory = new AgentFactory();
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 1, 1, 1, 0));
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 1, 1, 1, 1, 1, 0));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		DefenseCalculator defenseCalc = new DefenseCalculator(map);
		Assert.assertEquals(CoverType.NONE.getDefense(), defenseCalc.getDefenseAgainst(enemy, player));
	}

	@Test
	public void testVerticalFullCoverDefense() throws BadStateStringException {
		/*
		 * Map looks like:
		 * 0 0 0 0
		 * 0 A 0 0
		 * 0 1 0 0
		 * 0 E 0 0
		 */
		String rawMapString = "4 4\n"
				+ "s 1 1";

		GridPosition playerPos = new GridPosition(1, 2);
		GridPosition enemyPos = new GridPosition(1, 0);
		BattleMap map = new BattleMap(new MapString(rawMapString));
		AgentFactory factory = new AgentFactory();
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 1, 1, 1, 0));
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 1, 1, 1, 1, 1, 0));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		DefenseCalculator defenseCalc = new DefenseCalculator(map);
		Assert.assertEquals(CoverType.FULL.getDefense(), defenseCalc.getDefenseAgainst(enemy, player));
	}

}

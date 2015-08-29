package com.pipai.wf.test.battle;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;

public class WeaponTest {

	@Test
	public void testNoWeapon() {
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		map.addAgent(playerState);
		Agent player = map.getAgentAtPos(playerPos);
		assertTrue(player.getCurrentWeapon() == null);
	}

}

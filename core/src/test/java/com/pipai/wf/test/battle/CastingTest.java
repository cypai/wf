package com.pipai.wf.test.battle;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.ReadySpellAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.exception.IllegalActionException;

public class CastingTest {
	
	@Test
	public void testAgentIllegalDestinationMoveAction() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
        map.addAgent(AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
        map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		assertTrue(player.getSpellList().size() == 0);
		ReadySpellAction ready = new ReadySpellAction(player, new FireballSpell());
		try {
			battle.performAction(ready);
			fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
			
		}
	}
}

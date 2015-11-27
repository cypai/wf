package com.pipai.wf.battle.damage;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;

public class DamageDealerTest {

	@Test
	public void testMakeAgentInactiveOnKO() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition agentPosition = new GridPosition(0, 0);
		AgentStateFactory factory = new AgentStateFactory();
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, agentPosition, 1, 1, 1, 1, 1, 1));
		Agent a = map.getAgentAtPos(agentPosition);
		DamageDealer damageDealer = new DamageDealer(map);
		damageDealer.doDamage(new DamageResult(true, false, 1, 0), a);
		Assert.assertEquals(0, a.getHP());
		Assert.assertTrue(a.isKO());
		Assert.assertFalse(map.getCell(agentPosition).hasAgent());
	}

}

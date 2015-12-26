package com.pipai.wf.unit.schema;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.GridPosition;

public class UnitSchemaTest {

	@Test
	public void testConstructFromAgentState() {
		AgentState as = new AgentStateFactory().battleAgentFromStats(Team.PLAYER, new GridPosition(0, 0),
				5, 4, 2, 60, 10, 1);
		Agent a = new Agent(as);
		a.setHP(3);
		a.setMP(2);
		a.setAP(0);
		UnitSchema schema = new UnitSchema(new AgentState(a));
		// HP should stay unchanged
		Assert.assertEquals(3, schema.getHP());
		Assert.assertEquals(5, schema.getMaxHP());
		// MP should be refreshed after battle
		Assert.assertEquals(4, schema.getMP());
		Assert.assertEquals(4, schema.getMaxMP());
		// AP should be refreshed after battle
		Assert.assertEquals(2, schema.getAP());
		Assert.assertEquals(2, schema.getMaxAP());
		Assert.assertEquals(60, schema.getAim());
		Assert.assertEquals(10, schema.getMobility());
		Assert.assertEquals(1, schema.getDefense());
	}

}

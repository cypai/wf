package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.GridPosition;

public class RegenerationAbilityTest {

	@Test
	public void testHealOnTurnEnd() {
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.getAbilities().add(new RegenerationAbility(1));
		Agent agent = new Agent(as);
		agent.setHP(agent.getMaxHP() - 1);
		Assert.assertFalse(agent == null);
		Assert.assertEquals(agent.getMaxHP() - 1, agent.getHP());
		agent.onRoundEnd();
		// Regeneration should proc at end of enemy turn
		Assert.assertEquals(agent.getMaxHP(), agent.getHP());
	}

}

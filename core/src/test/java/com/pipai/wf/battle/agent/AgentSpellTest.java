package com.pipai.wf.battle.agent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.spell.FireballSpell;
import com.pipai.wf.unit.ability.FireballAbility;

public class AgentSpellTest {

	@Test
	public void testSpellGranted() {
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.getAbilities().add(new FireballAbility());
		Agent agent = new Agent(as);
		Assert.assertTrue(agent.getAbilities().hasSpell(FireballSpell.class));
	}

}

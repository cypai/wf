package com.pipai.wf.battle.agent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.spell.FireballSpell;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.unit.ability.FireballAbility;

public class AgentSpellTest {

	@Test
	public void testSpellGranted() {
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		Agent agent = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		agent.getAbilities().add(new FireballAbility());
		Assert.assertTrue(agent.getAbilities().hasSpell(FireballSpell.class));
	}

}

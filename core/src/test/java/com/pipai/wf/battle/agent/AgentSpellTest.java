package com.pipai.wf.battle.agent;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.unit.ability.FireballAbility;

public class AgentSpellTest {

	@Test
	public void testSpellGranted() {
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPosition = mock(GridPosition.class);
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new FireballAbility());
		Agent agent = new Agent(as, mockMap, mockConfig);
		assertTrue(agent.getAbilities().hasSpell(FireballSpell.class));
	}

}

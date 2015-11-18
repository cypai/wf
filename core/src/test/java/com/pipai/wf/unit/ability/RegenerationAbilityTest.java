package com.pipai.wf.unit.ability;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;

public class RegenerationAbilityTest {

	@Test
	public void testHealOnTurnEnd() {
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPosition = mock(GridPosition.class);
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new RegenerationAbility(1));
		Agent agent = new Agent(as, mockMap, mockConfig);
		agent.setHP(agent.getMaxHP() - 1);
		assertFalse(agent == null);
		assertTrue(agent.getHP() == agent.getMaxHP() - 1);
		agent.onRoundEnd();
		// Regeneration should proc at end of enemy turn
		assertTrue(agent.getHP() == agent.getMaxHP());
	}

}

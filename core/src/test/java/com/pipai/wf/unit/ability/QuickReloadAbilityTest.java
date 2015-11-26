package com.pipai.wf.unit.ability;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;

public class QuickReloadAbilityTest {

	@Test
	public void testNoQuickReload() {
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPosition = mock(GridPosition.class);
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.weapons.add(new Pistol());
		Agent agent = new Agent(as, mockMap, mockConfig);
		BattleController battle = new BattleController(mockMap, mockConfig);
		try {
			battle.performAction(new ReloadAction(agent));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(agent.getAP() == 0);
	}

	@Test
	public void testQuickReload() {
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPosition = mock(GridPosition.class);
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new QuickReloadAbility());
		as.weapons.add(new Pistol());
		Agent agent = new Agent(as, mockMap, mockConfig);
		BattleController battle = new BattleController(mockMap, mockConfig);
		try {
			battle.performAction(new ReloadAction(agent));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(agent.getAP() == 1);
	}

}

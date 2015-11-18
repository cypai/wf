package com.pipai.wf.battle.weapon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.SwitchWeaponAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class WeaponTest {

	@Test
	public void testNoWeapon() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		Agent player = new Agent(playerState, mockMap, mockConfig);
		assertTrue(player.getCurrentWeapon() == null);
	}

	@Test
	public void testWeaponGrantedAbilities() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Bow(mockConfig));
		playerState.weapons.add(new InnateCasting(mockConfig));
		Agent player = new Agent(playerState, mockMap, mockConfig);
		assertTrue(player.getAbilities().hasAbility(QuickReloadAbility.class));
		try {
			new SwitchWeaponAction(player).perform(mockConfig);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertFalse(player.getAbilities().hasAbility(QuickReloadAbility.class));
	}

}

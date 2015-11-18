package com.pipai.wf.battle.action;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;

public class SwitchWeaponActionTest {

	@Test
	public void testSwitchWeaponLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Pistol(mockConfig));
		playerState.weapons.add(new InnateCasting(mockConfig));
		Agent player = new Agent(playerState, mockMap, mockConfig);
		BattleController battle = new BattleController(mockMap, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		assertTrue(player.getCurrentWeapon() instanceof Pistol);
		try {
			battle.performAction(new SwitchWeaponAction(player));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.SWITCH_WEAPON);
		assertTrue(ev.getPerformer() == player);
		assertTrue(ev.getChainEvents().size() == 0);
		assertTrue(player.getCurrentWeapon() instanceof SpellWeapon);
	}

}

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
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;

public class ReloadActionTest {

	@Test
	public void testReloadWeapon() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		Pistol pistol = new Pistol(mockConfig);
		pistol.expendAmmo(1);
		playerState.weapons.add(pistol);
		Agent player = new Agent(playerState, mockMap, mockConfig);
		assertTrue(player.getCurrentWeapon() == pistol);
		assertTrue(pistol.currentAmmo() < pistol.baseAmmoCapacity());
		BattleController battle = new BattleController(mockMap, mockConfig);
		try {
			battle.performAction(new ReloadAction(player));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(pistol.currentAmmo() == pistol.baseAmmoCapacity());
	}

	@Test
	public void testReloadLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap mockMap = mock(BattleMap.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Pistol(mockConfig));
		Agent player = new Agent(playerState, mockMap, mockConfig);
		BattleController battle = new BattleController(mockMap, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		try {
			battle.performAction(new ReloadAction(player));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.RELOAD);
		assertTrue(ev.getPerformer() == player);
		assertTrue(ev.getChainEvents().size() == 0);
	}

}

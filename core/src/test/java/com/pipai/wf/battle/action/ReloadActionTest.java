package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.MockGUIObserver;

public class ReloadActionTest {

	@Test
	public void testReloadWeapon() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		Pistol pistol = new Pistol();
		pistol.expendAmmo(1);
		playerState.getWeapons().add(pistol);
		BattleController controller = new BattleController(mockMap, mockConfig);
		Agent player = new Agent(playerState);
		Assert.assertEquals(pistol, player.getCurrentWeapon());
		Assert.assertTrue(pistol.currentAmmo() < pistol.baseAmmoCapacity());
		try {
			new ReloadAction(controller, player).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(pistol.baseAmmoCapacity(), pistol.currentAmmo());
	}

	@Test
	public void testReloadLog() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		playerState.getWeapons().add(new Pistol());
		BattleController controller = new BattleController(mockMap, mockConfig);
		Agent player = new Agent(playerState);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		try {
			new ReloadAction(controller, player).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent ev = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.RELOAD, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

}

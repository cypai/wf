package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfTestUtils;

public class ReloadActionTest {

	@Test
	public void testReloadWeapon() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, mockPos);
		Pistol pistol = new Pistol();
		pistol.expendAmmo(1);
		player.getInventory().setItem(pistol, 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
		Assert.assertTrue(pistol.getCurrentAmmo() < pistol.baseAmmoCapacity());
		try {
			new ReloadAction(controller, player).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(pistol.baseAmmoCapacity(), pistol.getCurrentAmmo());
	}

	@Test
	public void testReloadLog() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, mockPos);
		player.getInventory().setItem(new Pistol(), 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
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

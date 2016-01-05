package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.WfTestUtils;

public class QuickReloadAbilityTest {

	@Test
	public void testNoQuickReload() {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Agent agent = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		Pistol pistol = new Pistol();
		agent.getInventory().setItem(pistol, 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
		try {
			new ReloadAction(controller, agent, pistol).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(0, agent.getAP());
	}

	@Test
	public void testQuickReload() {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Agent agent = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		Pistol pistol = new Pistol();
		agent.getAbilities().add(new QuickReloadAbility());
		agent.getInventory().setItem(pistol, 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
		try {
			new ReloadAction(controller, agent, pistol).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(1, agent.getAP());
	}

}

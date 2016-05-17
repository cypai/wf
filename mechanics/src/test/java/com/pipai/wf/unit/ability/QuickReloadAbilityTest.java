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
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.util.GridPosition;

public class QuickReloadAbilityTest {

	@Test
	public void testNoQuickReload() throws IllegalActionException {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Agent agent = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		Pistol pistol = new Pistol();
		agent.getInventory().setItem(pistol, 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
		new ReloadAction(controller, agent, pistol).perform();
		Assert.assertEquals(0, agent.getAP());
	}

	@Test
	public void testQuickReload() throws IllegalActionException {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Agent agent = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		Pistol pistol = new Pistol();
		agent.getAbilities().add(new QuickReloadAbility());
		agent.getInventory().setItem(pistol, 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
		new ReloadAction(controller, agent, pistol).perform();
		Assert.assertEquals(1, agent.getAP());
	}

	@Test
	public void testWeaponGrantedQuickReload() throws IllegalActionException {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Agent agent = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		Bow bow = new Bow();
		agent.getInventory().setItem(bow, 1);
		BattleController controller = new BattleController(mockMap, mockConfig);
		new ReloadAction(controller, agent, bow).perform();
		Assert.assertEquals(1, agent.getAP());
	}

}

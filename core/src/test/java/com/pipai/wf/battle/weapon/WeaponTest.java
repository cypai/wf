package com.pipai.wf.battle.weapon;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class WeaponTest {

	@Test
	@Ignore
	// TODO: Refactor weapon granted abilities with new inventory system
	public void testWeaponGrantedAbilities() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		BattleController controller = new BattleController(mockMap, mockConfig);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, mockPos);
		player.getInventory().setItem(new Bow(), 1);
		player.getInventory().setItem(new InnateCasting(), 1);
		Assert.assertTrue(player.getAbilities().hasAbility(QuickReloadAbility.class));
		// try {
		// new SwitchWeaponAction(controller, player).perform();
		// } catch (IllegalActionException e) {
		// Assert.fail(e.getMessage());
		// }
		Assert.assertFalse(player.getAbilities().hasAbility(QuickReloadAbility.class));
	}

}

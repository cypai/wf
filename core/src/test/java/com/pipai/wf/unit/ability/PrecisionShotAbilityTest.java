package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.PrecisionShotAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.test.WfTestUtils;

public class PrecisionShotAbilityTest {

	@Test
	public void testPrecisionShotCooldown() throws IllegalActionException {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(0, 0);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = Mockito.mock(DamageCalculator.class);
		Mockito.when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(false, false, 0, 0));
		Mockito.when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		Agent mockTarget = Mockito.mock(Agent.class);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		player.getAbilities().add(new PrecisionShotAbility());
		Bow bow = new Bow();
		player.getInventory().setItem(bow, 1);
		map.addAgent(player);
		BattleController controller = new BattleController(map, mockConfig);
		PrecisionShotAbility ability = (PrecisionShotAbility) player.getAbilities()
				.getAbility(PrecisionShotAbility.class);
		new PrecisionShotAction(controller, player, mockTarget, bow).perform();
		Assert.assertTrue(ability.onCooldown());
		controller.endTurn();
		// Enemy turn ends immediately
		controller.endTurn();
		Assert.assertTrue(ability.onCooldown());
		try {
			new PrecisionShotAction(controller, player, mockTarget, bow).perform();
			Assert.fail("Expected IllegalActionException not thrown");
		} catch (IllegalActionException e) {
			Assert.assertEquals("Ability is on cooldown", e.getMessage());
		}
		controller.endTurn();
		// Enemy turn ends immediately
		controller.endTurn();
		Assert.assertFalse(ability.onCooldown());
		// Check that we can perform it again
		new PrecisionShotAction(controller, player, mockTarget, bow).perform();
		Assert.assertTrue(ability.onCooldown());
	}

}

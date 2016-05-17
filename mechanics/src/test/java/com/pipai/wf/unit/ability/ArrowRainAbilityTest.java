package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.util.GridPosition;

public class ArrowRainAbilityTest {

	@Test
	public void testFirstAction() throws IllegalActionException {
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
		AgentFactory factory = new AgentFactory();
		Agent player = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 3, 5, 65, 0);
		player.getAbilities().add(new ArrowRainAbility());
		Bow bow = new Bow();
		player.getInventory().setItem(bow, 1);
		map.addAgent(player);
		BattleController controller = new BattleController(map, mockConfig);
		new RangedWeaponAttackAction(controller, player, mockTarget, bow).perform();
		Assert.assertEquals(2, player.getAP());
	}

	@Test
	public void testNotFirstAction() throws IllegalActionException {
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
		AgentFactory factory = new AgentFactory();
		Agent player = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 3, 5, 65, 0);
		player.getAbilities().add(new ArrowRainAbility());
		Bow bow = new Bow();
		player.getInventory().setItem(bow, 1);
		map.addAgent(player);
		player.setAP(2);
		BattleController controller = new BattleController(map, mockConfig);
		new RangedWeaponAttackAction(controller, player, mockTarget, bow).perform();
		Assert.assertEquals(0, player.getAP());
	}

}

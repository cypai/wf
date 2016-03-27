package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.SuppressionAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.damage.TargetedActionCalculator;
import com.pipai.wf.battle.event.SuppressionEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.test.MockGUIObserver;

public class SuppressionTest {

	private static BattleMap generateMap(GridPosition playerPos, GridPosition enemyPos) throws BadStateStringException {
		BattleMap map = new BattleMap(2, 2);
		AgentFactory factory = new AgentFactory();
		Agent player = factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 1, 1, 1, 0);
		player.getAbilities().add(new SuppressionAbility());
		player.getInventory().setItem(new Bow(), 1);
		map.addAgent(player);
		Agent enemy = factory.battleAgentFromStats(Team.ENEMY, enemyPos, 1, 1, 1, 1, 1, 0);
		enemy.getInventory().setItem(new Bow(), 1);
		map.addAgent(enemy);
		return map;
	}

	@Test
	public void testSuppression() throws BadStateStringException, IllegalActionException {
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		BattleMap map = generateMap(playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.getTargetedActionCalculator()).thenReturn(new TargetedActionCalculator(mockConfig));
		BattleController controller = new BattleController(map, mockConfig);
		Bow playerBow = (Bow) player.getInventory().getItem(1);
		Bow enemyBow = (Bow) enemy.getInventory().getItem(1);
		int toHit = new RangedWeaponAttackAction(controller, enemy, player, enemyBow).getHitCalculation().total();
		new SuppressionAction(controller, player, enemy, playerBow).perform();
		Assert.assertEquals(-30, enemy.getStatusEffects().aimModifierList().total());
		int suppressedToHit = new RangedWeaponAttackAction(controller, enemy, player, enemyBow).getHitCalculation()
				.total();
		Assert.assertEquals(toHit - 30, suppressedToHit);
		Assert.assertEquals(0, player.getAP());
		Assert.assertEquals(playerBow.baseAmmoCapacity() - 2, playerBow.getCurrentAmmo());
	}

	@Test
	public void testNoAmmoSuppression() throws BadStateStringException {
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		BattleMap map = generateMap(playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Bow playerBow = (Bow) player.getInventory().getItem(1);
		playerBow.expendAmmo(playerBow.baseAmmoCapacity() + 1);
		BattleController controller = new BattleController(map, Mockito.mock(BattleConfiguration.class));
		try {
			new SuppressionAction(controller, player, enemy, playerBow).perform();
			Assert.fail("Expected not enough ammo exception not thrown");
		} catch (IllegalActionException e) {
			Assert.assertTrue(e.getMessage().endsWith("Not enough ammunition"));
		}
	}

	@Test
	public void testSuppressionLog() throws BadStateStringException, IllegalActionException {
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		BattleMap map = generateMap(playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		BattleController controller = new BattleController(map, Mockito.mock(BattleConfiguration.class));
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Bow bow = (Bow) player.getInventory().getItem(1);
		new SuppressionAction(controller, player, enemy, bow).perform();
		SuppressionEvent ev = (SuppressionEvent) observer.getEvent();
		Assert.assertEquals(player, ev.performer);
		Assert.assertEquals(enemy, ev.target);
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

}

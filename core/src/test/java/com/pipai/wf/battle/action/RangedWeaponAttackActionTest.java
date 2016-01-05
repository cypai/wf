package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.util.UtilFunctions;

// TODO: Make these tests better
public class RangedWeaponAttackActionTest {

	@Test
	public void testAttackLog() throws IllegalActionException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = Mockito.mock(DamageCalculator.class);
		Mockito.when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(true, false, 1, 0));
		Mockito.when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 1);
		AgentFactory factory = new AgentFactory();
		Agent player = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 1000, 0);
		Pistol pistol = new Pistol();
		player.getInventory().setItem(pistol, 1);
		map.addAgent(player);
		Agent enemy = WfTestUtils.createGenericAgent(Team.ENEMY, enemyPos);
		map.addAgent(enemy);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		new RangedWeaponAttackAction(controller, player, enemy, pistol).perform();
		BattleEvent ev = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.RANGED_WEAPON_ATTACK, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertEquals(enemy, ev.getTarget());
		Assert.assertEquals(0, ev.getChainEvents().size());
		// Player has 1000 aim, cannot miss
		Assert.assertTrue(ev.getDamageResult().isHit());
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - ev.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
		Assert.assertEquals(player.getMaxHP(), player.getHP());
	}

}

package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.util.UtilFunctions;

public class RangedWeaponAttackActionTest {

	// TODO: Make these tests better

	@Test
	public void testAttackLog() {
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
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 1000, 0);
		playerState.getWeapons().add(new Pistol());
		map.addAgent(playerState);
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertFalse(player == null || enemy == null);
		TargetedWithAccuracyActionOWCapable atk = (TargetedWithAccuracyActionOWCapable) ((Pistol) player.getCurrentWeapon()).getAction(controller, player, enemy);
		try {
			atk.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		Assert.assertEquals(BattleEvent.Type.RANGED_WEAPON_ATTACK, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertEquals(enemy, ev.getTarget());
		Assert.assertEquals(0, ev.getChainEvents().size());
		// Player has 1000 aim, cannot miss
		Assert.assertTrue(ev.getDamageResult().hit);
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - ev.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
		Assert.assertEquals(player.getMaxHP(), player.getHP());
	}

}

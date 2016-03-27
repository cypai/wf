package com.pipai.wf.battle.action;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.MoveEvent;
import com.pipai.wf.battle.event.OverwatchActivationEvent;
import com.pipai.wf.battle.event.OverwatchEvent;
import com.pipai.wf.battle.event.RangedWeaponAttackEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfTestUtils;
import com.pipai.wf.util.UtilFunctions;

public class OverwatchActionTest extends GdxMockedTest {

	@Test
	public void testNoAmmoOverwatch() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		Pistol pistol = new Pistol();
		pistol.setCurrentAmmo(0);
		player.getInventory().setItem(pistol, 1);
		map.addAgent(player);
		map.addAgent(WfTestUtils.createGenericAgent(Team.ENEMY, enemyPos));
		OverwatchAction ow = new OverwatchAction(controller, player, pistol,
				new RangedWeaponAttackAction(controller, player, pistol));
		try {
			ow.perform();
			Assert.fail("Expected exception not thrown");
		} catch (IllegalActionException e) {
			// Expected, don't do anything
		}
	}

	@Test
	public void testOverwatchLog() throws IllegalActionException {
		BattleConfiguration mockConfig = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, playerPos);
		Pistol pistol = new Pistol();
		player.getInventory().setItem(pistol, 1);
		map.addAgent(player);
		Agent enemy = WfTestUtils.createGenericAgent(Team.ENEMY, enemyPos);
		map.addAgent(enemy);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		OverwatchAction ow = new OverwatchAction(controller, player, pistol,
				new RangedWeaponAttackAction(controller, player, pistol));
		ow.perform();
		OverwatchEvent ev = (OverwatchEvent) observer.getEvent();
		Assert.assertEquals(player, ev.performer);
		Assert.assertEquals("Overwatch: " + pistol.getName(), ev.overwatchName);
		Assert.assertEquals(0, ev.getChainEvents().size());
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, enemy, path, 1);
		move.perform();
		MoveEvent moveEv = (MoveEvent) observer.getEvent();
		Assert.assertEquals(enemy, moveEv.performer);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		Assert.assertEquals(1, chain.size());
		OverwatchActivationEvent owEv = (OverwatchActivationEvent) chain.peekFirst();
		Assert.assertEquals(player, owEv.performer);
		Assert.assertEquals(enemy, owEv.target);
		Assert.assertEquals(1, owEv.getChainEvents().size());
		RangedWeaponAttackEvent attackEvent = (RangedWeaponAttackEvent) owEv.getChainEvents().get(0);
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(),
				enemy.getMaxHP() - attackEvent.damageResult.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
		Assert.assertEquals(player.getMaxHP(), player.getHP());
	}

	@Test
	public void testDualOverwatchLog() throws IllegalActionException {
		BattleConfiguration mockConfig = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition player1Pos = new GridPosition(1, 1);
		GridPosition player2Pos = new GridPosition(0, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		Agent player1 = WfTestUtils.createGenericAgent(Team.PLAYER, player1Pos);
		Pistol pistol1 = new Pistol();
		player1.getInventory().setItem(pistol1, 1);
		map.addAgent(player1);
		map.addAgent(WfTestUtils.createGenericAgent(Team.ENEMY, enemyPos));
		Agent player2 = WfTestUtils.createGenericAgent(Team.PLAYER, player2Pos);
		Pistol pistol2 = new Pistol();
		player2.getInventory().setItem(pistol2, 1);
		map.addAgent(player2);
		// Enemy needs more HP to more accurately test damage
		AgentFactory factory = new AgentFactory();
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 10, 5, 2, 5, 65, 0));
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent enemy = map.getAgentAtPos(enemyPos);
		new OverwatchAction(controller, player1, pistol1, new RangedWeaponAttackAction(controller, player1, pistol1))
				.perform();
		new OverwatchAction(controller, player2, pistol2, new RangedWeaponAttackAction(controller, player2, pistol2))
				.perform();
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, enemy, path, 1);
		move.perform();
		MoveEvent moveEv = (MoveEvent) observer.getEvent();
		Assert.assertEquals(enemy, moveEv.performer);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		Assert.assertEquals(2, chain.size());
		OverwatchActivationEvent owEv1 = (OverwatchActivationEvent) chain.pollFirst();
		Assert.assertEquals(player1, owEv1.performer);
		Assert.assertEquals(enemy, owEv1.target);
		Assert.assertEquals(1, owEv1.getChainEvents().size());
		RangedWeaponAttackEvent attackEvent1 = (RangedWeaponAttackEvent) owEv1.getChainEvents().get(0);
		OverwatchActivationEvent owEv2 = (OverwatchActivationEvent) chain.pollFirst();
		Assert.assertEquals(player2, owEv2.performer);
		Assert.assertEquals(enemy, owEv2.target);
		Assert.assertEquals(1, owEv2.getChainEvents().size());
		RangedWeaponAttackEvent attackEvent2 = (RangedWeaponAttackEvent) owEv2.getChainEvents().get(0);
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0,
				enemy.getMaxHP(),
				enemy.getMaxHP() - attackEvent1.damageResult.getDamage() - attackEvent2.damageResult.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
	}

}

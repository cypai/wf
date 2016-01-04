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
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.util.UtilFunctions;

public class OverwatchActionTest extends GdxMockedTest {

	@Test
	public void testNoWeaponOverwatch() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		map.addAgent(playerState);
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(controller, player);
		try {
			ow.perform();
			Assert.fail("Expected exception not thrown");
		} catch (IllegalActionException e) {
			// Expected, don't do anything
		}
	}

	@Test
	public void testNoAmmoOverwatch() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.getWeapons().add(new Pistol());
		playerState.getWeapons().get(0).expendAmmo(new Pistol().baseAmmoCapacity());
		map.addAgent(playerState);
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(controller, player);
		try {
			ow.perform();
			Assert.fail("Expected exception not thrown");
		} catch (IllegalActionException e) {
			// Expected, don't do anything
		}
	}

	@Test
	public void testOverwatchLog() {
		BattleConfiguration mockConfig = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.getWeapons().add(new Pistol());
		map.addAgent(playerState);
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(controller, player);
		try {
			ow.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent ev = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.OVERWATCH, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertTrue(ev.getPreparedOWName().equals("Attack"));
		Assert.assertEquals(0, ev.getChainEvents().size());
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, enemy, path, 1);
		try {
			move.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent moveEv = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.MOVE, moveEv.getType());
		Assert.assertEquals(enemy, moveEv.getPerformer());
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		Assert.assertEquals(1, chain.size());
		BattleEvent owEv = chain.peekFirst();
		Assert.assertEquals(BattleEvent.Type.OVERWATCH_ACTIVATION, owEv.getType());
		Assert.assertEquals(player, owEv.getPerformer());
		Assert.assertEquals(enemy, owEv.getTarget());
		Assert.assertTrue(owEv.getActivatedOverwatchAction() instanceof RangedWeaponAttackAction);
		Assert.assertEquals(0, owEv.getChainEvents().size());
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - owEv.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
		Assert.assertEquals(player.getMaxHP(), player.getHP());
	}

	@Test
	public void testDualOverwatchLog() {
		BattleConfiguration mockConfig = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition player1Pos = new GridPosition(1, 1);
		GridPosition player2Pos = new GridPosition(0, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState player1State = factory.battleAgentFromStats(Team.PLAYER, player1Pos, 3, 5, 2, 5, 65, 0);
		player1State.getWeapons().add(new Pistol());
		map.addAgent(player1State);
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		AgentState player2State = factory.battleAgentFromStats(Team.PLAYER, player2Pos, 3, 5, 2, 5, 65, 0);
		player2State.getWeapons().add(new Pistol());
		map.addAgent(player2State);
		// Enemy needs more HP to more accurately test damage
		map.addAgent(factory.battleAgentFromStats(Team.ENEMY, enemyPos, 10, 5, 2, 5, 65, 0));
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent player1 = map.getAgentAtPos(player1Pos);
		Agent player2 = map.getAgentAtPos(player2Pos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		try {
			new OverwatchAction(controller, player1).perform();
			new OverwatchAction(controller, player2).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, enemy, path, 1);
		try {
			move.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent moveEv = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.MOVE, moveEv.getType());
		Assert.assertEquals(enemy, moveEv.getPerformer());
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		Assert.assertEquals(2, chain.size());
		BattleEvent owEv1 = chain.pollFirst();
		Assert.assertEquals(BattleEvent.Type.OVERWATCH_ACTIVATION, owEv1.getType());
		Assert.assertEquals(enemy, owEv1.getTarget());
		Assert.assertTrue(owEv1.getActivatedOverwatchAction() instanceof RangedWeaponAttackAction);
		Assert.assertEquals(0, owEv1.getChainEvents().size());
		BattleEvent owEv2 = chain.pollFirst();
		Assert.assertEquals(BattleEvent.Type.OVERWATCH_ACTIVATION, owEv2.getType());
		Assert.assertEquals(enemy, owEv2.getTarget());
		Assert.assertTrue(owEv2.getActivatedOverwatchAction() instanceof RangedWeaponAttackAction);
		Assert.assertEquals(0, owEv2.getChainEvents().size());
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - owEv1.getDamage() - owEv2.getDamage());
		Assert.assertEquals(expectedHP, enemy.getHP());
	}

}

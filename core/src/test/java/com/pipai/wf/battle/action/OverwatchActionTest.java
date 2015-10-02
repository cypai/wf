package com.pipai.wf.battle.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import java.util.LinkedList;

import org.junit.Test;

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
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.util.UtilFunctions;

public class OverwatchActionTest extends GdxMockedTest {

	@Test
	public void testNoWeaponOverwatch() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		map.addAgent(playerState);
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, mockConfig);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(player);
		try {
			battle.performAction(ow);
			fail("Expected exception not thrown");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testNoAmmoOverwatch() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Pistol(mockConfig));
		playerState.weapons.get(0).expendAmmo(new Pistol(mockConfig).baseAmmoCapacity());
		map.addAgent(playerState);
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, mockConfig);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(player);
		try {
			battle.performAction(ow);
			fail("Expected exception not thrown");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testOverwatchLog() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(config);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Pistol(config));
		map.addAgent(playerState);
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, config);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(player);
		try {
			battle.performAction(ow);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.OVERWATCH);
		assertTrue(ev.getPerformer() == player);
		assertTrue(ev.getPreparedOWName().equals("Attack"));
		assertTrue(ev.getChainEvents().size() == 0);
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(enemy, path, 1);
		try {
			battle.performAction(move);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent moveEv = observer.ev;
		assertTrue(moveEv.getType() == BattleEvent.Type.MOVE);
		assertTrue(moveEv.getPerformer() == enemy);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		assertTrue(chain.size() == 1);
		BattleEvent owEv = chain.peekFirst();
		assertTrue(owEv.getType() == BattleEvent.Type.OVERWATCH_ACTIVATION);
		assertTrue(owEv.getPerformer() == player);
		assertTrue(owEv.getTarget() == enemy);
		assertTrue(owEv.getActivatedOWAction() instanceof RangedWeaponAttackAction);
		assertTrue(owEv.getChainEvents().size() == 0);
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - owEv.getDamage());
		assertTrue(enemy.getHP() == expectedHP);
		assertTrue(player.getHP() == player.getMaxHP());
	}

	@Test
	public void testDualOverwatchLog() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		GridPosition player1Pos = new GridPosition(1, 1);
		GridPosition player2Pos = new GridPosition(0, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(config);
		AgentState player1State = factory.newBattleAgentState(Team.PLAYER, player1Pos, 3, 5, 2, 5, 65, 0);
		player1State.weapons.add(new Pistol(config));
		map.addAgent(player1State);
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		AgentState player2State = factory.newBattleAgentState(Team.PLAYER, player2Pos, 3, 5, 2, 5, 65, 0);
		player2State.weapons.add(new Pistol(config));
		map.addAgent(player2State);
		// Enemy needs more HP to more accurately test damage
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 10, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, config);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent player1 = map.getAgentAtPos(player1Pos);
		Agent player2 = map.getAgentAtPos(player2Pos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		try {
			battle.performAction(new OverwatchAction(player1));
			battle.performAction(new OverwatchAction(player2));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(enemy, path, 1);
		try {
			battle.performAction(move);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent moveEv = observer.ev;
		assertTrue(moveEv.getType() == BattleEvent.Type.MOVE);
		assertTrue(moveEv.getPerformer() == enemy);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		assertTrue(chain.size() == 2);
		BattleEvent owEv1 = chain.pollFirst();
		assertTrue(owEv1.getType() == BattleEvent.Type.OVERWATCH_ACTIVATION);
		assertTrue(owEv1.getTarget() == enemy);
		assertTrue(owEv1.getActivatedOWAction() instanceof RangedWeaponAttackAction);
		assertTrue(owEv1.getChainEvents().size() == 0);
		BattleEvent owEv2 = chain.pollFirst();
		assertTrue(owEv2.getType() == BattleEvent.Type.OVERWATCH_ACTIVATION);
		assertTrue(owEv2.getTarget() == enemy);
		assertTrue(owEv2.getActivatedOWAction() instanceof RangedWeaponAttackAction);
		assertTrue(owEv2.getChainEvents().size() == 0);
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - owEv1.getDamage() - owEv2.getDamage());
		assertTrue(enemy.getHP() == expectedHP);
	}

}

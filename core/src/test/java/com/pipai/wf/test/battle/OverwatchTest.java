package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfConfiguredTest;
import com.pipai.wf.util.UtilFunctions;

public class OverwatchTest extends WfConfiguredTest {

	@Test
	public void testNoWeaponOverwatch() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		map.addAgent(playerState);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(player);
		try {
			battle.performAction(ow);
			fail("Expected exception not thrown");
		} catch (IllegalActionException e) {}
	}

	@Test
	public void testNoAmmoOverwatch() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Pistol());
		playerState.weapons.get(0).expendAmmo(new Pistol().baseAmmoCapacity());
		map.addAgent(playerState);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(player);
		try {
			battle.performAction(ow);
			fail("Expected exception not thrown");
		} catch (IllegalActionException e) {}
	}

	@Test
	public void testOverwatchLog() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new Pistol());
		map.addAgent(playerState);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
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
		assertTrue(ev.getPreparedOWName() == "Attack");
		assertTrue(ev.getChainEvents().size() == 0);
		//Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<GridPosition>();
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

}

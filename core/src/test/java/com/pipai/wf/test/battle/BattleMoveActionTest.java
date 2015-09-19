package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.exception.IllegalActionException;

public class BattleMoveActionTest {

	@Test
	public void testAgentMoveAction() {
		/*
		 * Map looks like:
		 * 0 1 1 0
		 * 0 1 0 0
		 * 0 A 0 0
		 */
		String rawMapString = "3 4\n"
				+ "s 1 1\n"
				+ "s 1 2\n"
				+ "s 2 2";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 0);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(2, 0));
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			move.perform();
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		assertFalse(map.getAgentAtPos(new GridPosition(2, 0)) == null);
		// Check to see if they are the same object
		assertTrue(agent == map.getAgentAtPos(new GridPosition(2, 0)));
		LinkedList<GridPosition> path2 = new LinkedList<>();
		path2.add(new GridPosition(1, 0));
		path2.add(new GridPosition(0, 0));
		path2.add(new GridPosition(0, 1));
		MoveAction move2 = new MoveAction(agent, path2, 1);
		try {
			move2.perform();
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		assertTrue(map.getAgentAtPos(new GridPosition(2, 0)) == null);
		assertFalse(map.getAgentAtPos(new GridPosition(0, 1)) == null);
	}

	@Test
	public void testAgentIllegalDestinationMoveAction() {
		/*
		 * Map looks like:
		 * 0 1 1 0
		 * 0 1 0 0
		 * 0 A 0 0
		 */
		String rawMapString = "3 4\n"
				+ "s 1 1\n"
				+ "s 1 2\n"
				+ "s 2 2";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 0);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(1, 1));
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			move.perform();
			fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			assertTrue(map.getAgentAtPos(new GridPosition(1, 1)) == null);
			assertFalse(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		}
	}

	@Test
	public void testAgentIllegalPathMoveAction() {
		/*
		 * Map looks like:
		 * 0 1 1 0
		 * 0 1 0 0
		 * 0 A 0 0
		 */
		String rawMapString = "3 4\n"
				+ "s 1 1\n"
				+ "s 1 2\n"
				+ "s 2 2";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		GridPosition playerPos = new GridPosition(1, 0);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(1, 1));
		path.add(new GridPosition(0, 1));
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			move.perform();
			fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			assertTrue(map.getAgentAtPos(new GridPosition(1, 1)) == null);
			assertFalse(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		}
	}

}

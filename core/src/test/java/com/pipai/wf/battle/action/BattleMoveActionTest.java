package com.pipai.wf.battle.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;

public class BattleMoveActionTest {

	private static BattleMap generateMap(String mapString, GridPosition playerPos) throws BadStateStringException {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		when(mockConfig.sightRange()).thenReturn(17);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		BattleMap map = new BattleMap(new MapString(mapString));
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 2, 1, 1, 0)));
		return map;
	}

	@Test
	public void testAgentMoveAction() throws BadStateStringException {
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
		GridPosition playerPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(2, 0));
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			move.perform(mock(BattleConfiguration.class));
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
			move2.perform(mock(BattleConfiguration.class));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		assertTrue(map.getAgentAtPos(new GridPosition(2, 0)) == null);
		assertFalse(map.getAgentAtPos(new GridPosition(0, 1)) == null);
	}

	@Test
	public void testAgentIllegalDestinationMoveAction() throws BadStateStringException {
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
		GridPosition playerPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(1, 1));
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			move.perform(mock(BattleConfiguration.class));
			fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			assertTrue(map.getAgentAtPos(new GridPosition(1, 1)) == null);
			assertFalse(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		}
	}

	@Test
	public void testAgentIllegalPathMoveAction() throws BadStateStringException {
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
		GridPosition playerPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos);
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(1, 1));
		path.add(new GridPosition(0, 1));
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			move.perform(mock(BattleConfiguration.class));
			fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			assertTrue(map.getAgentAtPos(new GridPosition(1, 1)) == null);
			assertFalse(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		}
	}

	@Test
	public void testMoveLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		map.addAgent(factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 0);
		path.add(agent.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			battle.performAction(move);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.MOVE);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getPath().peekLast().equals(dest));
		assertTrue(ev.getChainEvents().size() == 0);
	}

	@Test
	public void testIllegalMoveLog() throws BadStateStringException {
		String rawMapString = "3 4\n"
				+ "s 2 1";
		GridPosition playerPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos);
		BattleController battle = new BattleController(map, mock(BattleConfiguration.class));
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(1, 1);
		path.add(agent.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(agent, path, 1);
		try {
			battle.performAction(move);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.MOVE);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getPath().peekLast().equals(dest));
		assertTrue(ev.getChainEvents().size() == 0);
		LinkedList<GridPosition> illegalPath = new LinkedList<>();
		illegalPath.add(agent.getPosition());
		illegalPath.add(new GridPosition(2, 1));
		MoveAction badmove = new MoveAction(agent, illegalPath, 1);
		try {
			battle.performAction(badmove);
			fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			BattleEvent ev2 = observer.ev;
			assertTrue(ev2 == ev);	// Check that notifyBattleEvent was not called
		}
	}

}

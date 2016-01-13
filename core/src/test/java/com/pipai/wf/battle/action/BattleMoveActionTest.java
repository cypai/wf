package com.pipai.wf.battle.action;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.MoveEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfTestUtils;

public class BattleMoveActionTest {

	private static BattleMap generateMap(String mapString, GridPosition playerPos) throws BadStateStringException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		Mockito.when(mockConfig.sightRange()).thenReturn(17);
		AgentFactory factory = new AgentFactory();
		BattleMap map = new BattleMap(new MapString(mapString));
		map.addAgent(factory.battleAgentFromStats(Team.PLAYER, playerPos, 1, 1, 2, 1, 1, 0));
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
		BattleController controller = new BattleController(map, Mockito.mock(BattleConfiguration.class));
		Agent agent = map.getAgentAtPos(playerPos);
		Assert.assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(2, 0));
		MoveAction move = new MoveAction(controller, agent, path, 1);
		try {
			move.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(null, map.getAgentAtPos(new GridPosition(1, 0)));
		Assert.assertFalse(map.getAgentAtPos(new GridPosition(2, 0)) == null);
		// Check to see if they are the same object
		Assert.assertEquals(map.getAgentAtPos(new GridPosition(2, 0)), agent);
		LinkedList<GridPosition> path2 = new LinkedList<>();
		path2.add(new GridPosition(1, 0));
		path2.add(new GridPosition(0, 0));
		path2.add(new GridPosition(0, 1));
		MoveAction move2 = new MoveAction(controller, agent, path2, 1);
		try {
			move2.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(null, map.getAgentAtPos(new GridPosition(1, 0)));
		Assert.assertEquals(null, map.getAgentAtPos(new GridPosition(2, 0)));
		Assert.assertFalse(map.getAgentAtPos(new GridPosition(0, 1)) == null);
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
		BattleController controller = new BattleController(map, Mockito.mock(BattleConfiguration.class));
		Agent agent = map.getAgentAtPos(playerPos);
		Assert.assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(1, 1));
		MoveAction move = new MoveAction(controller, agent, path, 1);
		try {
			move.perform();
			Assert.fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			Assert.assertEquals(null, map.getAgentAtPos(new GridPosition(1, 1)));
			Assert.assertFalse(map.getAgentAtPos(new GridPosition(1, 0)) == null);
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
		BattleController controller = new BattleController(map, Mockito.mock(BattleConfiguration.class));
		Agent agent = map.getAgentAtPos(playerPos);
		Assert.assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(agent.getPosition());
		path.add(new GridPosition(1, 1));
		path.add(new GridPosition(0, 1));
		MoveAction move = new MoveAction(controller, agent, path, 1);
		try {
			move.perform();
			Assert.fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			Assert.assertEquals(null, map.getAgentAtPos(new GridPosition(1, 1)));
			Assert.assertFalse(map.getAgentAtPos(new GridPosition(1, 0)) == null);
		}
	}

	@Test
	public void testMoveLog() throws IllegalActionException {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(1, 0);
		map.addAgent(WfTestUtils.createGenericAgent(Team.PLAYER, playerPos));
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		Assert.assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 0);
		path.add(agent.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, agent, path, 1);
		move.perform();
		MoveEvent ev = (MoveEvent) observer.getEvent();
		Assert.assertEquals(agent, ev.performer);
		Assert.assertTrue(ev.path.peekLast().equals(dest));
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

	@Test
	public void testIllegalMoveLog() throws BadStateStringException, IllegalActionException {
		String rawMapString = "3 4\n"
				+ "s 2 1";
		GridPosition playerPos = new GridPosition(1, 0);
		BattleMap map = generateMap(rawMapString, playerPos);
		BattleController controller = new BattleController(map, Mockito.mock(BattleConfiguration.class));
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		Assert.assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(1, 1);
		path.add(agent.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(controller, agent, path, 1);
		move.perform();
		MoveEvent ev = (MoveEvent) observer.getEvent();
		Assert.assertEquals(agent, ev.performer);
		Assert.assertTrue(ev.path.peekLast().equals(dest));
		Assert.assertEquals(0, ev.getChainEvents().size());
		LinkedList<GridPosition> illegalPath = new LinkedList<>();
		illegalPath.add(agent.getPosition());
		illegalPath.add(new GridPosition(2, 1));
		MoveAction badmove = new MoveAction(controller, agent, illegalPath, 1);
		try {
			badmove.perform();
			Assert.fail("Expected IllegalMoveException was not thrown");
		} catch (IllegalActionException e) {
			BattleEvent ev2 = observer.getEvent();
			// Check that notifyBattleEvent was not called
			Assert.assertEquals(ev, ev2);
		}
	}

}

package com.pipai.wf.test;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.RangeAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.Agent.State;
import com.pipai.wf.battle.attack.SimpleRangedAttack;
import com.pipai.wf.battle.exception.BadStateStringException;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.MapString;
import com.pipai.wf.battle.map.GridPosition;

public class BattleLogTest {
	
	@Test
	public void testMoveLog() {
		String rawMapString = "3 4\n"
				+ "a 1 0";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		BattleController battle = new BattleController(map);
		Agent agent = map.getAgentAtPos(new GridPosition(1, 0));
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<GridPosition>();
		GridPosition dest = new GridPosition(2, 0);
		path.add(agent.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(agent, path);
		BattleEvent ev = battle.performAction(move);
		assertTrue(ev.getType() == BattleEvent.Type.MOVE);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getPath().peekLast().equals(dest));
		assertTrue(ev.getChainEvents().size() == 0);
	}
	
	@Test
	public void testIllegalMoveLog() {
		String rawMapString = "3 4\n"
				+ "s 2 1\n"
				+ "a 1 0";
		BattleMap map = null;
		try {
			map = new BattleMap(new MapString(rawMapString));
		} catch (BadStateStringException e) {
			fail(e.getMessage());
		}
		BattleController battle = new BattleController(map);
		Agent agent = map.getAgentAtPos(new GridPosition(1, 0));
		assertFalse(agent == null);
		LinkedList<GridPosition> path = new LinkedList<GridPosition>();
		GridPosition dest = new GridPosition(1, 1);
		path.add(agent.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(agent, path);
		BattleEvent ev = battle.performAction(move);
		assertTrue(ev.getType() == BattleEvent.Type.MOVE);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getPath().peekLast().equals(dest));
		assertTrue(ev.getChainEvents().size() == 0);
		LinkedList<GridPosition> illegalPath = new LinkedList<GridPosition>();
		illegalPath.add(agent.getPosition());
		illegalPath.add(new GridPosition(2, 1));
		MoveAction badmove = new MoveAction(agent, illegalPath);
		BattleEvent ev2 = battle.performAction(badmove);
		assertTrue(ev2 == null);
	}
	
	@Test
	public void testAttackLog() {
		BattleMap map = null;
		map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
        map.addAgent(new AgentState(playerPos, Agent.Team.PLAYER, 5));
        map.addAgent(new AgentState(enemyPos, Agent.Team.ENEMY, 5));
		BattleController battle = new BattleController(map);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		RangeAttackAction atk = new RangeAttackAction(player, enemy, new SimpleRangedAttack());
		BattleEvent ev = battle.performAction(atk);
		assertTrue(ev.getType() == BattleEvent.Type.ATTACK);
		assertTrue(ev.getPerformer() == player);
		assertTrue(ev.getTarget() == enemy);
		assertTrue(ev.getChainEvents().size() == 0);
	}
	
	@Test
	public void testOverwatchLog() {
		BattleMap map = null;
		map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
        map.addAgent(new AgentState(playerPos, Agent.Team.PLAYER, 5));
        map.addAgent(new AgentState(enemyPos, Agent.Team.ENEMY, 5));
		BattleController battle = new BattleController(map);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		OverwatchAction ow = new OverwatchAction(enemy, new SimpleRangedAttack());
		BattleEvent ev = battle.performAction(ow);
		assertTrue(ev.getType() == BattleEvent.Type.OVERWATCH);
		assertTrue(ev.getPerformer() == enemy);
		assertTrue(ev.getAttack() instanceof SimpleRangedAttack);
		assertTrue(ev.getChainEvents().size() == 0);
		//Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<GridPosition>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(player.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(player, path);
		BattleEvent moveEv = battle.performAction(move);
		assertTrue(moveEv.getType() == BattleEvent.Type.MOVE);
		assertTrue(moveEv.getPerformer() == player);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		assertTrue(chain.size() == 1);
		BattleEvent owEv = chain.peekFirst();
		assertTrue(owEv.getType() == BattleEvent.Type.OVERWATCH_ACTIVATION);
		assertTrue(owEv.getPerformer() == enemy);
		assertTrue(owEv.getTarget() == player);
		assertTrue(owEv.getAttack() instanceof SimpleRangedAttack);
		assertTrue(owEv.getChainEvents().size() == 0);
	}

}

package com.pipai.wf.battle.log;

import static org.junit.Assert.assertTrue;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.Team;

public class BattleLogTest {

	@Test
	public void chainPopTest() {
		BattleLog log = new BattleLog();
		BattleEvent primary = BattleEvent.startTurnEvent(Team.PLAYER);
		BattleEvent chain = BattleEvent.startTurnEvent(Team.PLAYER);
		primary.addChainEvent(chain);
		log.logEvent(primary);
		BattleEvent popped = log.getLastEvent();
		assertTrue(popped.getNumChainEvents() == 1);
		LinkedList<BattleEvent> l = popped.getChainEvents();
		l.pop();
		assertTrue(l.size() == 0);
		assertTrue(popped.getNumChainEvents() == 1);	// Make sure getChainEvent() returns a copy
		assertTrue(log.getLastEvent().getNumChainEvents() == 1);	// Make sure log does not change
	}

}

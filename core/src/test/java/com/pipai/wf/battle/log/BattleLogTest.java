package com.pipai.wf.battle.log;

import java.util.LinkedList;

import org.junit.Assert;
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
		Assert.assertEquals(1, popped.getNumChainEvents());
		LinkedList<BattleEvent> l = popped.getChainEvents();
		l.pop();
		Assert.assertEquals(0, l.size());
		Assert.assertEquals(1, popped.getNumChainEvents());	// Make sure getChainEvent() returns a copy
		Assert.assertEquals(1, log.getLastEvent().getNumChainEvents());	// Make sure log does not change
	}

}

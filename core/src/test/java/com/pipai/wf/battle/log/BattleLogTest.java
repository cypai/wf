package com.pipai.wf.battle.log;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.BattleLog;
import com.pipai.wf.battle.event.StartTurnEvent;

public class BattleLogTest {

	@Test
	public void chainPopTest() {
		BattleLog log = new BattleLog();
		BattleEvent primary = new StartTurnEvent(Team.PLAYER);
		BattleEvent chain = new StartTurnEvent(Team.PLAYER);
		primary.addChainEvent(chain);
		log.logEvent(primary);
		BattleEvent popped = log.getLastEvent();
		Assert.assertEquals(1, popped.getChainEvents().size());
		LinkedList<BattleEvent> l = popped.getChainEvents();
		l.pop();
		Assert.assertEquals(0, l.size());
		// Make sure getChainEvent() returns a copy
		Assert.assertEquals(1, popped.getChainEvents().size());
		// Make sure log does not change
		Assert.assertEquals(1, log.getLastEvent().getChainEvents().size());
	}

}

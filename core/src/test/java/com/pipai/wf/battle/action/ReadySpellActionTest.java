package com.pipai.wf.battle.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;

public class ReadySpellActionTest {

	@Test
	public void testReadyWithoutActualizationAbility() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new FireballAbility());
		as.weapons.add(new InnateCasting());
		Agent player = new Agent(as, mock(BattleMap.class), mockConfig);
		ReadySpellAction ready = new ReadySpellAction(player, new FireballSpell());
		try {
			ready.perform(mockConfig);
			fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyWithoutFireballAbility() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new FireActualizationAbility(1));
		as.weapons.add(new InnateCasting());
		Agent player = new Agent(as, mock(BattleMap.class), mockConfig);
		ReadySpellAction ready = new ReadySpellAction(player, new FireballSpell());
		try {
			ready.perform(mockConfig);
			fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyWithoutSpellWeapon() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		GridPosition mockPos = mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new FireballAbility());
		as.abilities.add(new FireActualizationAbility(1));
		Agent player = new Agent(as, mock(BattleMap.class), mockConfig);
		ReadySpellAction ready = new ReadySpellAction(player, new FireballSpell());
		try {
			ready.perform(mockConfig);
			fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyFireballLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4, mock(BattleConfiguration.class));
		GridPosition playerPos = new GridPosition(1, 0);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.weapons.add(new InnateCasting());
		as.abilities.add(new FireActualizationAbility(1));
		as.abilities.add(new FireballAbility());
		map.addAgent(as);
		BattleController battle = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		try {
			battle.performAction(new ReadySpellAction(agent, new FireballSpell()));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.READY);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getSpell() instanceof FireballSpell);
		assertFalse(ev.getQuickened());
		assertTrue(agent.getAP() == 1);
		assertTrue(ev.getChainEvents().size() == 0);
	}

	@Test
	public void testQuickenedFireballLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4, mock(BattleConfiguration.class));
		GridPosition playerPos = new GridPosition(1, 0);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState as = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.weapons.add(new InnateCasting());
		as.abilities.add(new FireActualizationAbility(2));
		as.abilities.add(new FireballAbility());
		map.addAgent(as);
		BattleController battle = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		try {
			battle.performAction(new ReadySpellAction(agent, new FireballSpell()));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.READY);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getSpell() instanceof FireballSpell);
		assertTrue(ev.getQuickened());
		assertTrue(agent.getAP() == 2);
		assertTrue(ev.getChainEvents().size() == 0);
	}

}

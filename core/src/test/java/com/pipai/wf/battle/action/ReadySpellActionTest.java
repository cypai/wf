package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

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
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		BattleController controller = new BattleController(Mockito.mock(BattleMap.class), mockConfig);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		as.getAbilities().add(new FireballAbility());
		as.getWeapons().add(new InnateCasting());
		Agent player = new Agent(as);
		ReadySpellAction ready = new ReadySpellAction(controller, player, new FireballSpell());
		try {
			ready.perform();
			Assert.fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyWithoutFireballAbility() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		BattleController controller = new BattleController(Mockito.mock(BattleMap.class), mockConfig);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		as.getAbilities().add(new FireActualizationAbility(1));
		as.getWeapons().add(new InnateCasting());
		Agent player = new Agent(as);
		ReadySpellAction ready = new ReadySpellAction(controller, player, new FireballSpell());
		try {
			ready.perform();
			Assert.fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyWithoutSpellWeapon() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		BattleController controller = new BattleController(Mockito.mock(BattleMap.class), mockConfig);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		as.getAbilities().add(new FireballAbility());
		as.getAbilities().add(new FireActualizationAbility(1));
		Agent player = new Agent(as);
		ReadySpellAction ready = new ReadySpellAction(controller, player, new FireballSpell());
		try {
			ready.perform();
			Assert.fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyFireballLog() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.getWeapons().add(new InnateCasting());
		as.getAbilities().add(new FireActualizationAbility(1));
		as.getAbilities().add(new FireballAbility());
		map.addAgent(as);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		try {
			new ReadySpellAction(controller, agent, new FireballSpell()).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		Assert.assertEquals(BattleEvent.Type.READY, ev.getType());
		Assert.assertEquals(agent, ev.getPerformer());
		Assert.assertTrue(ev.getSpell() instanceof FireballSpell);
		Assert.assertFalse(ev.getQuickened());
		Assert.assertEquals(1, agent.getAP());
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

	@Test
	public void testQuickenedFireballLog() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.getWeapons().add(new InnateCasting());
		as.getAbilities().add(new FireActualizationAbility(2));
		as.getAbilities().add(new FireballAbility());
		map.addAgent(as);
		BattleController controller = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		try {
			new ReadySpellAction(controller, agent, new FireballSpell()).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		Assert.assertEquals(BattleEvent.Type.READY, ev.getType());
		Assert.assertEquals(agent, ev.getPerformer());
		Assert.assertTrue(ev.getSpell() instanceof FireballSpell);
		Assert.assertTrue(ev.getQuickened());
		Assert.assertEquals(2, agent.getAP());
		Assert.assertEquals(0, ev.getChainEvents().size());
	}

}

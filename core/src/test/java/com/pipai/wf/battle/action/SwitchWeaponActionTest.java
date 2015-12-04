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
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;

public class SwitchWeaponActionTest {

	@Test
	public void testSwitchWeaponLog() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		playerState.getWeapons().add(new Pistol());
		playerState.getWeapons().add(new InnateCasting());
		BattleController controller = new BattleController(mockMap, mockConfig);
		Agent player = new Agent(playerState);
		MockGUIObserver observer = new MockGUIObserver();
		controller.registerObserver(observer);
		Assert.assertTrue(player.getCurrentWeapon() instanceof Pistol);
		try {
			new SwitchWeaponAction(controller, player).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleEvent ev = observer.getEvent();
		Assert.assertEquals(BattleEvent.Type.SWITCH_WEAPON, ev.getType());
		Assert.assertEquals(player, ev.getPerformer());
		Assert.assertEquals(0, ev.getChainEvents().size());
		Assert.assertTrue(player.getCurrentWeapon() instanceof SpellWeapon);
	}

}

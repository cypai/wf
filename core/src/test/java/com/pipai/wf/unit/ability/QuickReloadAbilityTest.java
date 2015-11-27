package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;

public class QuickReloadAbilityTest {

	@Test
	public void testNoQuickReload() {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.getWeapons().add(new Pistol());
		BattleController controller = new BattleController(mockMap, mockConfig);
		Agent agent = new Agent(as);
		try {
			new ReloadAction(controller, agent).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(0, agent.getAP());
	}

	@Test
	public void testQuickReload() {
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, mockPosition, 3, 5, 2, 5, 65, 0);
		as.getAbilities().add(new QuickReloadAbility());
		as.getWeapons().add(new Pistol());
		BattleController controller = new BattleController(mockMap, mockConfig);
		Agent agent = new Agent(as);
		try {
			new ReloadAction(controller, agent).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(1, agent.getAP());
	}

}

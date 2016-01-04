package com.pipai.wf.battle.weapon;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.SwitchWeaponAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class WeaponTest {

	@Test
	public void testNoWeapon() {
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		Agent player = new Agent(playerState);
		Assert.assertEquals(null, player.getCurrentWeapon());
	}

	@Test
	public void testWeaponGrantedAbilities() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap mockMap = Mockito.mock(BattleMap.class);
		BattleController controller = new BattleController(mockMap, mockConfig);
		GridPosition mockPos = Mockito.mock(GridPosition.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, mockPos, 3, 5, 2, 5, 65, 0);
		playerState.getWeapons().add(new Bow());
		playerState.getWeapons().add(new InnateCasting());
		Agent player = new Agent(playerState);
		Assert.assertTrue(player.getAbilities().hasAbility(QuickReloadAbility.class));
		try {
			new SwitchWeaponAction(controller, player).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertFalse(player.getAbilities().hasAbility(QuickReloadAbility.class));
	}

}

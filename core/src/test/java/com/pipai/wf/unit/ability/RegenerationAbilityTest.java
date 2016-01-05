package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.test.WfTestUtils;

public class RegenerationAbilityTest {

	@Test
	public void testHealOnTurnEnd() {
		GridPosition mockPosition = Mockito.mock(GridPosition.class);
		Agent player = WfTestUtils.createGenericAgent(Team.PLAYER, mockPosition);
		player.getAbilities().add(new RegenerationAbility(1));
		player.setHP(player.getMaxHP() - 1);
		Assert.assertEquals(player.getMaxHP() - 1, player.getHP());
		player.onRoundEnd();
		// Regeneration should proc at end of enemy turn
		Assert.assertEquals(player.getMaxHP(), player.getHP());
	}

}

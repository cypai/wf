package com.pipai.wf.unit.ability;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.PrecisionShotAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;

public class PrecisionShotAbilityTest {

	@Test
	public void testPrecisionShotCooldown() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(0, 0);
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = mock(DamageCalculator.class);
		when(mockDamageCalculator.rollDamageGeneral(
				Mockito.any(AccuracyPercentages.class),
				Mockito.any(DamageFunction.class),
				Mockito.anyInt())).thenReturn(new DamageResult(false, false, 0, 0));
		when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		Agent mockTarget = mock(Agent.class);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.abilities.add(new PrecisionShotAbility());
		playerState.weapons.add(new Pistol(mockConfig));
		map.addAgent(playerState);
		Agent agent = map.getAgentAtPos(playerPos);
		BattleController battle = new BattleController(map, mockConfig);
		try {
			battle.performAction(new PrecisionShotAction(agent, mockTarget));
			assertTrue(agent.getInnateAbilities().getAbility(PrecisionShotAbility.class).isOnCooldown());
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}

		battle.endTurn();
		assertTrue(agent.getInnateAbilities().getAbility(PrecisionShotAbility.class).isOnCooldown());
		try {
			battle.performAction(new PrecisionShotAction(agent, mockTarget));
			fail("Expected IllegalActionException not thrown");
		} catch (IllegalActionException e) {
		}

		battle.endTurn();
		assertFalse(agent.getInnateAbilities().getAbility(PrecisionShotAbility.class).isOnCooldown());
		try {
			battle.performAction(new PrecisionShotAction(agent, mockTarget));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
	}

}

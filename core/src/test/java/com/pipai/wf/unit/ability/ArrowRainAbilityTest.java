package com.pipai.wf.unit.ability;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.OverwatchableTargetedAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Bow;

public class ArrowRainAbilityTest {

	@Test
	public void testFirstAction() throws IllegalActionException {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(0, 0);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = Mockito.mock(DamageCalculator.class);
		Mockito.when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(false, false, 0, 0));
		Mockito.when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		Agent mockTarget = Mockito.mock(Agent.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 3, 5, 65, 0);
		playerState.getAbilities().add(new ArrowRainAbility());
		playerState.getWeapons().add(new Bow());
		map.addAgent(playerState);
		Agent agent = map.getAgentAtPos(playerPos);
		BattleController controller = new BattleController(map, mockConfig);
		OverwatchableTargetedAction atk = (OverwatchableTargetedAction) ((Bow) agent.getCurrentWeapon()).getAction(controller, agent, mockTarget);
		atk.perform();
		Assert.assertEquals(2, agent.getAP());
	}

	@Test
	public void testNotFirstAction() throws IllegalActionException {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(0, 0);
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = Mockito.mock(DamageCalculator.class);
		Mockito.when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(false, false, 0, 0));
		Mockito.when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		Agent mockTarget = Mockito.mock(Agent.class);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 3, 5, 65, 0);
		playerState.getAbilities().add(new ArrowRainAbility());
		playerState.getWeapons().add(new Bow());
		map.addAgent(playerState);
		Agent agent = map.getAgentAtPos(playerPos);
		agent.setAP(2);
		BattleController controller = new BattleController(map, mockConfig);
		OverwatchableTargetedAction atk = (OverwatchableTargetedAction) ((Bow) agent.getCurrentWeapon()).getAction(controller, agent, mockTarget);
		atk.perform();
		Assert.assertEquals(0, agent.getAP());
	}

}

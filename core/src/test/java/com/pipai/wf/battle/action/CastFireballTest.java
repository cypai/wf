package com.pipai.wf.battle.action;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedList;

import org.junit.Test;
import org.mockito.Matchers;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.DamageCalculator;
import com.pipai.wf.battle.damage.DamageFunction;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.util.UtilFunctions;

public class CastFireballTest extends GdxMockedTest {

	@Test
	public void testCastFireballLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		DamageCalculator mockDamageCalculator = mock(DamageCalculator.class);
		when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(true, false, 1, 0));
		when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 1000, 0);
		playerState.weapons.add(new InnateCasting(mockConfig));
		playerState.abilities.add(new FireActualizationAbility(1));
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent agent = map.getAgentAtPos(playerPos);
		Agent target = map.getAgentAtPos(enemyPos);
		try {
			battle.performAction(new ReadySpellAction(agent, new FireballSpell()));
			assertTrue(((SpellWeapon) agent.getCurrentWeapon()).getSpell() != null);
			battle.performAction(WeaponActionFactory.defaultWeaponAction(agent, target));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.CAST_TARGET);
		assertTrue(ev.getPerformer() == agent);
		assertTrue(ev.getTarget() == target);
		assertTrue(ev.getSpell() instanceof FireballSpell);
		assertTrue(ev.getChainEvents().size() == 0);
		// Player has 1000 aim, cannot miss
		assertTrue(ev.getDamageResult().hit);
		int expectedHP = UtilFunctions.clamp(0, target.getMaxHP(), target.getMaxHP() - ev.getDamage());
		assertTrue(target.getHP() == expectedHP);
		assertTrue(agent.getHP() == agent.getMaxHP());
		assertTrue(((SpellWeapon) agent.getCurrentWeapon()).getSpell() == null);
	}

	@Test
	public void testFireballOverwatchLog() {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		when(mockConfig.sightRange()).thenReturn(17);
		DamageCalculator mockDamageCalculator = mock(DamageCalculator.class);
		when(mockDamageCalculator.rollDamageGeneral(
				Matchers.any(AccuracyPercentages.class),
				Matchers.any(DamageFunction.class),
				Matchers.anyInt())).thenReturn(new DamageResult(false, false, 0, 0));
		when(mockConfig.getDamageCalculator()).thenReturn(mockDamageCalculator);
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState playerState = factory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new InnateCasting(mockConfig));
		playerState.abilities.add(new FireActualizationAbility(1));
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		map.addAgent(factory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map, mockConfig);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		try {
			battle.performAction(new ReadySpellAction(player, new FireballSpell()));
			assertTrue(((SpellWeapon) player.getCurrentWeapon()).getSpell() != null);
			battle.performAction(new OverwatchAction(player));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.OVERWATCH);
		assertTrue(ev.getPerformer() == player);
		assertTrue(ev.getPreparedOWName().equals("Fireball"));
		assertTrue(ev.getChainEvents().size() == 0);
		// Test Overwatch Activation
		LinkedList<GridPosition> path = new LinkedList<>();
		GridPosition dest = new GridPosition(2, 1);
		path.add(enemy.getPosition());
		path.add(dest);
		MoveAction move = new MoveAction(enemy, path, 1);
		try {
			battle.performAction(move);
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent moveEv = observer.ev;
		assertTrue(moveEv.getType() == BattleEvent.Type.MOVE);
		assertTrue(moveEv.getPerformer() == enemy);
		LinkedList<BattleEvent> chain = moveEv.getChainEvents();
		assertTrue(chain.size() == 1);
		BattleEvent owEv = chain.peekFirst();
		assertTrue(owEv.getType() == BattleEvent.Type.OVERWATCH_ACTIVATION);
		assertTrue(owEv.getPerformer() == player);
		assertTrue(owEv.getTarget() == enemy);
		assertTrue(owEv.getActivatedOWAction() instanceof TargetedSpellWeaponAction);
		assertTrue(owEv.getChainEvents().size() == 0);
		// Overwatch will always have a chance to miss since it clamps before applying aim penalty
		int expectedHP = UtilFunctions.clamp(0, enemy.getMaxHP(), enemy.getMaxHP() - owEv.getDamage());
		assertTrue(enemy.getHP() == expectedHP);
		assertTrue(player.getHP() == player.getMaxHP());
		assertTrue(((SpellWeapon) player.getCurrentWeapon()).getSpell() == null);
	}
}

package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.ReadySpellAction;
import com.pipai.wf.battle.action.TargetedSpellWeaponAction;
import com.pipai.wf.battle.action.WeaponActionFactory;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfConfiguredTest;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.util.UtilFunctions;

public class SpellTest extends WfConfiguredTest {

	@Test
	public void testReadyWithoutActualizationAbility() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0));
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertFalse(player == null || enemy == null);
		assertTrue(player.getSpellList().size() == 0);
		ReadySpellAction ready = new ReadySpellAction(player, new FireballSpell());
		try {
			battle.performAction(ready);
			fail("Did not throw expected IllegalActionException");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testNoActualizationFireballLog() {
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new InnateCasting());
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		BattleController battle = new BattleController(map);
		Agent agent = map.getAgentAtPos(playerPos);
		try {
			battle.performAction(new ReadySpellAction(agent, new FireballSpell()));
			fail("Did not throw expected exception");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testReadyFireballLog() {
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new InnateCasting());
		playerState.abilities.add(new FireActualizationAbility(1));
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		BattleController battle = new BattleController(map);
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
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new InnateCasting());
		playerState.abilities.add(new FireActualizationAbility(2));
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		BattleController battle = new BattleController(map);
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

	@Test
	public void testCastFireballLog() {
		BattleMap map = new BattleMap(3, 4);
		GridPosition playerPos = new GridPosition(1, 0);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 1000, 0);
		playerState.weapons.add(new InnateCasting());
		playerState.abilities.add(new FireActualizationAbility(1));
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
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
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 2);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		playerState.weapons.add(new InnateCasting());
		playerState.abilities.add(new FireActualizationAbility(1));
		playerState.abilities.add(new FireballAbility());
		map.addAgent(playerState);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
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

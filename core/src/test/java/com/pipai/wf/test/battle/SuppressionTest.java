package com.pipai.wf.test.battle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.action.SuppressionAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;
import com.pipai.wf.test.WfConfiguredTest;
import com.pipai.wf.unit.ability.SuppressionAbility;

public class SuppressionTest extends WfConfiguredTest {

	@Test
	public void testSuppression() {
		BattleMap map = new BattleMap(2, 2);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new SuppressionAbility());
		as.weapons.add(new Bow());
		map.addAgent(as);
		AgentState enemyAs = AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0);
		enemyAs.weapons.add(new Bow());
		map.addAgent(enemyAs);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		int toHit = new RangedWeaponAttackAction(enemy, player).getHitCalculation().total();
		BattleController battle = new BattleController(map);
		try {
			battle.performAction(new SuppressionAction(player, enemy));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(enemy.getStatusEffects().aimModifierList().total() == -30);
		int suppressedToHit = new RangedWeaponAttackAction(enemy, player).getHitCalculation().total();
		assertTrue(suppressedToHit == toHit - 30);
		assertTrue(player.getAP() == 0);
		Weapon weapon = player.getCurrentWeapon();
		assertTrue(weapon.currentAmmo() == weapon.baseAmmoCapacity() - 2);
	}

	@Test
	public void testNoAmmoSuppression() {
		BattleMap map = new BattleMap(2, 2);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new SuppressionAbility());
		as.weapons.add(new Bow());
		map.addAgent(as);
		AgentState enemyAs = AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0);
		map.addAgent(enemyAs);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCurrentWeapon() != null);
		player.getCurrentWeapon().expendAmmo(player.getCurrentWeapon().baseAmmoCapacity() + 1);
		BattleController battle = new BattleController(map);
		try {
			battle.performAction(new SuppressionAction(player, enemy));
			fail("Expected not enough ammo exception not thrown");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testSuppressionLog() {
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 1);
		AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 1000, 0);
		playerState.weapons.add(new Pistol());
		playerState.abilities.add(new SuppressionAbility());
		map.addAgent(playerState);
		map.addAgent(AgentStateFactory.newBattleAgentState(Team.ENEMY, enemyPos, 3, 5, 2, 5, 65, 0));
		BattleController battle = new BattleController(map);
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		try {
			battle.performAction(new SuppressionAction(player, enemy));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		BattleEvent ev = observer.ev;
		assertTrue(ev.getType() == BattleEvent.Type.TARGETED_ACTION);
		assertTrue(ev.getTargetedAction().getClass() == SuppressionAction.class);
		assertTrue(ev.getPerformer() == player);
		assertTrue(ev.getTarget() == enemy);
		assertTrue(ev.getChainEvents().size() == 0);
	}

}

package com.pipai.wf.unit.ability;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.pipai.wf.battle.BattleConfiguration;
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
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.BadStateStringException;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.MockGUIObserver;

public class SuppressionTest {

	private static BattleMap generateMap(GridPosition playerPos, GridPosition enemyPos) throws BadStateStringException {
		BattleConfiguration mockConfig = mock(BattleConfiguration.class);
		// when(mockConfig.sightRange()).thenReturn(17);
		BattleMap map = new BattleMap(2, 2);
		AgentStateFactory factory = new AgentStateFactory(mockConfig);
		AgentState player = factory.battleAgentFromStats(Team.PLAYER, playerPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0));
		player.abilities.add(new SuppressionAbility());
		player.weapons.add(new Bow(mockConfig));
		map.addAgent(player);
		AgentState enemy = factory.battleAgentFromStats(Team.ENEMY, enemyPos, factory.statsOnlyState(1, 1, 1, 1, 1, 0));
		enemy.weapons.add(new Bow(mockConfig));
		map.addAgent(enemy);
		return map;
	}

	@Test
	public void testSuppression() throws BadStateStringException {
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		BattleMap map = generateMap(playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		int toHit = new RangedWeaponAttackAction(enemy, player).getHitCalculation().total();
		BattleController battle = new BattleController(map, mock(BattleConfiguration.class));
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
	public void testNoAmmoSuppression() throws BadStateStringException {
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		BattleMap map = generateMap(playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		assertTrue(player.getCurrentWeapon() != null);
		player.getCurrentWeapon().expendAmmo(player.getCurrentWeapon().baseAmmoCapacity() + 1);
		BattleController battle = new BattleController(map, mock(BattleConfiguration.class));
		try {
			battle.performAction(new SuppressionAction(player, enemy));
			fail("Expected not enough ammo exception not thrown");
		} catch (IllegalActionException e) {
		}
	}

	@Test
	public void testSuppressionLog() throws BadStateStringException {
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		BattleMap map = generateMap(playerPos, enemyPos);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		BattleController battle = new BattleController(map, mock(BattleConfiguration.class));
		MockGUIObserver observer = new MockGUIObserver();
		battle.registerObserver(observer);
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

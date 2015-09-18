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
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.test.WfConfiguredTest;
import com.pipai.wf.unit.ability.SuppressionAbility;

public class SuppressionTest extends WfConfiguredTest {

	@Test
	public void testSuppression() {
		BattleMap map = new BattleMap(2, 2);
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 0);
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
	}

}

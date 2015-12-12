package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;

public class KoExpTest extends GdxMockedTest {

	@Test
	public void test() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 1);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 1000, 0);
		playerState.getWeapons().add(new Pistol());
		map.addAgent(playerState);
		AgentState enemyState = factory.battleAgentFromStats(Team.ENEMY, enemyPos, 1, 5, 2, 5, 65, -100);
		enemyState.setExpGiven(5);
		map.addAgent(enemyState);
		BattleController controller = new BattleController(map, config);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		Assert.assertFalse(player == null || enemy == null);
		OverwatchableTargetedAction atk = (OverwatchableTargetedAction) ((Pistol) player.getCurrentWeapon()).getAction(controller, player, enemy);
		try {
			atk.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertTrue(enemy.isKO());
		Assert.assertTrue(enemy.getExpGiven() > 0);
		Assert.assertEquals(player.getExp(), enemy.getExpGiven());
	}

}

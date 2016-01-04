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
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.unit.schema.SlimeSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class KoExpTest extends GdxMockedTest {

	@Test
	public void testPostBattleGainKoExp() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 1);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 1000, 1, 0);
		playerState.getWeapons().add(new Pistol());
		map.addAgent(playerState);
		UnitSchema schema = new SlimeSchema(1);
		AgentState enemyState = factory.battleAgentFromSchema(Team.ENEMY, enemyPos, schema);
		map.addAgent(enemyState);
		BattleController controller = new BattleController(map, config);
		Agent player = map.getAgentAtPos(playerPos);
		Agent enemy = map.getAgentAtPos(enemyPos);
		// Ensure enemy will be KOed
		enemy.setHP(1);
		Assert.assertFalse(player == null || enemy == null);
		Assert.assertTrue(enemy.getExpGiven() > 0);
		Assert.assertEquals(schema.getExpGiven(), enemy.getExpGiven());
		OverwatchableTargetedAction atk = (OverwatchableTargetedAction) ((Pistol) player.getCurrentWeapon()).getAction(controller, player, enemy);
		try {
			atk.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertTrue(enemy.isKO());
		Assert.assertEquals(player.getExp(), enemy.getExpGiven());
	}

}

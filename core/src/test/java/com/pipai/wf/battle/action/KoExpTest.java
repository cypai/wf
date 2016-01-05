package com.pipai.wf.battle.action;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.unit.schema.SlimeSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class KoExpTest extends GdxMockedTest {

	@Test
	public void testPostBattleGainKoExp() throws IllegalActionException {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(5, 5);
		GridPosition playerPos = new GridPosition(1, 1);
		GridPosition enemyPos = new GridPosition(2, 1);
		AgentFactory factory = new AgentFactory();
		Agent player = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 1000, 1, 0);
		Pistol pistol = new Pistol();
		player.getInventory().setItem(pistol, 1);
		map.addAgent(player);
		UnitSchema schema = new SlimeSchema(1);
		Agent enemy = factory.battleAgentFromSchema(Team.ENEMY, enemyPos, schema);
		map.addAgent(enemy);
		BattleController controller = new BattleController(map, config);
		// Ensure enemy will be KOed
		enemy.setHP(1);
		Assert.assertTrue(enemy.getExpGiven() > 0);
		Assert.assertEquals(schema.getExpGiven(), enemy.getExpGiven());
		new RangedWeaponAttackAction(controller, player, enemy, pistol).perform();
		Assert.assertTrue(enemy.isKO());
		Assert.assertEquals(player.getExp(), enemy.getExpGiven());
	}

}

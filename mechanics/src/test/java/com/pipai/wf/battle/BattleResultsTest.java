package com.pipai.wf.battle;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.action.OverwatchableTargetedAction;
import com.pipai.wf.battle.action.RangedWeaponAttackAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.unit.schema.SlimeSchema;
import com.pipai.wf.unit.schema.UnitSchema;
import com.pipai.wf.util.GridPosition;

public class BattleResultsTest extends GdxMockedTest {

	@Test
	public void testVictoryResult() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(2, 2);
		AgentFactory factory = new AgentFactory();
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		Agent player = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 1000, 5, 0);
		Pistol pistol = new Pistol();
		player.getInventory().setItem(pistol, 1);
		UnitSchema schema = new SlimeSchema(1);
		Agent enemy = factory.battleAgentFromSchema(Team.ENEMY, enemyPos, schema);
		map.addAgent(player);
		map.addAgent(enemy);
		BattleController controller = new BattleController(map, config);
		player.setHP(1);
		player.setMP(2);
		// Ensure enemy will be KOed and give exp
		enemy.setHP(1);
		OverwatchableTargetedAction atk = pistol
				.getParticularAction(RangedWeaponAttackAction.class, controller, player).get();
		atk.setTarget(enemy);
		try {
			atk.perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		BattleResult result = controller.battleResult();
		Assert.assertEquals(BattleResult.Result.VICTORY, result.getResult());
		UnitSchema unitState = result.getPartyState().get(0);
		Assert.assertEquals(1, unitState.getHP());
		Assert.assertEquals(3, unitState.getMaxHP());
		// MP should be refreshed after battle
		Assert.assertEquals(5, unitState.getMP());
		Assert.assertEquals(5, unitState.getMaxMP());
		// AP should be refreshed after battle
		Assert.assertEquals(2, unitState.getAP());
		Assert.assertEquals(2, unitState.getMaxAP());
		Assert.assertEquals(5, unitState.getMobility());
		Assert.assertEquals(1000, unitState.getAim());
		Assert.assertEquals(0, unitState.getDefense());
		Assert.assertEquals(schema.getExpGiven(), unitState.getExp());
	}

}

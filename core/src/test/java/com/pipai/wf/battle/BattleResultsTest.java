package com.pipai.wf.battle;

import org.junit.Assert;
import org.junit.Test;

import com.pipai.libgdx.test.GdxMockedTest;
import com.pipai.wf.battle.action.OverwatchableTargetedAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.schema.SlimeSchema;
import com.pipai.wf.unit.schema.UnitSchema;

public class BattleResultsTest extends GdxMockedTest {

	@Test
	public void testVictoryResult() {
		BattleConfiguration config = new BattleConfiguration();
		BattleMap map = new BattleMap(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 1000, 5, 0);
		playerState.getWeapons().add(new Pistol());
		UnitSchema schema = new SlimeSchema(1);
		AgentState enemyState = factory.battleAgentFromSchema(Team.ENEMY, enemyPos, schema);
		map.addAgent(playerState);
		map.addAgent(enemyState);
		BattleController controller = new BattleController(map, config);
		Agent player = map.getAgentAtPos(playerPos);
		player.setHP(1);
		player.setMP(2);
		Agent enemy = map.getAgentAtPos(enemyPos);
		// Ensure enemy will be KOed and give exp
		enemy.setHP(1);
		OverwatchableTargetedAction atk = (OverwatchableTargetedAction) ((Pistol) player.getCurrentWeapon()).getAction(controller, player, enemy);
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

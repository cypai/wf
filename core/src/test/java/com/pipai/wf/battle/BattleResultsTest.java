package com.pipai.wf.battle;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.unit.schema.UnitSchema;

public class BattleResultsTest {

	@Test
	public void testVictoryResult() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(2, 2);
		AgentStateFactory factory = new AgentStateFactory();
		GridPosition playerPos = new GridPosition(0, 0);
		GridPosition enemyPos = new GridPosition(0, 1);
		AgentState playerState = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 100, 5, 0);
		AgentState enemyState = factory.battleAgentFromStats(Team.ENEMY, enemyPos, 3, 5, 2, 100, 5, 0);
		map.addAgent(playerState);
		map.addAgent(enemyState);
		BattleController controller = new BattleController(map, mockConfig);
		Agent player = map.getAgentAtPos(playerPos);
		player.setHP(1);
		player.setMP(2);
		Agent enemy = map.getAgentAtPos(enemyPos);
		enemy.setHP(0);
		BattleResult result = controller.battleResult();
		Assert.assertEquals(BattleResult.Result.VICTORY, result.getResult());
		UnitSchema unitState = result.getPartyState().get(0);
		Assert.assertEquals(1, unitState.getHP());
		Assert.assertEquals(3, unitState.getMaxHP());
		Assert.assertEquals(2, unitState.getMP());
		Assert.assertEquals(5, unitState.getMaxMP());
		Assert.assertEquals(2, unitState.getMaxAP());
		Assert.assertEquals(5, unitState.getMobility());
		Assert.assertEquals(100, unitState.getAim());
		Assert.assertEquals(0, unitState.getDefense());
	}

}

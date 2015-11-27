package com.pipai.wf.battle.effect;

import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.damage.DamageDealer;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.exception.IllegalActionException;

public class AcidStatusEffectTest {

	@Test
	public void testAcid() {
		BattleConfiguration mockConfig = Mockito.mock(BattleConfiguration.class);
		BattleMap map = new BattleMap(2, 2);
		BattleController controller = new BattleController(map, mockConfig);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentStateFactory factory = new AgentStateFactory();
		AgentState as = factory.battleAgentFromStats(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.getWeapons().add(new Bow());
		map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		Assert.assertEquals(agent.getMobility(), agent.getEffectiveMobility());
		agent.inflictStatus(new AcidStatusEffect(agent, 1, new DamageDealer(map)));
		Assert.assertTrue(agent.getEffectiveMobility() < agent.getMobility());
		Assert.assertEquals(agent.getMaxHP(), agent.getHP());
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(new GridPosition(1, 0));
		try {
			new MoveAction(controller, agent, path, 1).perform();
		} catch (IllegalActionException e) {
			Assert.fail(e.getMessage());
		}
		Assert.assertEquals(agent.getMaxHP() - 1, agent.getHP());
	}

}

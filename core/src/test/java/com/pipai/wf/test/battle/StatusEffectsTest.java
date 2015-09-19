package com.pipai.wf.test.battle;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.effect.AcidStatusEffect;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.exception.IllegalActionException;

public class StatusEffectsTest {

	@Test
	public void testAcid() {
		BattleMap map = new BattleMap(2, 2);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.weapons.add(new Bow());
		map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		assertTrue(agent.getEffectiveMobility() == agent.getBaseMobility());
		agent.inflictStatus(new AcidStatusEffect(agent, 1));
		assertTrue(agent.getEffectiveMobility() < agent.getBaseMobility());
		BattleController battle = new BattleController(map);
		assertTrue(agent.getHP() == agent.getMaxHP());
		LinkedList<GridPosition> path = new LinkedList<>();
		path.add(new GridPosition(1, 0));
		try {
			battle.performAction(new MoveAction(agent, path, 1));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(agent.getHP() == agent.getMaxHP() - 1);
	}

}

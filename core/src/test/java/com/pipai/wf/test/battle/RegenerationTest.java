package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityFactory;
import com.pipai.wf.unit.ability.AbilityType;

public class RegenerationTest {

	
	@Test
	public void testHealOnTurnEnd() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		ArrayList<Ability> abilities = new ArrayList<Ability>();
		abilities.add(AbilityFactory.createAbility(AbilityType.REGENERATION));
		as.addAbilities(abilities);
        map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		agent.setHP(agent.getMaxHP() - 1);
		assertFalse(agent == null);
		assertTrue(agent.getHP() == agent.getMaxHP() - 1);
		
		BattleController battle = new BattleController(map);
		battle.endTurn();	// Ends player turn
		battle.endTurn();	// Ends enemy turn
		// Regeneration should proc at end of enemy turn
		assertTrue(agent.getHP() == agent.getMaxHP());
	}
}

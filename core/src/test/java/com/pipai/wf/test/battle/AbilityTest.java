package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.RegenerationAbility;

public class AbilityTest {

	@Test
	public void testHealOnTurnEnd() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		AbilityList abilities = new AbilityList();
		abilities.add(new RegenerationAbility(1));
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

	@Test
	public void testSpellGranted() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		AbilityList abilities = new AbilityList();
		abilities.add(new FireballAbility());
		as.addAbilities(abilities);
		map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		boolean hasFireball = false;
		for (Spell s : agent.getSpellList()) {
			if (s instanceof FireballSpell) {
				hasFireball = true;
				break;
			}
		}
		assertTrue(hasFireball);
	}

}

package com.pipai.wf.test.battle;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.FireballSpell;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.QuickReloadAbility;
import com.pipai.wf.unit.ability.RegenerationAbility;

public class AbilityTest {

	@Test
	public void testHealOnTurnEnd() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new RegenerationAbility(1));
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
		as.abilities.add(new FireballAbility());
		map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		assertTrue(agent.getAbilities().hasSpell(FireballSpell.class));
	}

	@Test
	public void testNoQuickReload() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.weapons.add(new Pistol());
		map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		BattleController battle = new BattleController(map);
		try {
			battle.performAction(new ReloadAction(agent));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(agent.getAP() == 0);
	}

	@Test
	public void testQuickReload() {
		BattleMap map = new BattleMap(1, 1);
		GridPosition playerPos = new GridPosition(0, 0);
		AgentState as = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
		as.abilities.add(new QuickReloadAbility());
		as.weapons.add(new Pistol());
		map.addAgent(as);
		Agent agent = map.getAgentAtPos(playerPos);
		BattleController battle = new BattleController(map);
		try {
			battle.performAction(new ReloadAction(agent));
		} catch (IllegalActionException e) {
			fail(e.getMessage());
		}
		assertTrue(agent.getAP() == 1);
	}

}

package com.pipai.wf.test.battle;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.SwitchWeaponAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.battle.agent.AgentStateFactory;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.unit.ability.QuickReloadAbility;

public class WeaponTest {

    @Test
    public void testNoWeapon() {
        BattleMap map = new BattleMap(3, 4);
        GridPosition playerPos = new GridPosition(1, 0);
        AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
        map.addAgent(playerState);
        Agent player = map.getAgentAtPos(playerPos);
        assertTrue(player.getCurrentWeapon() == null);
    }

    @Test
    public void testWeaponGrantedAbilities() {
        BattleMap map = new BattleMap(3, 4);
        GridPosition playerPos = new GridPosition(1, 0);
        AgentState playerState = AgentStateFactory.newBattleAgentState(Team.PLAYER, playerPos, 3, 5, 2, 5, 65, 0);
        playerState.weapons.add(new Bow());
        playerState.weapons.add(new InnateCasting());
        map.addAgent(playerState);
        BattleController battle = new BattleController(map);
        Agent player = map.getAgentAtPos(playerPos);
        assertTrue(player.getAbilities().hasAbility(QuickReloadAbility.class));
        try {
            battle.performAction(new SwitchWeaponAction(player));
        } catch (IllegalActionException e) {
            fail(e.getMessage());
        }
        assertFalse(player.getAbilities().hasAbility(QuickReloadAbility.class));
    }

}

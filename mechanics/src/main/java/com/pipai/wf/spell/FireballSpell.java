package com.pipai.wf.spell;

import java.util.Arrays;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.TargetedSpellWeaponAction;
import com.pipai.wf.battle.agent.Agent;

public class FireballSpell extends Spell {

	@Override
	public int requiredMP() {
		return 1;
	}

	@Override
	public int getMinDamage() {
		return 3;
	}

	@Override
	public int getMaxDamage() {
		return 5;
	}

	@Override
	public String getName() {
		return "Fireball";
	}

	@Override
	public String getDescription() {
		return "A simple fire spell that does good damage";
	}

	@Override
	public boolean canTargetAgent() {
		return true;
	}

	@Override
	public boolean canOverwatch() {
		return true;
	}

	@Override
	public SpellElement element() {
		return SpellElement.FIRE;
	}

	@Override
	public List<Action> getAvailableActions(BattleController controller, Agent agent) {
		return Arrays.asList(new TargetedSpellWeaponAction(controller, agent));
	}

}

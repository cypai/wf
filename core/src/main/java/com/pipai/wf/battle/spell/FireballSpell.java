package com.pipai.wf.battle.spell;

import com.pipai.wf.battle.action.TargetedSpellWeaponAction;
import com.pipai.wf.battle.action.TargetedAction;
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
	public String name() {
		return "Fireball";
	}

	@Override
	public String description() {
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
	public TargetedAction getAction(Agent performer, Agent target) {
		return new TargetedSpellWeaponAction(performer, target);
	}

	@Override
	public SpellElement element() {
		return SpellElement.FIRE;
	}

}

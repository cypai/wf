package com.pipai.wf.spell;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.TargetedAction;
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
	public TargetedAction getAction(BattleController controller, Agent performer, Agent target) {
		return new TargetedSpellWeaponAction(controller, performer, target);
	}

	@Override
	public SpellElement element() {
		return SpellElement.FIRE;
	}

}

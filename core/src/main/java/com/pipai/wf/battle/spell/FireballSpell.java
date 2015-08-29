package com.pipai.wf.battle.spell;

import com.pipai.wf.battle.action.TargetedSpellWeaponAction;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.util.UtilFunctions;

public class FireballSpell extends Spell {

	@Override
	public int requiredMP() {
		return 1;
	}

	@Override
	public int getAccuracy(Agent attacker, Agent target, float distance) {
		int total_aim = attacker.getBaseAim() - target.getDefense(attacker.getPosition());
		int acc = UtilFunctions.clamp(1, 100, total_aim);
		return acc;
	}

	@Override
	public int getCritPercentage(Agent attacker, Agent target, float distance) {
		return 0;
	}

	@Override
	public boolean rollToHit(Agent attacker, Agent target, float distance) {
		int diceroll = 1 + UtilFunctions.rng.nextInt(100);
		return diceroll <= getAccuracy(attacker, target, distance);
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
		return new TargetedSpellWeaponAction(performer, target, this);
	}

}

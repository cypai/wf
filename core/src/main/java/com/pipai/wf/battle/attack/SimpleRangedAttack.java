package com.pipai.wf.battle.attack;

import com.pipai.wf.util.UtilFunctions;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.weapon.Weapon;

public class SimpleRangedAttack extends Attack {

	public int requiredAmmo() {
		return 1;
	}
	
	public int getAccuracy(Agent attacker, Agent target, float distance) {
		Weapon weapon = attacker.getCurrentWeapon();
		int total_aim = attacker.getBaseAim() + weapon.flatAimModifier() - target.getDefense(attacker.getPosition());
		int acc = UtilFunctions.clamp(1, 100, total_aim);
		return acc;
	}
	
	public int getCritPercentage(Agent attacker, Agent target, float distance) {
		return 0;
	}
	
	public boolean rollToHit(Agent attacker, Agent target, float distance) {
		int diceroll = 1 + UtilFunctions.rng.nextInt(100);
		return diceroll <= getAccuracy(attacker, target, distance);
	}
	
	public int getMinDamage(Agent attacker, Agent target) {
		return attacker.getCurrentWeapon().minBaseDamage();
	}
	
	public int getMaxDamage(Agent attacker, Agent target) {
		return attacker.getCurrentWeapon().maxBaseDamage();
	}
	
	public int damageRoll(Agent attacker, Agent target, float distance) {
		int min_dmg = getMinDamage(attacker, target);
		int max_dmg = getMaxDamage(attacker, target);
		return UtilFunctions.rng.nextInt(max_dmg - min_dmg + 1) + min_dmg;
	}
	
	public String description() {
		return "A simple ranged attack for testing purposes";
	}
	
}

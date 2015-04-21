package com.pipai.wf.battle.attack;

import java.util.Random;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.weapon.Weapon;

public abstract class Attack {
	/*
	 * Note: All accuracies are now calculated using integers for ease of modifying
	 */
	
	public static final Random rng = new Random();
	
	public abstract int getAccuracy(Agent attacker, Agent target, float distance);
	
	/*
	 * Returns the conditional probability P(Crit | Hit)
	 */
	public abstract int getCritPercentage(Agent attacker, Agent target, float distance);
	
	public abstract boolean rollToHit(Agent attacker, Agent target, float distance);
	
	public abstract int getMinDamage(Agent attacker, Agent target);
	
	public abstract int getMaxDamage(Agent attacker, Agent target);
	
	public abstract int damageRoll(Agent attacker, Agent target, float distance);
	
	public abstract String description();
	
}

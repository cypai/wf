package com.pipai.wf.battle.attack;

import com.pipai.wf.battle.agent.Agent;

public abstract class Attack {
	/*
	 * Note: All accuracies are now calculated using integers for ease of modifying
	 */
	
	/*
	 * How much ammo necessary to perform this attack
	 */
	public abstract int requiredAmmo();
	
	public abstract int getAccuracy(Agent attacker, Agent target, float distance);
	
	/*
	 * Returns the conditional probability P(Crit | Hit)
	 */
	public abstract int getCritPercentage(Agent attacker, Agent target, float distance);
	
	public abstract boolean rollToHit(Agent attacker, Agent target, float distance);
	
	public abstract int getMinDamage(Agent attacker, Agent target);
	
	public abstract int getMaxDamage(Agent attacker, Agent target);
	
	public abstract int damageRoll(Agent attacker, Agent target, float distance);
	
	public abstract String name();
	
	public abstract String description();
	
}

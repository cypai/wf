package com.pipai.wf.unit.ability;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.spell.Spell;

public abstract class Ability {
	
	private int level;
	private AbilityType type;
	
	public Ability(AbilityType t, int level) {
		type = t;
		this.level = level;
	}
	
	public abstract String name();
	
	public abstract String description();
	
	public void onCast(Agent caster, Spell spell) {}
	
	public void onEndTurn(Agent a) {}
	
	public void onRoundEnd(Agent a) {}
	
	public Spell grantSpell() {
		return null;
	}
	
	public AbilityType type() {
		return type;
	}
	
	public int getLevel() {
		return level;
	}
	
}

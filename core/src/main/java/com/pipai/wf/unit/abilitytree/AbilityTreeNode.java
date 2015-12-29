package com.pipai.wf.unit.abilitytree;

import com.pipai.wf.unit.ability.Ability;

public class AbilityTreeNode {

	private Ability ability;
	private boolean taken;

	public AbilityTreeNode(Ability ability) {
		this.ability = ability;
		taken = false;
	}

	public Ability getAbility() {
		return ability;
	}

	public boolean isTaken() {
		return taken;
	}

	public void setTaken(boolean taken) {
		this.taken = taken;
	}

}

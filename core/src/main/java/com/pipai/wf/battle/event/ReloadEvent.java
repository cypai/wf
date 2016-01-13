package com.pipai.wf.battle.event;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.item.weapon.Weapon;

public class ReloadEvent extends BattleEvent {

	public final Agent performer;
	public final Weapon weapon;

	public ReloadEvent(Agent performer, Weapon weapon) {
		this.performer = performer;
		this.weapon = weapon;
	}

	@Override
	public String toString() {
		return performer.getName() + " reloaded " + weapon.getName();
	}

}

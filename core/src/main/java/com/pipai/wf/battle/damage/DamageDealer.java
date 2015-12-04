package com.pipai.wf.battle.damage;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.BattleMap;

public class DamageDealer {

	private BattleMap map;

	public DamageDealer(BattleMap map) {
		this.map = map;
	}

	public void doDamage(DamageResult damage, Agent target) {
		target.takeDamage(damage.getDamage());
		if (target.isKO()) {
			map.getCell(target.getPosition()).makeAgentInactive();
		}
	}

}

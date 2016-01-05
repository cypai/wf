package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.item.Item;
import com.pipai.wf.item.weapon.Weapon;

public final class AiHelper {

	private AiHelper() {
	}

	public static Weapon getAgentWeapon(Agent a) {
		for (Item item : a.getInventory().getItems()) {
			if (item instanceof Weapon) {
				return (Weapon) item;
			}
		}
		return null;
	}

}

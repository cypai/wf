package com.pipai.wf.battle.ai.utils;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.item.Item;

public class ActionListGenerator {

	private BattleController controller;

	public ActionListGenerator(BattleController controller) {
		this.controller = controller;
	}

	public ArrayList<Action> generateWeaponActionList(Agent a) {
		// TODO: Fix this
		ArrayList<Action> list = new ArrayList<>();
		AgentInventory inventory = a.getInventory();
		for (int i = 1; i <= inventory.getSlots(); i++) {
			Item item = inventory.getItem(i);
			if (item != null) {
				list.addAll(item.getAvailableActions(controller, a));
			}
		}
		return list;
	}

}

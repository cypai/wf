package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.WeaponActionFactory;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.vision.AgentVisionCalculator;
import com.pipai.wf.battle.weapon.Weapon;

public class ActionListGenerator {

	private BattleController controller;

	public ActionListGenerator(BattleController controller) {
		this.controller = controller;
	}

	public ArrayList<Action> generateWeaponActionList(Agent a) {
		AgentVisionCalculator calc = new AgentVisionCalculator(controller.getBattleMap(), controller.getBattleConfiguration());
		ArrayList<Action> list = new ArrayList<>();
		ArrayList<Weapon> weaponList = a.getWeapons();
		if (weaponList.size() > 0 && weaponList.get(0).currentAmmo() > 0) {
			for (Agent target : calc.enemiesInRangeOf(a)) {
				list.add(new WeaponActionFactory(controller).defaultWeaponAction(a, target));
			}
			list.add(new OverwatchAction(controller, a));
		}
		return list;
	}

}

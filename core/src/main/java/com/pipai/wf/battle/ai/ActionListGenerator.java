package com.pipai.wf.battle.ai;

import java.util.ArrayList;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.WeaponActionFactory;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.weapon.Weapon;

public class ActionListGenerator {

	public static ArrayList<Action> generateWeaponActionList(Agent a) {
		ArrayList<Action> list = new ArrayList<Action>();
		ArrayList<Weapon> weaponList = a.getWeapons();
		if (weaponList.size() > 0) {
			for (Agent target : a.targetableEnemies()) {
				list.add(WeaponActionFactory.defaultWeaponAction(a, target));
			}
			list.add(new OverwatchAction(a));
		}
		return list;
	}

}

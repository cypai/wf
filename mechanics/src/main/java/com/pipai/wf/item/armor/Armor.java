package com.pipai.wf.item.armor;

import java.util.ArrayList;
import java.util.List;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.EquipArmorAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.item.Item;
import com.pipai.wf.util.UtilFunctions;

public abstract class Armor extends Item {

	private static final EquipArmorAction EQUIP_ARMOR_ACTION = new EquipArmorAction();

	private int hp;

	public Armor(int hp) {
		this.hp = hp;
	}

	@Override
	public List<Action> getAvailableActions(BattleController controller, Agent agent) {
		ArrayList<Action> actions = new ArrayList<>(1);
		if (agent.getInventory().contains(this)) {
			EQUIP_ARMOR_ACTION.setBattleController(controller);
			EQUIP_ARMOR_ACTION.setPerformer(agent);
			EQUIP_ARMOR_ACTION.setSlot(agent.getInventory().getSlotFor(this));
			actions.add(EQUIP_ARMOR_ACTION);
		}
		return actions;
	}

	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = UtilFunctions.clamp(hp, 0, maxHP());
	}

	/**
	 * @return Amount of damage that went through the armor
	 */
	public int takeDamage(int val) {
		if (val > hp) {
			int pierceDmg = val - hp;
			hp = 0;
			return pierceDmg;
		} else {
			hp -= val;
			return 0;
		}
	}

	public abstract int maxHP();

	@Override
	public abstract Armor copyAsNew();

	@Override
	public Armor copy() {
		Armor armor = copyAsNew();
		armor.setHP(getHP());
		return armor;
	}

}

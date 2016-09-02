package com.pipai.wf.unit.schema;

import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.RegenerationAbility;
import com.pipai.wf.util.UtilFunctions;

public class SlimeSchema extends NewUnitSchema {

	private int level;

	public SlimeSchema(int level) {
		super("Slime", new BasicStats(4 + level, 7 + level / 2, 2, 60, 8, 0));
		this.level = level;
	}

	@Override
	public AbilityList getAbilities() {
		AbilityList l = new AbilityList();
		RegenerationAbility regen = new RegenerationAbility();
		regen.setLevel(UtilFunctions.clamp(level / 2, 1, 5));
		l.add(regen);
		FireActualizationAbility fireAbility = new FireActualizationAbility();
		fireAbility.setLevel(1);
		l.add(fireAbility);
		l.add(new FireballAbility());
		return l;
	}

	@Override
	public AgentInventory getInventory() {
		AgentInventory inventory = new AgentInventory(3);
		inventory.setItem(new InnateCasting(), 1);
		return inventory;
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public int getExpGiven() {
		return 10 + 2 * level;
	}

}

package com.pipai.wf.unit.schema;

import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.item.armor.LeatherArmor;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.SuppressionAbility;
import com.pipai.wf.unit.race.Race;

public class TidusSchema extends RaceTemplateSchema {

	public TidusSchema() {
		super(Race.HUMAN);
	}

	@Override
	public int getAim() {
		// For testing purposes
		return 100;
	}

	@Override
	public AbilityList getAbilities() {
		AbilityList l = new AbilityList();
		FireActualizationAbility fireAbility = new FireActualizationAbility();
		fireAbility.setLevel(1);
		l.add(fireAbility);
		l.add(new FireballAbility());
		l.add(new SuppressionAbility());
		return l;
	}

	@Override
	public AgentInventory getInventory() {
		// TODO: Magic number here
		AgentInventory inventory = new AgentInventory(3);
		inventory.setItem(new LeatherArmor(), 1);
		inventory.setItem(new Bow(), 2);
		inventory.setItem(new InnateCasting(), 3);
		return inventory;
	}

	@Override
	public String getName() {
		return "Tidus";
	}

}

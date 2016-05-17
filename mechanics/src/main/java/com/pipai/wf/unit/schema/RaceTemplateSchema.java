package com.pipai.wf.unit.schema;

import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.item.armor.LeatherArmor;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.race.Race;

public class RaceTemplateSchema extends NewUnitSchema {

	private Race race;

	public RaceTemplateSchema(Race race) {
		super(race.getName(), race.getBasicStats());
		this.race = race;
	}

	@Override
	public AbilityList getAbilities() {
		AbilityList l = new AbilityList();
		return l;
	}

	@Override
	public AgentInventory getInventory() {
		// TODO: Magic number here
		AgentInventory inventory = new AgentInventory(3);
		inventory.setItem(new LeatherArmor(), 1);
		if (race == Race.FAIRY) {
			inventory.setItem(new Bow(), 2);
		} else {
			inventory.setItem(new Pistol(), 2);
		}
		if (getAbilities().hasAbility(FireActualizationAbility.class)) {
			// TODO: Deal with this, it's not technically an item
			inventory.setItem(new InnateCasting(), 3);
		}
		return inventory;
	}

	@Override
	public int getLevel() {
		return 1;
	}

	@Override
	public int getExpGiven() {
		return 0;
	}

	@Override
	public String getName() {
		return race.getName();
	}

}

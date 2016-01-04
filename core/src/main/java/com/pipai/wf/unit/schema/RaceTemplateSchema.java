package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.item.armor.Armor;
import com.pipai.wf.item.armor.LeatherArmor;
import com.pipai.wf.item.weapon.Bow;
import com.pipai.wf.item.weapon.InnateCasting;
import com.pipai.wf.item.weapon.Pistol;
import com.pipai.wf.item.weapon.Weapon;
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
	public Armor getArmor() {
		return new LeatherArmor();
	}

	@Override
	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> l = new ArrayList<>();
		if (race == Race.FAIRY) {
			l.add(new Bow());
		} else {
			l.add(new Pistol());
		}
		if (getAbilities().hasAbility(FireActualizationAbility.class)) {
			l.add(new InnateCasting());
		}
		return l;
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

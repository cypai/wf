package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.race.Race;

public class RaceTemplateSchema extends NewUnitSchema {

	private Race race;

	public RaceTemplateSchema(Race race) {
		super(race.getName(), race.getHP(), race.getMP(), race.getAP(), race.getAim(), race.getMobility(), race.getDefense());
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
	public String getName() {
		return race.getName();
	}

}

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

public class RaceTemplateSchema implements UnitSchema {

	private Race race;

	public RaceTemplateSchema(Race race) {
		this.race = race;
	}

	@Override
	public int hp() {
		return race.hp;
	}

	@Override
	public int mp() {
		return race.mp;
	}

	@Override
	public int ap() {
		return race.ap;
	}

	@Override
	public int aim() {
		return race.aim;
	}

	@Override
	public int mobility() {
		return race.mobility;
	}

	@Override
	public int defense() {
		return race.defense;
	}

	@Override
	public AbilityList abilities() {
		AbilityList l = new AbilityList();
		return l;
	}

	@Override
	public Armor armor() {
		return new LeatherArmor();
	}

	@Override
	public ArrayList<Weapon> weapons() {
		ArrayList<Weapon> l = new ArrayList<Weapon>();
		if (race == Race.FAIRY) {
			l.add(new Bow());
		} else {
			l.add(new Pistol());
		}
		if (abilities().hasAbility(FireActualizationAbility.class)) {
			l.add(new InnateCasting());
		}
		return l;
	}

	@Override
	public String name() {
		return race.name;
	}

}

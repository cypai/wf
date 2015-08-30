package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.race.Race;

public class TidusSchema extends RaceTemplateSchema {

	public TidusSchema() {
		super(Race.HUMAN);
	}

	@Override
	public AbilityList abilities() {
		AbilityList l = new AbilityList();
		l.add(new FireballAbility());
		return l;
	}

	@Override
	public Armor armor() {
		return new LeatherArmor();
	}

	@Override
	public ArrayList<Weapon> weapons() {
		ArrayList<Weapon> l = new ArrayList<Weapon>();
		l.add(new Pistol());
		l.add(new SpellWeapon());
		return l;
	}

	@Override
	public String name() {
		return "Tidus";
	}

}

package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.battle.weapon.Pistol;
import com.pipai.wf.battle.weapon.SpellWeapon;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.Ability;
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
	public ArrayList<Ability> abilities() {
		ArrayList<Ability> l = new ArrayList<Ability>();
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
	
}

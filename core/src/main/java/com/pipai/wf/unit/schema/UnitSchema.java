package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.AbilityList;

public interface UnitSchema {
	
	public int hp();
	public int mp();
	public int ap();
	public int aim();
	public int mobility();
	public int defense();
	
	public AbilityList abilities();
	
	public Armor armor();
	public ArrayList<Weapon> weapons();
	
}

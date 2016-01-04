package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.item.armor.Armor;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.unit.ability.AbilityList;

public abstract class NewUnitSchema extends UnitSchema {

	public NewUnitSchema(String name, BasicStats stats) {
		super(name, stats);
	}

	@Override
	public final int getHP() {
		return getMaxHP();
	}

	@Override
	public final int getMP() {
		return getMaxMP();
	}

	@Override
	public int getExp() {
		return 0;
	}

	@Override
	public final void setLevel(int level) {
		throw new UnsupportedOperationException("Cannot set level for a UnitSchema schematic detailing a new unit. "
				+ "Instead, create a new UnitSchema(this) to use it as a prototype if you need to change it.");
	}

	@Override
	public final void setExp(int exp) {
		throw new UnsupportedOperationException("Cannot set exp for a UnitSchema schematic detailing a new unit. "
				+ "Instead, create a new UnitSchema(this) to use it as a prototype if you need to change it.");
	}

	@Override
	public abstract int getExpGiven();

	@Override
	public abstract AbilityList getAbilities();

	@Override
	public abstract Armor getArmor();

	@Override
	public abstract ArrayList<Weapon> getWeapons();

	@Override
	public abstract int getLevel();

}

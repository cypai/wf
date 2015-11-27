package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.NoArmor;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.RegenerationAbility;
import com.pipai.wf.util.UtilFunctions;

public class SlimeSchema extends NewUnitSchema {

	private int level;

	public SlimeSchema(int level) {
		super("Slime", 4 + level, 7 + level / 2, 2, 60, 8, 0);
		this.level = level;
	}

	@Override
	public AbilityList getAbilities() {
		AbilityList l = new AbilityList();
		l.add(new RegenerationAbility(UtilFunctions.clamp(1, 5, level / 2)));
		l.add(new FireActualizationAbility(1));
		l.add(new FireballAbility());
		return l;
	}

	@Override
	public Armor getArmor() {
		return new NoArmor();
	}

	@Override
	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> l = new ArrayList<>();
		l.add(new InnateCasting());
		return l;
	}

}

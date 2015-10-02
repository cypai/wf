package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.NoArmor;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.RegenerationAbility;
import com.pipai.wf.util.UtilFunctions;

public class SlimeSchema implements UnitSchema {

	private int level;

	public SlimeSchema(int level) {
		this.level = level;
	}

	@Override
	public int hp() {
		return 4 + level;
	}

	@Override
	public int mp() {
		return 7 + level / 2;
	}

	@Override
	public int ap() {
		return 2;
	}

	@Override
	public int aim() {
		return 60;
	}

	@Override
	public int mobility() {
		return 8;
	}

	@Override
	public int defense() {
		return 0;
	}

	@Override
	public AbilityList abilities() {
		AbilityList l = new AbilityList();
		l.add(new RegenerationAbility(UtilFunctions.clamp(1, 5, level / 2)));
		l.add(new FireActualizationAbility(1));
		l.add(new FireballAbility());
		return l;
	}

	@Override
	public Armor armor() {
		return new NoArmor();
	}

	@Override
	public ArrayList<Weapon> weapons(BattleConfiguration config) {
		ArrayList<Weapon> l = new ArrayList<>();
		l.add(new InnateCasting(config));
		return l;
	}

	@Override
	public String name() {
		return "Slime";
	}

}

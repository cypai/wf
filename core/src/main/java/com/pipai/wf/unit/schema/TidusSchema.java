package com.pipai.wf.unit.schema;

import java.util.ArrayList;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.battle.weapon.Bow;
import com.pipai.wf.battle.weapon.InnateCasting;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.ability.SuppressionAbility;
import com.pipai.wf.unit.race.Race;

public class TidusSchema extends RaceTemplateSchema {

	public TidusSchema() {
		super(Race.HUMAN);
	}

	@Override
	public int getAim() {
		// For testing purposes
		return 100;
	}

	@Override
	public AbilityList getAbilities() {
		AbilityList l = new AbilityList();
		FireActualizationAbility fireAbility = new FireActualizationAbility();
		fireAbility.setLevel(1);
		l.add(fireAbility);
		l.add(new FireballAbility());
		l.add(new SuppressionAbility());
		return l;
	}

	@Override
	public Armor getArmor() {
		return new LeatherArmor();
	}

	@Override
	public ArrayList<Weapon> getWeapons() {
		ArrayList<Weapon> l = new ArrayList<>();
		l.add(new Bow());
		l.add(new InnateCasting());
		return l;
	}

	@Override
	public String getName() {
		return "Tidus";
	}

}

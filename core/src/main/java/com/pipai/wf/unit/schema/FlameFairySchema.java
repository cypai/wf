package com.pipai.wf.unit.schema;

import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.armor.LeatherArmor;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.FireActualizationAbility;
import com.pipai.wf.unit.ability.FireballAbility;
import com.pipai.wf.unit.race.Race;

public class FlameFairySchema extends RaceTemplateSchema {

	public FlameFairySchema() {
		super(Race.FAIRY);
	}

	@Override
	public AbilityList getAbilities() {
		AbilityList l = new AbilityList();
		l.add(new FireActualizationAbility(2));
		l.add(new FireballAbility());
		return l;
	}

	@Override
	public Armor getArmor() {
		return new LeatherArmor();
	}

	@Override
	public String getName() {
		return "Flame Fairy";
	}

}

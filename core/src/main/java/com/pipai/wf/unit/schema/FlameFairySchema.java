package com.pipai.wf.unit.schema;

import com.pipai.wf.item.armor.Armor;
import com.pipai.wf.item.armor.LeatherArmor;
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
		FireActualizationAbility fireActualizationAbility = new FireActualizationAbility();
		fireActualizationAbility.setLevel(2);
		l.add(fireActualizationAbility);
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

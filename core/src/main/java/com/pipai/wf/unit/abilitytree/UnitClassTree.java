package com.pipai.wf.unit.abilitytree;

import com.pipai.wf.unit.ability.PrecisionShotAbility;
import com.pipai.wf.unit.ability.QuickReloadAbility;
import com.pipai.wf.unit.ability.SuppressionAbility;

public enum UnitClassTree {

	RANGER(new AbilityTree.Builder(new QuickReloadAbility())
			.addAbilityAtCurrentLevel(new SuppressionAbility())
			.addAbilityAtCurrentLevel(new PrecisionShotAbility())
			.goToNextLevel()
			.addAbilityAtCurrentLevel(new SuppressionAbility())
			.addAbilityAtCurrentLevel(new SuppressionAbility())
			.addAbilityAtCurrentLevel(new SuppressionAbility())
			.goToNextLevel()
			.addAbilityAtCurrentLevel(new SuppressionAbility())
			.build());

	private AbilityTree abilityTree;

	UnitClassTree(AbilityTree abilityTree) {
		this.abilityTree = abilityTree;
	}

	public AbilityTree getAbilityTree() {
		return abilityTree.deepCopyAsNew();
	}

}

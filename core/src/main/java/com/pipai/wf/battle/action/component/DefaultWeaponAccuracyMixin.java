package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.battle.damage.PercentageModifierList;

public interface DefaultWeaponAccuracyMixin extends ActionInterface, HasPerformerTargetComponents, HasWeaponComponent,
		HasHitCritComponents, AccuracyPercentages {

	@Override
	default PercentageModifierList getHitCalculation() {
		return getTargetedActionCalculator().baseHitCalculation(getBattleMap(), getPerformer(), getTarget(),
				getWeapon());
	}

	@Override
	default PercentageModifierList getCritCalculation() {
		return getTargetedActionCalculator().baseCritCalculation(getBattleMap(), getPerformer(), getTarget(),
				getWeapon());
	}

}

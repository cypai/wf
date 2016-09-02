package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.damage.AccuracyPercentages;
import com.pipai.wf.util.UtilFunctions;

public interface HasHitCritComponents extends HitAccuracyComponent, CritAccuracyComponent, AccuracyPercentages {

	@Override
	default int toHit() {
		int totalAim = getHitCalculation().total();
		return UtilFunctions.clamp(totalAim, 1, 100);
	}

	@Override
	default int toCrit() {
		int critProb = getCritCalculation().total();
		return UtilFunctions.clamp(critProb, 1, 100);
	}

}

package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.util.UtilFunctions;

public interface CritAccuracyComponent {

	PercentageModifierList getCritCalculation();

	default int toCrit() {
		int critProb = getCritCalculation().total();
		return UtilFunctions.clamp(1, 100, critProb);
	}

}

package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.damage.PercentageModifierList;
import com.pipai.wf.util.UtilFunctions;

public interface HitAccuracyComponent {

	PercentageModifierList getHitCalculation();

	default int toHit() {
		int total_aim = getHitCalculation().total();
		return UtilFunctions.clamp(1, 100, total_aim);
	}

}

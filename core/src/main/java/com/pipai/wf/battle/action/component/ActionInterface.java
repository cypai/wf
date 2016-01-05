package com.pipai.wf.battle.action.component;

import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.damage.TargetedActionCalculator;
import com.pipai.wf.battle.map.BattleMap;

public interface ActionInterface {

	BattleMap getBattleMap();

	TargetedActionCalculator getTargetedActionCalculator();

	ActionVerificationResult verify();

}

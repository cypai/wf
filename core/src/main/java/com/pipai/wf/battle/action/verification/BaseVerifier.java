package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.battle.action.component.ApRequiredComponent;
import com.pipai.wf.battle.action.component.PerformerComponent;
import com.pipai.wf.battle.action.component.TargetComponent;
import com.pipai.wf.battle.action.component.WeaponComponent;

public class BaseVerifier implements ActionVerifier {

	private static final BaseVerifier VERIFIER = new BaseVerifier();

	public static BaseVerifier getInstance() {
		return VERIFIER;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (action.getBattleController() == null) {
			return ActionVerificationResult.invalidResult("No battle controller set for action " + action.getName());
		}
		boolean hasPerformerComp = action instanceof PerformerComponent;
		boolean hasTargetComp = action instanceof TargetComponent;
		boolean hasWeaponComp = action instanceof WeaponComponent;
		if (hasPerformerComp && ((PerformerComponent) action).getPerformer() == null) {
			return ActionVerificationResult.invalidResult("No performer set for action " + action.getName());
		}
		if (hasPerformerComp && ((PerformerComponent) action).getPerformer().isKO()) {
			return ActionVerificationResult.invalidResult("KOed unit cannot act");
		}
		if (hasTargetComp && ((TargetComponent) action).getTarget() == null) {
			return ActionVerificationResult.invalidResult("No target set for action " + action.getName());
		}
		if (hasWeaponComp && ((WeaponComponent) action).getWeapon() == null) {
			return ActionVerificationResult.invalidResult("No weapon set for action " + action.getName());
		}
		if (hasPerformerComp && action instanceof ApRequiredComponent) {
			if (((PerformerComponent) action).getPerformer().getAP() < ((ApRequiredComponent) action).getAPRequired()) {
				return ActionVerificationResult.invalidResult("Not enough AP to perform action " + action.getName());
			}
		}
		return ActionVerificationResult.validResult();
	}

}

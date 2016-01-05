package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;

public class WeaponFlagVerifier implements ActionVerifier {

	private Weapon weapon;
	private WeaponFlag flag;
	private String errorMsg;

	public WeaponFlagVerifier(Weapon weapon, WeaponFlag flag, String errorMsg) {
		this.weapon = weapon;
		this.flag = flag;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (weapon == null) {
			return ActionVerificationResult.invalidResult("No weapon specified");
		}
		if (!weapon.hasFlag(flag)) {
			return ActionVerificationResult.invalidResult(errorMsg);
		}
		return ActionVerificationResult.validResult();
	}

}

package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;

public class WeaponAmmoVerifier implements ActionVerifier {

	private Weapon weapon;
	private int minimumRequiredAmmo;

	public WeaponAmmoVerifier(Weapon weapon, int minimumRequiredAmmo) {
		this.weapon = weapon;
		this.minimumRequiredAmmo = minimumRequiredAmmo;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (weapon == null) {
			return ActionVerificationResult.invalidResult("No weapon specified");
		}
		if (weapon.hasFlag(WeaponFlag.NEEDS_AMMUNITION) && weapon.getCurrentAmmo() < minimumRequiredAmmo) {
			return ActionVerificationResult.invalidResult("Not enough ammunition");
		}
		return ActionVerificationResult.validResult();
	}

}

package com.pipai.wf.battle.action.verification;

import java.util.function.BooleanSupplier;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;

public class SupplierVerifier implements ActionVerifier {

	private BooleanSupplier supplier;
	private String errorMsg;

	public SupplierVerifier(BooleanSupplier supplier, String errorMsg) {
		this.supplier = supplier;
		this.errorMsg = errorMsg;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (!supplier.getAsBoolean()) {
			return ActionVerificationResult.invalidResult(errorMsg);
		}
		return ActionVerificationResult.validResult();
	}

}

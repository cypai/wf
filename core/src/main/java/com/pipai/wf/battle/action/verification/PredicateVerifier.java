package com.pipai.wf.battle.action.verification;

import java.util.function.Predicate;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;

public class PredicateVerifier<T> implements ActionVerifier {

	private Predicate<T> predicate;
	private T arg;
	private String errorMsg;

	public PredicateVerifier(Predicate<T> predicate, T arg, String errorMsg) {
		this.predicate = predicate;
		this.arg = arg;
		this.errorMsg = errorMsg;
	}

	@Override
	public ActionVerificationResult verify(Action action) {
		if (!predicate.test(arg)) {
			return ActionVerificationResult.invalidResult(errorMsg);
		}
		return ActionVerificationResult.validResult();
	}

}

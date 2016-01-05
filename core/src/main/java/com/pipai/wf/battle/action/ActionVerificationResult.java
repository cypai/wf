package com.pipai.wf.battle.action;

public class ActionVerificationResult {

	private final boolean isValid;
	private final String reason;

	private static final ActionVerificationResult VALID_RESULT = new ActionVerificationResult();

	public static ActionVerificationResult validResult() {
		return VALID_RESULT;
	}

	public static ActionVerificationResult invalidResult(String reason) {
		return new ActionVerificationResult(reason);
	}

	private ActionVerificationResult() {
		isValid = true;
		reason = "";
	}

	private ActionVerificationResult(String reason) {
		isValid = false;
		this.reason = reason;
	}

	public boolean isValid() {
		return isValid;
	}

	public String getReason() {
		return reason;
	}

}

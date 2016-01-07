package com.pipai.wf.battle.action;

public final class ActionVerificationResult {

	private final boolean valid;
	private final String reason;

	private static final ActionVerificationResult VALID_RESULT = new ActionVerificationResult();

	public static ActionVerificationResult validResult() {
		return VALID_RESULT;
	}

	public static ActionVerificationResult invalidResult(String reason) {
		return new ActionVerificationResult(reason);
	}

	private ActionVerificationResult() {
		valid = true;
		reason = "";
	}

	private ActionVerificationResult(String reason) {
		valid = false;
		this.reason = reason;
	}

	public boolean isValid() {
		return valid;
	}

	public String getReason() {
		return reason;
	}

}

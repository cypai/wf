package com.pipai.wf.battle.action.verification;

import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.ActionVerificationResult;

public interface ActionVerifier {

	ActionVerificationResult verify(Action action);

}

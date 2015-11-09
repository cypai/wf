package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.action.Action;

public class ActionScore {

	public final Action action;
	public final float score;

	public ActionScore(Action action, float score) {
		this.action = action;
		this.score = score;
	}

	public ActionScore compareAndReturnBetter(ActionScore other) {
		if (other == null || score >= other.score) {
			return this;
		} else {
			return other;
		}
	}

}

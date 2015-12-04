package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.action.Action;

public class ActionScore {

	private final Action action;
	private final float score;

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

	public Action getAction() {
		return action;
	}

	public float getScore() {
		return score;
	}

}

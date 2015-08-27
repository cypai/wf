package com.pipai.wf.battle.ai;

import com.pipai.wf.battle.action.Action;

public class ActionScore {
	
	public final Action action;
	public final int score;
	
	public ActionScore(Action action, int score) {
		this.action = action;
		this.score = score;
	}
	
}

package com.pipai.wf.battle.ai.utils;

import com.pipai.wf.battle.ai.Ai;

public class AiMoveRunnable implements Runnable {

	private Ai ai;

	public AiMoveRunnable(Ai ai) {
		this.ai = ai;
	}

	@Override
	public void run() {
		ai.performMove();
	}

}

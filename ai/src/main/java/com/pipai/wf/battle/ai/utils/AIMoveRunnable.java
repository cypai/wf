package com.pipai.wf.battle.ai.utils;

import com.pipai.wf.battle.ai.Ai;

public class AIMoveRunnable implements Runnable {

	private Ai ai;

	public AIMoveRunnable(Ai ai) {
		this.ai = ai;
	}

	@Override
	public void run() {
		ai.performMove();
	}

}

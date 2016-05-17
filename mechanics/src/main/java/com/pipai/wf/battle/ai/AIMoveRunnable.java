package com.pipai.wf.battle.ai;

public class AIMoveRunnable implements Runnable {

	private AI ai;

	public AIMoveRunnable(AI ai) {
		this.ai = ai;
	}

	@Override
	public void run() {
		ai.performMove();
	}

}

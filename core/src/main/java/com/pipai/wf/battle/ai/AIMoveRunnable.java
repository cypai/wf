package com.pipai.wf.battle.ai;

public class AIMoveRunnable implements Runnable {
	
	protected AI ai;
	
	public AIMoveRunnable(AI ai) {
		this.ai = ai;
	}
	
	public void run() {
		ai.performMove();
	}
	
}

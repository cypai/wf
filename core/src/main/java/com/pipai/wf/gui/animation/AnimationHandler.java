package com.pipai.wf.gui.animation;

import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.AnchoredCamera;

public abstract class AnimationHandler {

	private BattleGui gui;
	private AnchoredCamera camera;
	private AnimationObserver observer;
	private boolean finished;

	public AnimationHandler(BattleGui gui) {
		this.gui = gui;
		gui.registerAnimationHandler(this);
		camera = gui.getCamera();
		finished = false;
	}

	public BattleGui getGUI() {
		return gui;
	}

	public AnchoredCamera getCamera() {
		return camera;
	}

	public void begin(AnimationObserver observer) {
		if (finished) {
			throw new IllegalStateException("Cannot call begin on finished AnimationHandler");
		}
		this.observer = observer;
		beginAnimation();
	}

	protected abstract void beginAnimation();

	public void update() {}

	protected void finish() {
		finished = true;
		if (observer != null) {
			observer.notifyAnimationEnd(this);
		}
	}

	public boolean isFinished() {
		return finished;
	}

}

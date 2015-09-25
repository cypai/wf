package com.pipai.wf.gui.animation;

import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.AnchoredCamera;

public abstract class AnimationHandler {

	private BattleGui gui;
	private AnchoredCamera camera;
	private AnimationObserver observer;
	private boolean initialized, controlReleased, finished;

	public AnimationHandler(BattleGui gui) {
		this.gui = gui;
		camera = gui.getCamera();
		initialized = false;
		controlReleased = false;
		finished = false;
	}

	public BattleGui getGui() {
		return gui;
	}

	public AnchoredCamera getCamera() {
		return camera;
	}

	public final void begin(AnimationObserver observer) {
		if (finished) {
			throw new IllegalStateException("Cannot call begin on finished AnimationHandler");
		}
		this.observer = observer;
		initAnimation();
		initialized = true;
	}

	protected abstract void initAnimation();

	public void update() {
	}

	protected final void releaseControl() {
		validateInitialized();
		if (controlReleased) {
			return;
		}
		controlReleased = true;
		if (observer != null) {
			observer.notifyControlReleased(this);
		}
	}

	protected final void finish() {
		releaseControl();
		finished = true;
		if (observer != null) {
			observer.notifyAnimationEnd(this);
		}
	}

	private void validateInitialized() {
		if (!initialized) {
			throw new IllegalStateException(this.getClass().toString() + " was not initialized");
		}
	}

	public boolean isFinished() {
		return finished;
	}

}

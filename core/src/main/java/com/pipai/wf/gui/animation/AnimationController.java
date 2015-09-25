package com.pipai.wf.gui.animation;

import java.util.ArrayList;

public class AnimationController implements AnimationObserver {

	private ArrayList<AnimationHandler> activeAnimations, createBuffer, deleteBuffer;
	private AnimationControllerObserver observer;

	public AnimationController(AnimationControllerObserver observer) {
		this.observer = observer;
		activeAnimations = new ArrayList<>();
		createBuffer = new ArrayList<>();
		deleteBuffer = new ArrayList<>();
	}

	public void startAnimation(AnimationHandler ahandler) {
		createBuffer.add(ahandler);
	}

	public void update() {
		for (AnimationHandler ahandler : activeAnimations) {
			ahandler.update();
		}
		cleanCreateBuffer();
		cleanDeleteBuffer();
	}

	private void cleanCreateBuffer() {
		for (AnimationHandler ahandler : createBuffer) {
			activeAnimations.add(ahandler);
			ahandler.begin(this);
		}
		createBuffer.clear();
	}

	private void cleanDeleteBuffer() {
		activeAnimations.removeAll(deleteBuffer);
		deleteBuffer.clear();
	}

	@Override
	public void notifyAnimationEnd(AnimationHandler finishedHandler) {
		deleteBuffer.add(finishedHandler);
	}

	@Override
	public void notifyControlReleased(AnimationHandler finishedHandler) {
		observer.notifyControlReleased();
	}

}

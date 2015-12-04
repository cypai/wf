package com.pipai.wf.gui.animation;

public interface AnimationObserver {

	void notifyAnimationEnd(AnimationHandler finishedHandler);

	void notifyControlReleased(AnimationHandler finishedHandler);

}

package com.pipai.wf.gui.animation;

public interface AnimationObserver {

	public void notifyAnimationEnd(AnimationHandler finishedHandler);

	public void notifyControlReleased(AnimationHandler finishedHandler);

}

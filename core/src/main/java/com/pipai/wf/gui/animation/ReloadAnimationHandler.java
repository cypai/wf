package com.pipai.wf.gui.animation;

import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;

public class ReloadAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGuiObject performer;
	private boolean skipCamera;

	public ReloadAnimationHandler(BattleGui gui, BattleEvent ev, boolean skipCamera) {
		super(gui);
		performer = getGui().getAgentGUIObject(ev.getPerformer());
		this.skipCamera = skipCamera;
	}

	@Override
	protected void initAnimation() {
		if (!skipCamera) {
			getCamera().moveTo(performer.getX(), performer.getY(), this);
		}
	}

	@Override
	public void update() {
		if (skipCamera) {
			performAnimation();
			finish();
		}
	}

	private void performAnimation() {
		TemporaryText ttext = new TemporaryText(getGui(), new Vector3(performer.getX(), performer.getY(), 0), "Reload");
		getGui().createInstance(ttext);
		finish();
	}

	@Override
	public void notifyCameraMoveEnd() {
		performAnimation();
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

}

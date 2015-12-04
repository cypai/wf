package com.pipai.wf.gui.animation;

import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;

public class OverwatchAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGuiObject performer;
	private BattleEvent ev;
	private boolean skipCamera;

	public OverwatchAnimationHandler(BattleGui gui, BattleEvent ev, boolean skipCamera) {
		super(gui);
		performer = getGui().getAgentGUIObject(ev.getPerformer());
		this.ev = ev;
		this.skipCamera = skipCamera;
	}

	@Override
	protected void initAnimation() {
		if (!skipCamera) {
			getCamera().moveTo(performer.x, performer.y, this);
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
		String owText = "Overwatch";
		owText += ev.getPreparedOWName().equals("Attack") ? "" : ": " + ev.getPreparedOWName();
		TemporaryText ttext = new TemporaryText(getGui(), performer.getPosition(), owText);
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

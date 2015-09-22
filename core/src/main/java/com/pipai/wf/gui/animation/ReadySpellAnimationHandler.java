package com.pipai.wf.gui.animation;

import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGui;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGuiObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;

public class ReadySpellAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGuiObject performer;
	private BattleEvent ev;
	private boolean skipCamera;

	public ReadySpellAnimationHandler(BattleGui gui, BattleEvent ev, boolean skipCamera) {
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
		String text = (ev.getQuickened() ? "Quickened " : "Ready ") + ev.getSpell().name();
		TemporaryText ttext = new TemporaryText(getGui(), new Vector3(performer.x, performer.y, 0), text);
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

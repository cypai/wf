package com.pipai.wf.gui.animation;

import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;

public class OverwatchAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGUIObject performer;
	private BattleEvent ev;
	private boolean skipCamera;

	public OverwatchAnimationHandler(BattleGUI gui, BattleEvent ev, boolean skipCamera) {
		super(gui);
		performer = getGUI().getAgentGUIObject(ev.getPerformer());
		this.ev = ev;
		this.skipCamera = skipCamera;
	}

	@Override
	protected void beginAnimation() {
		if (skipCamera) {
			notifyCameraMoveEnd();
		} else {
			getCamera().moveTo(performer.x, performer.y);
		}
	}

	@Override
	public void notifyCameraMoveEnd() {
		String owText = "Overwatch";
		owText += (ev.getPreparedOWName().equals("Attack") ? "" : ": " + ev.getPreparedOWName());
		TemporaryText ttext = new TemporaryText(getGUI(), new Vector3(performer.x, performer.y, 0), 80, 24, owText);
		getGUI().createInstance(ttext);
		finish();
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

}
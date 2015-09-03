package com.pipai.wf.gui.animation;

import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.overlay.TemporaryText;

public class ReloadAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGUIObject performer;
	private boolean skipCamera;

	public ReloadAnimationHandler(BattleGUI gui, BattleEvent ev, boolean skipCamera) {
		super(gui);
		performer = getGUI().getAgentGUIObject(ev.getPerformer());
		this.skipCamera = skipCamera;
	}

	@Override
	protected void beginAnimation() {
		if (skipCamera) {
			notifyCameraMoveEnd();
		} else {
			getCamera().moveTo(performer.x, performer.y, this);
		}
	}

	@Override
	public void notifyCameraMoveEnd() {
		TemporaryText ttext = new TemporaryText(getGUI(), new Vector3(performer.x, performer.y, 0), 80, 24, "Reload");
		getGUI().createInstance(ttext);
		finish();
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		notifyCameraMoveEnd();
	}

}

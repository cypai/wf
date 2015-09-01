package com.pipai.wf.gui.animation;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.battle.BattleTerrainRenderer;

public class MoveAnimationHandler extends AnimationHandler implements CameraMovementObserver {

	private AgentGUIObject agent;
	private BattleEvent moveEvent;
	private LinkedList<GridPosition> path;
	private boolean noCameraFollow;

	public MoveAnimationHandler(BattleGUI gui, AgentGUIObject a, BattleEvent moveEvent, boolean noCameraFollow) {
		super(gui);
		this.agent = a;
		this.moveEvent = moveEvent;
		path = moveEvent.getPath();
		this.noCameraFollow = noCameraFollow;
	}

	@Override
	public void beginAnimation() {
		if (noCameraFollow) {
			getCamera().moveTo(agent.x, agent.y);
			notifyCameraMoveEnd();
		} else {
			getCamera().moveTo(agent.x, agent.y, this);
		}
	}

	@Override
	public void notifyCameraMoveEnd() {
		agent.animateMoveSequence(vectorizePath(path), moveEvent.getChainEvents());
	}

	private LinkedList<Vector2> vectorizePath(LinkedList<GridPosition> path) {
		LinkedList<Vector2> vectorized = new LinkedList<Vector2>();
		for (GridPosition p : path) {
			vectorized.add(BattleTerrainRenderer.centerOfGridPos(p));
		}
		return vectorized;
	}

}

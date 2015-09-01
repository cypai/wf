package com.pipai.wf.gui.animation;

import java.util.LinkedList;

import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.gui.BattleGUI;
import com.pipai.wf.gui.camera.CameraMovementObserver;
import com.pipai.wf.guiobject.battle.AgentGUIObject;
import com.pipai.wf.guiobject.battle.BattleTerrainRenderer;

public class MoveAnimationHandler extends AnimationHandler implements CameraMovementObserver, AnimationObserver {

	private AgentGUIObject agent;
	private LinkedList<GridPosition> path;
	private LinkedList<BattleEvent> chainEvents;
	private boolean noCameraFollow;

	private BulletAttackAnimationHandler owAniHandler;

	//Animation variables
	private boolean cameraDone, go;
	private LinkedList<Vector2> moveSeq;
	private Vector2 start, dest;
	private int t;	//Animation time t counter

	public MoveAnimationHandler(BattleGUI gui, AgentGUIObject a, BattleEvent moveEvent, boolean noCameraFollow) {
		super(gui);
		this.agent = a;
		path = moveEvent.getPath();
		moveSeq = vectorizePath(path);
		chainEvents = moveEvent.getChainEvents();
		this.noCameraFollow = noCameraFollow;
		go = false;
		cameraDone = false;
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
		if (!cameraDone) {
			cameraDone = true;
			go = true;
			animateNextMoveInSeq();
		}
	}

	@Override
	public void notifyCameraMoveInterrupt() {
		System.out.println("Interrupt");
		notifyCameraMoveEnd();
	}

	/**
	 * @return null if no activation, the chained BattleEvent if there is
	 */
	private BattleEvent getOWActivationEvent() {
		BattleEvent retEv = null;
		if (chainEvents.size() > 0) {
			for (BattleEvent ev : chainEvents) {
				Vector2 owtile = BattleTerrainRenderer.centerOfGridPos(ev.getTargetTile());
				if (owtile.epsilonEquals(start, 0.0001f)) {
					retEv = ev;
					break;
				}
			}
			chainEvents.remove(retEv);
		}
		return retEv;
	}

	private void animateNextMoveInSeq() {
		start = moveSeq.pollFirst();
		dest = moveSeq.peekFirst();
		t = 0;
		if (dest == null) {
			start = null;
			finish();
		}
	}

	@Override
	public void update() {
		if (go && !isFinished()) {
			t += 1;
			int time = 6;
			if (t <= time) {
				float alpha = (float)t/(float)time;
				agent.x = start.x*(1-alpha) + dest.x*(alpha);
				agent.y = start.y*(1-alpha) + dest.y*(alpha);
			}
			if (t > time) {
				BattleEvent ow = getOWActivationEvent();
				if (ow != null) {
					owAniHandler = new BulletAttackAnimationHandler(getGUI(), ow);
					owAniHandler.begin(this);
					go = false;
				} else {
					animateNextMoveInSeq();
				}
			}
		}
	}

	private LinkedList<Vector2> vectorizePath(LinkedList<GridPosition> path) {
		LinkedList<Vector2> vectorized = new LinkedList<Vector2>();
		for (GridPosition p : path) {
			vectorized.add(BattleTerrainRenderer.centerOfGridPos(p));
		}
		return vectorized;
	}

	@Override
	public void notifyAnimationEnd(AnimationHandler finishedHandler) {
		if (finishedHandler == owAniHandler) {
			if (agent.isShowingKO()) {
				finish();
			} else {
				go = true;
			}
		}
	}

}

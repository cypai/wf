package com.pipai.wf.battle.event;

import java.util.LinkedList;
import java.util.List;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.util.GridPosition;

public class MoveEvent extends BattleEvent {

	public final Agent performer;
	public final LinkedList<GridPosition> path;
	private boolean koDuringMovement;

	public MoveEvent(Agent performer, List<GridPosition> path) {
		this.performer = performer;
		this.path = new LinkedList<>(path);
	}

	@Override
	public String toString() {
		return performer.getName() + " moved with path"
				+ path.stream().map(tile -> tile.toString()).reduce("", (a, b) -> a + " " + b);
	}

}

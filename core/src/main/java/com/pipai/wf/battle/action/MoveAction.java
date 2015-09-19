package com.pipai.wf.battle.action;

import java.util.LinkedList;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.IllegalActionException;

public class MoveAction extends AlterStateAction {

	protected LinkedList<GridPosition> path;
	protected int useAP;

	public MoveAction(Agent performerAgent, LinkedList<GridPosition> path, int useAP) {
		super(performerAgent);
		this.path = path;
		this.useAP = useAP;
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		if (useAP > getPerformer().getAP()) {
			throw new IllegalActionException("AP required for movement greater than current AP");
		}
		boolean isValid = true;
		BattleEvent event = BattleEvent.moveEvent(getPerformer(), path);
		for (GridPosition pos : path) {
			if (pos.equals(getPerformer().getPosition())) {
				continue;
			}
			BattleMapCell cell = getBattleMap().getCell(pos);
			if (cell == null || !cell.isEmpty()) {
				isValid = false;
				break;
			}
		}
		if (isValid) {
			log(event);
		} else {
			throw new IllegalActionException("Move path sequence is not valid");
		}
		for (GridPosition pos : path) {
			getPerformer().setPosition(pos);
			for (Agent a : getPerformer().enemiesInRange()) {
				if (a.isOverwatching()) {
					a.activateOverwatch(getPerformer(), event, pos);
					if (getPerformer().isKO()) {
						return;
					}
				}
			}
		}
		GridPosition dest = path.peekLast();
		getPerformer().setPosition(dest);
		getPerformer().useAP(useAP);
	}

	@Override
	public String name() {
		return "Move";
	}

	@Override
	public String description() {
		return "Move to a different location";
	}

}

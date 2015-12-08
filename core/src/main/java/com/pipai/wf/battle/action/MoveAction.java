package com.pipai.wf.battle.action;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.vision.AgentVisionCalculator;
import com.pipai.wf.exception.IllegalActionException;

public class MoveAction extends AlterStateAction {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoveAction.class);

	private LinkedList<GridPosition> path;
	private int useAP;

	public MoveAction(BattleController controller, Agent performerAgent, LinkedList<GridPosition> path, int useAP) {
		super(controller, performerAgent);
		this.path = path;
		this.useAP = useAP;
	}

	@Override
	public int getAPRequired() {
		return 1;
	}

	@Override
	protected void performImpl() throws IllegalActionException {
		LOGGER.debug("Performed by '" + getPerformer().getName() + "' with path " + path + " and useAP " + useAP);
		Agent movingAgent = getPerformer();
		if (useAP > movingAgent.getAP()) {
			throw new IllegalActionException("AP required for movement greater than current AP");
		}
		BattleEvent event = BattleEvent.moveEvent(movingAgent, path);
		if (pathIsValid()) {
			logBattleEvent(event);
		} else {
			throw new IllegalActionException("Move path sequence is not valid");
		}
		AgentVisionCalculator visionCalc = new AgentVisionCalculator(getBattleMap(), getBattleConfiguration());
		for (GridPosition pos : path) {
			setAgentPosition(movingAgent, pos);
			for (Agent a : visionCalc.enemiesInRangeOf(movingAgent)) {
				if (a.isOverwatching()) {
					OverwatchableTargetedAction owAction = a.getOverwatchAction();
					setAgentPosition(movingAgent, pos);
					owAction.performOnOverwatch(event, movingAgent);
					a.clearOverwatch();
					if (movingAgent.isKO()) {
						return;
					}
				}
			}
		}
		GridPosition dest = path.peekLast();
		setAgentPosition(movingAgent, dest);
		movingAgent.useAP(useAP);
	}

	private boolean pathIsValid() {
		boolean isValid = true;
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
		return isValid;
	}

	private void setAgentPosition(Agent a, GridPosition pos) {
		BattleMapCell startCell = getBattleMap().getCell(a.getPosition());
		startCell.removeAgent();
		BattleMapCell destinationCell = getBattleMap().getCell(pos);
		destinationCell.setAgent(a);
		a.setPosition(pos);
	}

	@Override
	public String getName() {
		return "Move";
	}

	@Override
	public String getDescription() {
		return "Move to a different location";
	}

}

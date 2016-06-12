package com.pipai.wf.battle.action;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.pipai.wf.battle.BattleController;
import com.pipai.wf.battle.action.component.ApRequiredComponent;
import com.pipai.wf.battle.action.verification.ActionVerifier;
import com.pipai.wf.battle.action.verification.BaseVerifier;
import com.pipai.wf.battle.action.verification.SupplierVerifier;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.event.BattleEvent;
import com.pipai.wf.battle.event.MoveEvent;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.vision.AgentVisionCalculator;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.GridPosition;

public class MoveAction extends PerformerAction implements ApRequiredComponent {

	// private static final Logger LOGGER = LoggerFactory.getLogger(MoveAction.class);

	private ImmutableList<GridPosition> path;
	private int apRequired;

	public MoveAction(BattleController controller, Agent performerAgent, LinkedList<GridPosition> path, int useAP) {
		super(controller, performerAgent);
		this.path = ImmutableList.copyOf(path);
		apRequired = useAP;
	}

	@Override
	protected List<ActionVerifier> getVerifiers() {
		return Arrays.asList(BaseVerifier.getInstance(),
				new SupplierVerifier(this::pathIsValid, "Move path sequence is not valid"));
	}

	@Override
	public int getAPRequired() {
		return apRequired;
	}

	@Override
	protected BattleEvent performImpl() throws IllegalActionException {
		Agent movingAgent = getPerformer();
		AgentVisionCalculator visionCalc = new AgentVisionCalculator(getBattleMap(), getBattleConfiguration());
		BattleEvent event = new MoveEvent(getPerformer(), path);
		for (GridPosition pos : path) {
			setAgentPosition(movingAgent, pos);
			for (Agent a : visionCalc.enemiesInRangeOf(movingAgent)) {
				if (a.isOverwatching()) {
					OverwatchableTargetedAction owAction = a.getOverwatchAction();
					setAgentPosition(movingAgent, pos);
					owAction.performOnOverwatch(event, movingAgent);
					a.clearOverwatch();
					if (movingAgent.isKO()) {
						return event;
					}
				}
			}
		}
		GridPosition dest = path.get(path.size() - 1);
		setAgentPosition(movingAgent, dest);
		movingAgent.useAP(apRequired);
		return event;
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

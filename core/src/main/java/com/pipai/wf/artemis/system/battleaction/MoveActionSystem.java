package com.pipai.wf.artemis.system.battleaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.math.collision.Ray;
import com.pipai.wf.artemis.event.RightClickRayEvent;
import com.pipai.wf.artemis.system.BattleSystem;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.artemis.system.TileGridPositionUtils;
import com.pipai.wf.battle.action.MoveAction;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.map.MapGraph;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.util.GridPosition;

import net.mostlyoriginal.api.event.common.Subscribe;

public class MoveActionSystem extends NoProcessingSystem {

	private static final Logger LOGGER = LoggerFactory.getLogger(MoveActionSystem.class);

	private SelectedUnitSystem selectedUnitSystem;
	private BattleSystem battleSystem;

	@Subscribe
	public void processRightClickRayEvent(RightClickRayEvent event) {
		Ray ray = event.pickRay;
		GridPosition tile = TileGridPositionUtils.getIntersectedTile(ray);
		MapGraph graph = selectedUnitSystem.getSelectedMapGraph();
		Agent selectedAgent = selectedUnitSystem.getSelectedAgent();
		MoveAction move = new MoveAction(battleSystem.getBattleController(), selectedAgent,
				graph.getPath(tile), graph.apRequiredToMoveTo(tile));
		try {
			move.perform();
		} catch (IllegalActionException e) {
			LOGGER.error("Could not perform move action - context: MoveAction {}", move, e);
		}
	}

}

package com.pipai.wf.artemis.system.battleanimation;

import com.artemis.ComponentMapper;
import com.pipai.wf.artemis.components.XYZPositionComponent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.battle.AgentEntitySystem;
import com.pipai.wf.artemis.system.battle.CameraInterpolationMovementSystem;
import com.pipai.wf.artemis.system.battle.MovableTileHighlightSystem;
import com.pipai.wf.artemis.system.battle.SelectedUnitSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.event.StartTurnEvent;

import net.mostlyoriginal.api.event.common.Subscribe;

public class StartTurnAnimationHandler extends NoProcessingSystem {

	private ComponentMapper<XYZPositionComponent> mXyzPosition;

	private AgentEntitySystem agentEntitySystem;
	private MovableTileHighlightSystem tileHighlightSystem;
	private SelectedUnitSystem selectedUnitSystem;
	private CameraInterpolationMovementSystem cameraInterpolationMovementSystem;

	@Subscribe
	public void handleStartTurnEvent(StartTurnEvent event) {
		if (event.team.equals(Team.ENEMY)) {
			tileHighlightSystem.removeAllTileHighlights();
		} else {
			selectedUnitSystem.updateForSelectedAgent();
			int entityId = agentEntitySystem.getAgentEntity(selectedUnitSystem.getSelectedAgent());
			cameraInterpolationMovementSystem.beginMovingCamera(mXyzPosition.get(entityId).position);
		}
	}

}

package com.pipai.wf.artemis.system.battleanimation;

import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.battle.MovableTileHighlightSystem;
import com.pipai.wf.artemis.system.battle.SelectedUnitSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.event.StartTurnEvent;

import net.mostlyoriginal.api.event.common.Subscribe;

public class StartTurnAnimationHandler extends NoProcessingSystem {

	private MovableTileHighlightSystem tileHighlightSystem;
	private SelectedUnitSystem selectedUnitSystem;

	@Subscribe
	public void handleStartTurnEvent(StartTurnEvent event) {
		if (event.team.equals(Team.ENEMY)) {
			tileHighlightSystem.removeAllTileHighlights();
		} else {
			selectedUnitSystem.updateForSelectedAgent();
		}
	}

}

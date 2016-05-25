package com.pipai.wf.artemis.system.battleanimation;

import com.artemis.ComponentMapper;
import com.pipai.wf.artemis.components.AnchoredPositionComponent;
import com.pipai.wf.artemis.components.TextComponent;
import com.pipai.wf.artemis.components.TimedDestroyComponent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.artemis.system.TileGridPositionUtils;
import com.pipai.wf.battle.event.OverwatchEvent;

import net.mostlyoriginal.api.event.common.Subscribe;

public class OverwatchEventAnimationHandler extends NoProcessingSystem {

	private ComponentMapper<TextComponent> mText;
	private ComponentMapper<AnchoredPositionComponent> mAnchoredPosition;
	private ComponentMapper<TimedDestroyComponent> mTimedDestroy;

	private SelectedUnitSystem selectedUnitSystem;

	@Subscribe
	public void handleOverwatchEvent(OverwatchEvent event) {
		int id = world.create();
		mText.create(id).text = "Overwatch";
		mAnchoredPosition.create(id).anchor = TileGridPositionUtils
				.gridPositionToTileCenter(event.performer.getPosition());
		mTimedDestroy.create(id).time = 90;

		selectedUnitSystem.updateForSelectedAgent();
	}

}

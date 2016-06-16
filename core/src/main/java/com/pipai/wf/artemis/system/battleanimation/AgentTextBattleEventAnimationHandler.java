package com.pipai.wf.artemis.system.battleanimation;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.artemis.components.AnchoredPositionComponent;
import com.pipai.wf.artemis.components.TextComponent;
import com.pipai.wf.artemis.components.TimedDestroyComponent;
import com.pipai.wf.artemis.event.AnimationEndEvent;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.TileGridPositionUtils;
import com.pipai.wf.artemis.system.battle.CameraInterpolationMovementSystem;
import com.pipai.wf.artemis.system.battle.SelectedUnitSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.event.AgentTextBattleEvent;
import com.pipai.wf.battle.event.OverwatchEvent;
import com.pipai.wf.battle.event.ReloadEvent;

import net.mostlyoriginal.api.event.common.EventSystem;
import net.mostlyoriginal.api.event.common.Subscribe;

public class AgentTextBattleEventAnimationHandler extends NoProcessingSystem {

	private ComponentMapper<TextComponent> mText;
	private ComponentMapper<AnchoredPositionComponent> mAnchoredPosition;
	private ComponentMapper<TimedDestroyComponent> mTimedDestroy;

	private EventSystem eventSystem;

	private SelectedUnitSystem selectedUnitSystem;
	private CameraInterpolationMovementSystem cameraInterpolationMovementSystem;

	@Subscribe
	public void handleAgentTextBattleEvent(AgentTextBattleEvent event) {
		Vector3 position = TileGridPositionUtils.gridPositionToTileCenter(event.performer.getPosition());

		if (event.performer.getTeam().equals(Team.ENEMY)) {
			handleTextOnlyEvent(event.text, position, true);
		} else {
			handleTextOnlyEvent(event.text, position, false);
			selectedUnitSystem.updateForSelectedAgent();
		}
	}

	@Subscribe
	public void handleReloadEvent(ReloadEvent event) {
		Vector3 position = TileGridPositionUtils.gridPositionToTileCenter(event.performer.getPosition());

		if (event.performer.getTeam().equals(Team.ENEMY)) {
			handleTextOnlyEvent("Reload", position, true);
		} else {
			handleTextOnlyEvent("Reload", position, false);
			selectedUnitSystem.updateForSelectedAgent();
		}
	}

	@Subscribe
	public void handleOverwatchEvent(OverwatchEvent event) {
		Vector3 position = TileGridPositionUtils.gridPositionToTileCenter(event.performer.getPosition());

		if (event.performer.getTeam().equals(Team.ENEMY)) {
			handleTextOnlyEvent("Overwatch", position, true);
		} else {
			handleTextOnlyEvent("Overwatch", position, false);
			selectedUnitSystem.updateForSelectedAgent();
		}
	}

	private void handleTextOnlyEvent(String text, Vector3 position, boolean moveCameraFirst) {
		if (moveCameraFirst) {
			cameraInterpolationMovementSystem.addOneTimeCallbackOnFinish(() -> {
				showTextAtPosition(text, position);
				eventSystem.dispatch(new AnimationEndEvent());
			});
			cameraInterpolationMovementSystem.beginMovingCamera(position);
		} else {
			showTextAtPosition(text, position);
			eventSystem.dispatch(new AnimationEndEvent());
		}
	}

	public void showTextAtPosition(String text, Vector3 position) {
		int id = world.create();
		mText.create(id).text = text;
		mAnchoredPosition.create(id).anchor = position;
		mTimedDestroy.create(id).time = 90;
	}

}

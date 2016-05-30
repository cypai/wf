package com.pipai.wf.artemis.system.input;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.battle.BattleSystem;
import com.pipai.wf.artemis.system.battle.SelectedUnitSystem;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.OverwatchAction;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;
import com.pipai.wf.item.weapon.WeaponFlag;

public class BattleKeysInputProcessorSystem extends NoProcessingSystem implements InputProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(BattleKeysInputProcessorSystem.class);

	private SelectedUnitSystem selectedUnitSystem;
	private BattleSystem battleSystem;

	@Override
	public boolean keyDown(int keycode) {
		boolean processed = false;

		if (battleSystem.getBattleController().getCurrentTeam().equals(Team.PLAYER)) {
			switch (keycode) {
			case Keys.R:
				performReload();
				processed = true;
				break;
			case Keys.Y:
				performOverwatch();
				processed = true;
				break;
			default:
				break;
			}
		}
		return processed;
	}

	private void performReload() {
		try {
			new ReloadAction(battleSystem.getBattleController(),
					selectedUnitSystem.getSelectedAgent(),
					(Weapon) selectedUnitSystem.getSelectedAgent().getInventory().getItem(2))
							.perform();
		} catch (IllegalActionException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	private void performOverwatch() {
		try {
			Weapon weapon = (Weapon) selectedUnitSystem.getSelectedAgent().getInventory().getItem(2);
			if (weapon.hasFlag(WeaponFlag.OVERWATCH)) {
				Optional<OverwatchAction> action = weapon.getParticularAction(OverwatchAction.class,
						battleSystem.getBattleController(), selectedUnitSystem.getSelectedAgent());
				if (action.isPresent()) {
					action.get().perform();
				}
			}
		} catch (IllegalActionException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}

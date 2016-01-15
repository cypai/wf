package com.pipai.wf.artemis.system.input;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.pipai.wf.artemis.system.BattleSystem;
import com.pipai.wf.artemis.system.NoProcessingSystem;
import com.pipai.wf.artemis.system.SelectedUnitSystem;
import com.pipai.wf.battle.action.ReloadAction;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.item.weapon.Weapon;

public class KeysInputProcessorSystem extends NoProcessingSystem implements InputProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(KeysInputProcessorSystem.class);

	private SelectedUnitSystem selectedUnitSystem;
	private BattleSystem battleSystem;

	@Override
	public boolean keyDown(int keycode) {
		boolean processed = true;
		switch (keycode) {
		case Keys.R:
			try {
				new ReloadAction(battleSystem.getBattleController(),
						selectedUnitSystem.getSelectedAgent(),
						(Weapon) selectedUnitSystem.getSelectedAgent().getInventory().getItem(2))
								.perform();
			} catch (IllegalActionException e) {
				LOGGER.error(e.getMessage(), e);
			}
			break;
		default:
			processed = false;
			break;
		}
		return processed;
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

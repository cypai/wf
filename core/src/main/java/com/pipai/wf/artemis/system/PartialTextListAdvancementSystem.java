package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.pipai.wf.artemis.components.textbox.PartialTextComponent;
import com.pipai.wf.artemis.components.textbox.TextListAdvancementStrategyComponent;
import com.pipai.wf.artemis.components.textbox.TextListComponent;

public class PartialTextListAdvancementSystem extends IteratingSystem implements InputProcessor {

	private ComponentMapper<PartialTextComponent> mPartialText;
	private ComponentMapper<TextListAdvancementStrategyComponent> mAdvancementStrategy;
	private ComponentMapper<TextListComponent> mTextList;

	private boolean next;

	public PartialTextListAdvancementSystem() {
		super(Aspect.all(PartialTextComponent.class, TextListAdvancementStrategyComponent.class,
				TextListComponent.class));
	}

	@Override
	protected void process(int entityId) {
		PartialTextComponent cPartialText = mPartialText.get(entityId);
		TextListComponent cTextList = mTextList.get(entityId);

		if (cPartialText.currentText.length() == cPartialText.fullText.length()) {
			TextListAdvancementStrategyComponent cAdvancementStrategy = mAdvancementStrategy.get(entityId);

			if (cTextList.index + 1 < cTextList.textQueue.size()) {
				switch (cAdvancementStrategy.advancementStrategy) {
				case AUTO:
					goToNextText(cPartialText, cTextList);
					break;
				case USER_INPUT:
					if (next) {
						goToNextText(cPartialText, cTextList);
					}
					break;
				default:
					break;
				}
			} else {
				switch (cAdvancementStrategy.advancementStrategy) {
				case AUTO:
					cTextList.finishedDisplay = true;
					break;
				case USER_INPUT:
					if (next) {
						cTextList.finishedDisplay = true;
					}
					break;
				default:
					break;
				}
			}
		}
		next = false;
	}

	private static void goToNextText(PartialTextComponent cPartialText, TextListComponent cTextList) {
		cTextList.index += 1;
		cPartialText.setToText(cTextList.textQueue.get(cTextList.index));
	}

	@Override
	public boolean keyDown(int keycode) {
		boolean processed = true;
		switch (keycode) {
		case Keys.Z:
			next = true;
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
		boolean processed = false;
		switch (button) {
		case Buttons.LEFT:
			next = true;
			processed = true;
			break;
		default:
			break;
		}
		return processed;
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

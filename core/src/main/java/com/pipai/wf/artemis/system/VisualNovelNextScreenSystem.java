package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.pipai.wf.artemis.components.textbox.SwitchScreenOnTextListCompletionComponent;
import com.pipai.wf.artemis.components.textbox.TextListComponent;
import com.pipai.wf.artemis.screen.VisualNovelScreen;

public class VisualNovelNextScreenSystem extends IteratingSystem {

	private ComponentMapper<TextListComponent> mTextList;
	private ComponentMapper<SwitchScreenOnTextListCompletionComponent> mNextScreen;

	private VisualNovelScreen vnScreen;

	public VisualNovelNextScreenSystem(VisualNovelScreen vnScreen) {
		super(Aspect.all(TextListComponent.class, SwitchScreenOnTextListCompletionComponent.class));
		this.vnScreen = vnScreen;
	}

	@Override
	protected void process(int entityId) {
		TextListComponent cTextList = mTextList.get(entityId);
		SwitchScreenOnTextListCompletionComponent cNextScreen = mNextScreen.get(entityId);

		if (cTextList.finishedDisplay) {
			vnScreen.switchScreen(cNextScreen.nextScreen);
			if (cNextScreen.nextScreen instanceof InputProcessor) {
				Gdx.input.setInputProcessor((InputProcessor) cNextScreen.nextScreen);
			}
		}
	}

}

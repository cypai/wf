package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.textbox.PartialTextComponent;
import com.sun.javafx.util.Utils;

public class PartialTextBoxUpdateSystem extends IteratingSystem {

	private ComponentMapper<PartialTextComponent> mPartialText;

	public PartialTextBoxUpdateSystem() {
		super(Aspect.all(PartialTextComponent.class));
	}

	@Override
	protected void process(int entityId) {
		PartialTextComponent cPartialText = mPartialText.get(entityId);
		if (cPartialText.currentText.length() < cPartialText.fullText.length()) {
			cPartialText.timer -= 1;
			if (cPartialText.timer <= 0) {
				cPartialText.timer = cPartialText.timerSlowness;
				int end = Utils.clampMax(cPartialText.currentText.length() + cPartialText.textUpdateRate,
						cPartialText.fullText.length());
				cPartialText.currentText = cPartialText.fullText.substring(0, end);
			}
		}
	}

}

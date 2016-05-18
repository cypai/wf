package com.pipai.wf.artemis.system;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.pipai.wf.artemis.components.PartialTextComponent;
import com.pipai.wf.artemis.components.TextListComponent;
import com.pipai.wf.artemis.components.TextListIterationStrategyComponent;

public class PartialTextListIterationSystem extends IteratingSystem {

	private ComponentMapper<PartialTextComponent> mPartialText;
	private ComponentMapper<TextListIterationStrategyComponent> mIterationStrategy;
	private ComponentMapper<TextListComponent> mTextList;

	public PartialTextListIterationSystem() {
		super(Aspect.all(PartialTextComponent.class, TextListIterationStrategyComponent.class,
				TextListComponent.class));
	}

	@Override
	protected void process(int entityId) {
		PartialTextComponent cPartialText = mPartialText.get(entityId);
		TextListComponent cTextList = mTextList.get(entityId);
		if (cTextList.index + 1 < cTextList.textQueue.size()
				&& cPartialText.currentText.length() == cPartialText.fullText.length()) {
			TextListIterationStrategyComponent cIterationStrategy = mIterationStrategy.get(entityId);
			switch (cIterationStrategy.updateStrategy) {
			case AUTO:
				cTextList.index += 1;
				cPartialText.setToText(cTextList.textQueue.get(cTextList.index));
				break;
			case USER_INPUT:
				break;
			default:
				break;
			}
		}
	}

}

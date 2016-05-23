package com.pipai.wf.artemis.components.textbox;

import com.artemis.Component;

public class TextListAdvancementStrategyComponent extends Component {

	public enum TextListAdvancementStrategy {
		USER_INPUT, AUTO;
	}

	public TextListAdvancementStrategy advancementStrategy;

}

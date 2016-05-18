package com.pipai.wf.artemis.components;

import com.artemis.Component;

public class TextListIterationStrategyComponent extends Component {

	public enum TextListIterationStrategy {
		USER_INPUT, AUTO;
	}

	public TextListIterationStrategy updateStrategy;

}

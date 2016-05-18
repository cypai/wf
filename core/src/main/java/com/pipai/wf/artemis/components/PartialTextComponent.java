package com.pipai.wf.artemis.components;

import com.artemis.Component;

public class PartialTextComponent extends Component {

	public int timer;
	public int timerSlowness;
	public String fullText;
	public String currentText;
	public int textUpdateRate;

	public void setToText(String text) {
		timer = timerSlowness;
		fullText = text;
		currentText = "";
	}

}

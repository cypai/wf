package com.pipai.wf.artemis.components;

import com.artemis.Component;
import com.badlogic.gdx.math.Interpolation;
import com.pipai.wf.artemis.event.InterpolationEndEvent;

public class InterpolationComponent extends Component {

	public Interpolation interpolation;
	public int t;
	public int maxT;

	public InterpolationEndEvent onEndEvent;

}

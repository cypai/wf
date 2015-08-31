package com.pipai.wf.math;

public class LinearFunction {

	private float m, b;

	public LinearFunction(float x1, float y1, float x2, float y2) {
		m = (y2 - y1) / (x2 - x1);
		b = y1 - m * x1;
	}

	public float eval(float x) {
		return m * x + b;
	}

}

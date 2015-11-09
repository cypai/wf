package com.pipai.wf.util;

public class Alarm {

	protected int t;

	public Alarm() {
		t = -1;
	}

	public void set(int t) {
		this.t = t;
	}

	public boolean check() {
		return t == 0;
	}

	public void update() {
		if (t >= 0) {
			t -= 1;
		}
	}

}

package com.pipai.wf.util;

public class Alarm {

	private int time;

	public Alarm() {
		time = -1;
	}

	public void setTime(int t) {
		time = t;
	}

	public int getTime() {
		return time;
	}

	public boolean check() {
		return time == 0;
	}

	public void update() {
		if (time >= 0) {
			time -= 1;
		}
	}

}

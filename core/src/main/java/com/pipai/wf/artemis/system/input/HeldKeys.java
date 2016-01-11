package com.pipai.wf.artemis.system.input;

import java.util.HashMap;

public class HeldKeys {

	private HashMap<Integer, Boolean> heldKeys;

	public HeldKeys() {
		heldKeys = new HashMap<>();
	}

	public final void keyDown(int keycode) {
		heldKeys.put(keycode, true);
	}

	public final void keyUp(int keycode) {
		heldKeys.put(keycode, false);
	}

	public final boolean isDown(int keycode) {
		return Boolean.TRUE.equals(heldKeys.get(keycode));
	}

}

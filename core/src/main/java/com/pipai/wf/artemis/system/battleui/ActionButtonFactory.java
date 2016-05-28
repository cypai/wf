package com.pipai.wf.artemis.system.battleui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.google.common.collect.ImmutableMap;
import com.pipai.wf.battle.action.Action;

public final class ActionButtonFactory {

	public static final ImmutableMap<String, String> ACTION_ICONS = ImmutableMap.<String, String>builder()
			.put("Overwatch", "graphics/icons/overwatch.png")
			.build();

	private ActionButtonFactory() {
	}

	public static Button createActionButton(ButtonStyle style, Action action) {
		if (ACTION_ICONS.containsKey(action.getName())) {
			return new IconButton(style, ACTION_ICONS.get(action.getName()));
		} else {
			return null;
		}
	}

}

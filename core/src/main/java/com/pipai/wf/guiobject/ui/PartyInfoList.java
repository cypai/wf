package com.pipai.wf.guiobject.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.battle.agent.AgentState;
import com.pipai.wf.gui.GUI;

public class PartyInfoList extends UIList {
	
	protected ArrayList<UnitInfoListItem> list;
	protected float padding;
	
	public PartyInfoList(GUI gui, List<AgentState> party, float x, float y, float width, float height, Color color) {
		super(gui, x, y, width, height, color);
		padding = 4;
		list = new ArrayList<UnitInfoListItem>();
		float listy = y - padding - 1;
		for (AgentState a : party) {
			UnitInfoListItem li = new UnitInfoListItem(gui, a, x + padding + 1, listy, width - 2 - 2 * padding, 2);
			list.add(li);
			gui.createInstance(li);
			listy -= li.height + padding;
		}
	}
	
}

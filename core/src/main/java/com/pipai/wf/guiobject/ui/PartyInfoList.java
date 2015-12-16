package com.pipai.wf.guiobject.ui;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.unit.schema.UnitSchema;

public class PartyInfoList extends UIList {

	private ArrayList<UnitInfoListItem> list;
	private float padding;

	public PartyInfoList(Gui gui, List<UnitSchema> party, float x, float y, float width, float height, Color color) {
		super(gui, x, y, width, height, color);
		padding = 4;
		list = new ArrayList<UnitInfoListItem>();
		float listy = y - padding - 1;
		for (UnitSchema a : party) {
			UnitInfoListItem li = new UnitInfoListItem(gui, a, x + padding + 1, listy, width - 2 - 2 * padding, 3);
			list.add(li);
			gui.createInstance(li);
			listy -= li.getHeight() + padding;
		}
	}

}

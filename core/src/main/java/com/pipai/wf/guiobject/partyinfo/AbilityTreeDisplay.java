package com.pipai.wf.guiobject.partyinfo;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.guiobject.GuiObject;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.XYPositioned;
import com.pipai.wf.unit.abilitytree.AbilityTree;
import com.pipai.wf.unit.abilitytree.AbilityTreeNode;

public class AbilityTreeDisplay extends GuiObject implements GuiRenderable, XYPositioned {

	private Vector2 position;
	private ArrayList<AbilityTreeNodeDisplay> nodeInstances;

	public AbilityTreeDisplay(Gui gui, AbilityTree abilityTree, float x, float y) {
		super(gui);
		position = new Vector2(x, y);
		nodeInstances = new ArrayList<>();
		for (int level = 1; level <= abilityTree.getHeight(); level++) {
			List<AbilityTreeNode> nodes = abilityTree.getAbilitiesAtHeight(level);
			float xVal = x;
			for (AbilityTreeNode node : nodes) {
				nodeInstances.add(new AbilityTreeNodeDisplay(node, xVal, y - AbilityTreeNodeDisplay.HEIGHT * (level - 1)));
				xVal += AbilityTreeNodeDisplay.HEIGHT;
			}
		}
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		for (AbilityTreeNodeDisplay node : nodeInstances) {
			node.render(batch);
		}
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

}

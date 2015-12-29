package com.pipai.wf.guiobject.partyinfo;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.pipai.wf.gui.BatchHelper;
import com.pipai.wf.guiobject.GuiRenderable;
import com.pipai.wf.guiobject.XYPositioned;
import com.pipai.wf.unit.abilitytree.AbilityTreeNode;

public class AbilityTreeNodeDisplay implements GuiRenderable, XYPositioned {

	public static final int HEIGHT = 32;

	private Vector2 position;
	private AbilityTreeNode node;

	public AbilityTreeNodeDisplay(AbilityTreeNode node, float x, float y) {
		position = new Vector2(x, y);
		this.node = node;
	}

	@Override
	public int renderPriority() {
		return 0;
	}

	@Override
	public void render(BatchHelper batch) {
		ShapeRenderer shapeRenderer = batch.getShapeRenderer();
		shapeRenderer.begin(ShapeType.Line);
		if (node.isTaken()) {
			shapeRenderer.setColor(Color.WHITE);
		} else {
			shapeRenderer.setColor(Color.GRAY);
		}
		shapeRenderer.rect(position.x, position.y, HEIGHT, HEIGHT);
		shapeRenderer.end();
	}

	@Override
	public Vector2 getPosition() {
		return position;
	}

}

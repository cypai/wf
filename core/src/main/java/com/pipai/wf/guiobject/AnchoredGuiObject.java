package com.pipai.wf.guiobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.gui.Gui;
import com.pipai.wf.util.RayMapper;

public abstract class AnchoredGuiObject extends GuiObject implements GuiRenderable {

	private Vector3 anchorPoint;
	private Vector2 screenPosition;
	private final RayMapper mapper;

	public AnchoredGuiObject(Gui gui, RayMapper mapper, Vector3 anchorPoint) {
		super(gui);
		this.anchorPoint = anchorPoint;
		this.mapper = mapper;
	}

	@Override
	public void update() {
		screenPosition = mapper.pointToScreen(anchorPoint.cpy());
	}

	public Vector3 getAnchorPoint() {
		return anchorPoint;
	}

	public void setAnchorPoint(float x, float y, float z) {
		anchorPoint.set(x, y, z);
	}

	public Vector2 getScreenPosition() {
		return screenPosition.cpy();
	}

	public float getScreenX() {
		return screenPosition.x;
	}

	public float getScreenY() {
		return screenPosition.y;
	}

	public RayMapper getMapper() {
		return mapper;
	}

}

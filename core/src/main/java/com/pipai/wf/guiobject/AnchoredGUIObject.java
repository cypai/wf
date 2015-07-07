package com.pipai.wf.guiobject;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.gui.GUI;
import com.pipai.wf.util.RayMapper;

public abstract class AnchoredGUIObject extends GUIObject implements Renderable {
	
	protected Vector3 anchorPoint;
	protected Vector2 screenPosition;
	protected final RayMapper mapper;
	
	public AnchoredGUIObject(GUI gui, RayMapper mapper, Vector3 anchorPoint) {
		super(gui);
		this.anchorPoint = anchorPoint;
		this.mapper = mapper;
	}
	
	@Override
	public void update() {
		screenPosition = mapper.pointToScreen(anchorPoint.cpy());
	}
	
}

package com.pipai.wf.guiobject;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.pipai.wf.gui.GUI;

public abstract class AnchoredGUIObject extends GUIObject implements Renderable {
	
	protected Vector3 anchorPoint, screenPosition;
	protected final Camera camera;
	
	public AnchoredGUIObject(GUI gui, Camera camera, Vector3 anchorPoint) {
		super(gui);
		this.anchorPoint = anchorPoint;
		this.camera = camera;
	}
	
	@Override
	public void update() {
		screenPosition = camera.project(anchorPoint.cpy());
	}
	
}

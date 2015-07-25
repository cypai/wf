package com.pipai.wf.guiobject;

import com.pipai.wf.gui.GUI;

public abstract class GUIObject {
	
	private static int nextID = 0;
	
	protected GUI gui;
	protected int id;
	
	public GUIObject(GUI gui) {
		this.gui = gui;
		this.id = GUIObject.nextID;
		GUIObject.nextID += 1;
	}
	
	public int getID() { return id; }
	
	public void update() {}
	
	public void dispose() {}
	
}

package com.pipai.wf.renderable.gui;

public class ActionClickable implements LeftClickable {
	
	protected int screenX, screenY, size;
	
	public ActionClickable(int screenX, int screenY, int size) {
		this.screenX = screenX;
		this.screenY = screenY;
		this.size = size;
	}
	
	public void onLeftClick(int gameX, int gameY) {
		
	}
	
}
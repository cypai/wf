package com.pipai.wf.guiobject;

import com.pipai.wf.gui.BatchHelper;

public interface Renderable {
	
	public int renderPriority();
	
	public void render(BatchHelper batch);
	
}
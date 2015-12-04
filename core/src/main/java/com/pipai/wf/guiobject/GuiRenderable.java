package com.pipai.wf.guiobject;

import com.pipai.wf.gui.BatchHelper;

public interface GuiRenderable {

	public int renderPriority();

	public void render(BatchHelper batch);

}

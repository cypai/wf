package com.pipai.wf.guiobject;

import com.pipai.wf.gui.BatchHelper;

public interface GuiRenderable {

	int renderPriority();

	void render(BatchHelper batch);

}

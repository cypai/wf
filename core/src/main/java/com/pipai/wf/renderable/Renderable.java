package com.pipai.wf.renderable;

public interface Renderable {
	
	public int renderPriority();
	
	public void render(BatchHelper batch);
	
}
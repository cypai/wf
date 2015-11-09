package com.pipai.wf.gui.util;

import java.util.ArrayList;

public class GuiObjectBuffers<T> {

	private ArrayList<T> guiObjects, createBuffer, deleteBuffer;

	public GuiObjectBuffers() {
		guiObjects = new ArrayList<>();
		createBuffer = new ArrayList<>();
		deleteBuffer = new ArrayList<>();
	}

	public void addToCreateBuffer(T obj) {
		createBuffer.add(obj);
	}

	public void addToDeleteBuffer(T obj) {
		deleteBuffer.add(obj);
	}

	public void flushCreateBuffer() {
		if (!createBuffer.isEmpty()) {
			guiObjects.addAll(createBuffer);
			createBuffer.clear();
		}
	}

	public void flushDeleteBuffer() {
		if (!deleteBuffer.isEmpty()) {
			guiObjects.removeAll(deleteBuffer);
			deleteBuffer.clear();
		}
	}

	public ArrayList<T> getAll() {
		return guiObjects;
	}

}

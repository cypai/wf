package com.pipai.wf.guiobject;

import java.util.LinkedList;

import com.pipai.wf.gui.GUI;

public abstract class GUIObject {

	private static int nextID = 0;

	protected GUI gui;
	protected int id;

	private LinkedList<GUIObjectDestroyEventObserver> destroyEvObservers;

	public GUIObject(GUI gui) {
		this.gui = gui;
		this.id = GUIObject.nextID;
		GUIObject.nextID += 1;
	}

	public int getID() { return id; }

	public void update() {}

	public void dispose() {}

	public void registerDestroyEventObserver(GUIObjectDestroyEventObserver o) {
		if (destroyEvObservers == null) {
			destroyEvObservers = new LinkedList<GUIObjectDestroyEventObserver>();
		}
		destroyEvObservers.add(o);
	}

	public void destroy() {
		gui.deleteInstance(this);
		if (destroyEvObservers != null) {
			for (GUIObjectDestroyEventObserver o : destroyEvObservers) {
				o.notifyOfDestroyEvent(this);
			}
		}
	}

}

package com.pipai.wf.guiobject;

import java.util.LinkedList;

import com.pipai.wf.gui.Gui;

public abstract class GuiObject {

	private static int nextID = 0;

	protected Gui gui;
	protected int id;

	private LinkedList<GuiObjectDestroyEventObserver> destroyEvObservers;

	public GuiObject(Gui gui) {
		this.gui = gui;
		id = GuiObject.nextID;
		GuiObject.nextID += 1;
	}

	public int getID() {
		return id;
	}

	public void update() {
	}

	public void dispose() {
	}

	public void registerDestroyEventObserver(GuiObjectDestroyEventObserver o) {
		if (destroyEvObservers == null) {
			destroyEvObservers = new LinkedList<GuiObjectDestroyEventObserver>();
		}
		destroyEvObservers.add(o);
	}

	public void destroy() {
		gui.deleteInstance(this);
		if (destroyEvObservers != null) {
			for (GuiObjectDestroyEventObserver o : destroyEvObservers) {
				o.notifyOfDestroyEvent(this);
			}
		}
	}

}

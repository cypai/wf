package com.pipai.wf.battle.event;

import java.util.LinkedList;

import net.mostlyoriginal.api.event.common.Event;

/*
 * For use in BattleLog and reporting back to a GUI on battle events/actions/outcomes
 */
public abstract class BattleEvent implements Event {

	private String actionName;

	private LinkedList<BattleEvent> chainEvents;

	public BattleEvent() {
		chainEvents = new LinkedList<BattleEvent>();
		actionName = "";
	}

	@SuppressWarnings("unchecked")
	public LinkedList<BattleEvent> getChainEvents() {
		return (LinkedList<BattleEvent>) chainEvents.clone();
	}

	public void addChainEvent(BattleEvent event) {
		chainEvents.add(event);
	}

	/**
	 * Returns a special "name" for this particular action
	 */
	public String getActionName() {
		return actionName;
	}

	/**
	 * Should only be called during the build. Sets a special "name" to be indicated on the screen. Returns itself for
	 * chaining
	 * 
	 * @return itself for chaining
	 */
	public BattleEvent withActionName(String actionName) {
		if (hasActionName()) {
			throw new UnsupportedOperationException("Cannot set actionName in BattleEvent after initialization");
		}
		this.actionName = actionName;
		return this;
	}

	public boolean hasActionName() {
		return !actionName.isEmpty();
	}

	@Override
	public abstract String toString();

}

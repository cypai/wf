package com.pipai.wf.battle.log;

import java.util.LinkedList;

import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.map.GridPosition;

/*
 * For use in BattleLog and reporting back to a GUI on battle events/actions/outcomes
 */
public class BattleEvent {
	
	public static enum Type {
		MOVE, ATTACK, OVERWATCH, OVERWATCH_ACTIVATION;
	}
	
	private Type type;
	private Agent performer, target;
	private LinkedList<GridPosition> path;
	private Attack atk;
	private boolean hit;
	private int damageRoll;
	private LinkedList<BattleEvent> chainedEvents;
	
	public static BattleEvent moveEvent(Agent performer, LinkedList<GridPosition> path) {
		BattleEvent event = new BattleEvent(Type.MOVE, performer, null);
		event.path = path;
		return event;
	}
	
	public static BattleEvent attackEvent(Agent performer, Agent target, Attack atk, boolean hit, int damage) {
		BattleEvent event = new BattleEvent(Type.ATTACK, performer, target);
		event.atk = atk;
		event.hit = hit;
		event.damageRoll = damage;
		return event;
	}
	
	public static BattleEvent overwatchEvent(Agent performer, Attack atk) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH, performer, null);
		event.atk = atk;
		return event;
	}
	
	public static BattleEvent overwatchActivationEvent(Agent performer, Agent target, Attack atk, boolean hit, int damage) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH_ACTIVATION, performer, target);
		event.atk = atk;
		event.hit = hit;
		event.damageRoll = damage;
		return event;
	}
	
	private BattleEvent(Type type, Agent performer, Agent target) {
		this.type = type;
		this.performer = performer;
		this.target = target;
		this.chainedEvents = new LinkedList<BattleEvent>();
	}
	
	public Type getType() { return this.type; }
	public Agent getPerformer() { return this.performer; }
	public Agent getTarget() { return this.target; }
	public LinkedList<GridPosition> getPath() { return this.path; }
	public Attack getAttack() { return this.atk; }
	public boolean isHit() { return this.hit; }
	public int getDamageRoll() { return this.damageRoll; }
	
	public int getNumChainEvents() { return this.chainedEvents.size(); }
	
	@SuppressWarnings("unchecked")
	public LinkedList<BattleEvent> getChainEvents() {
		return (LinkedList<BattleEvent>) this.chainedEvents.clone();
	}
	
	public void addChainEvent(BattleEvent event) {
		this.chainedEvents.add(event);
	}
	
	@Override
	public String toString() {
		String returnStr = type.toString() + ": ";
		switch (type) {
		case ATTACK:
			returnStr += atk.getClass() + " ";
			returnStr += String.valueOf(hit) + " ";
			returnStr += damageRoll;
			break;
		default:
			break;
		}
		return returnStr;
	}
	
}
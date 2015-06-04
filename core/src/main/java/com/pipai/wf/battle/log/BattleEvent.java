package com.pipai.wf.battle.log;

import java.util.LinkedList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.Spell;

/*
 * For use in BattleLog and reporting back to a GUI on battle events/actions/outcomes
 */
public class BattleEvent {
	
	public static enum Type {
		MOVE, ATTACK, READY, CAST_TARGET, OVERWATCH, OVERWATCH_ACTIVATION, RELOAD, START_TURN;
	}
	
	private Team team;
	private Type type;
	private Agent performer, target;
	private LinkedList<GridPosition> path;
	private Attack atk;
	private Spell spell;
	private boolean hit;
	private int damageRoll;
	private LinkedList<BattleEvent> chainedEvents;
	private GridPosition targetTile;
	
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
	
	public static BattleEvent readySpellEvent(Agent performer, Spell spell) {
		BattleEvent event = new BattleEvent(Type.READY, performer, null);
		event.spell = spell;
		return event;
	}
	
	public static BattleEvent castTargetEvent(Agent performer, Agent target, Spell spell, boolean hit, int damage) {
		BattleEvent event = new BattleEvent(Type.CAST_TARGET, performer, target);
		event.spell = spell;
		event.hit = hit;
		event.damageRoll = damage;
		return event;
	}
	
	public static BattleEvent overwatchEvent(Agent performer, Attack atk) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH, performer, null);
		event.atk = atk;
		return event;
	}
	
	public static BattleEvent overwatchActivationEvent(Agent performer, Agent target, Attack atk, GridPosition targetTile, boolean hit, int damage) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH_ACTIVATION, performer, target);
		event.atk = atk;
		event.hit = hit;
		event.damageRoll = damage;
		event.targetTile = targetTile;
		return event;
	}
	
	public static BattleEvent reloadEvent(Agent performer) {
		BattleEvent event = new BattleEvent(Type.RELOAD, performer, null);
		return event;
	}
	
	public static BattleEvent startTurnEvent(Team team) {
		BattleEvent event = new BattleEvent(Type.START_TURN, null, null);
		event.team = team;
		return event;
	}
	
	private BattleEvent(Type type, Agent performer, Agent target) {
		this.type = type;
		this.performer = performer;
		this.target = target;
		this.chainedEvents = new LinkedList<BattleEvent>();
	}
	
	public Team getTeam() { return this.team; }
	public Type getType() { return this.type; }
	public Agent getPerformer() { return this.performer; }
	public Agent getTarget() { return this.target; }
	public LinkedList<GridPosition> getPath() { return this.path; }
	public Attack getAttack() { return this.atk; }
	public Spell getSpell() { return this.spell; }
	public boolean isHit() { return this.hit; }
	public int getDamageRoll() { return this.damageRoll; }
	public GridPosition getTargetTile() { return this.targetTile; }
	
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
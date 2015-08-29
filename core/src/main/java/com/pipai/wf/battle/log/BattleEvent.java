package com.pipai.wf.battle.log;

import java.util.LinkedList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.attack.Attack;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.Weapon;

/*
 * For use in BattleLog and reporting back to a GUI on battle events/actions/outcomes
 */
public class BattleEvent {
	
	public static enum Type {
		MOVE, RANGED_WEAPON_ATTACK, ATTACK, READY, CAST_TARGET, OVERWATCH, OVERWATCH_ACTIVATION, RELOAD, SWITCH_WEAPON, START_TURN;
	}
	
	private Team team;
	private Type type;
	private Agent performer, target;
	private LinkedList<GridPosition> path;
	private Attack atk;
	private Weapon wpn;
	private Spell spell;
	private DamageResult dmgResult;
	private LinkedList<BattleEvent> chainedEvents;
	private GridPosition targetTile;
	
	public static BattleEvent moveEvent(Agent performer, LinkedList<GridPosition> path) {
		BattleEvent event = new BattleEvent(Type.MOVE, performer, null);
		event.path = path;
		return event;
	}
	
	public static BattleEvent rangedWeaponAttackEvent(Agent performer, Agent target, Weapon weapon, DamageResult dmgResult) {
		BattleEvent event = new BattleEvent(Type.RANGED_WEAPON_ATTACK, performer, target);
		event.wpn = weapon;
		event.dmgResult = dmgResult;
		return event;
	}
	
	public static BattleEvent attackEvent(Agent performer, Agent target, Attack atk, DamageResult dmgResult) {
		BattleEvent event = new BattleEvent(Type.ATTACK, performer, target);
		event.atk = atk;
		event.dmgResult = dmgResult;
		return event;
	}
	
	public static BattleEvent readySpellEvent(Agent performer, Spell spell) {
		BattleEvent event = new BattleEvent(Type.READY, performer, null);
		event.spell = spell;
		return event;
	}
	
	public static BattleEvent castTargetEvent(Agent performer, Agent target, Spell spell, DamageResult dmgResult) {
		BattleEvent event = new BattleEvent(Type.CAST_TARGET, performer, target);
		event.spell = spell;
		event.dmgResult = dmgResult;
		return event;
	}
	
	public static BattleEvent overwatchEvent(Agent performer, Attack atk) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH, performer, null);
		event.atk = atk;
		return event;
	}
	
	public static BattleEvent overwatchActivationEvent(Agent performer, Agent target, Attack atk, GridPosition targetTile, DamageResult dmgResult) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH_ACTIVATION, performer, target);
		event.atk = atk;
		event.dmgResult = dmgResult;
		event.targetTile = targetTile;
		return event;
	}
	
	public static BattleEvent reloadEvent(Agent performer) {
		BattleEvent event = new BattleEvent(Type.RELOAD, performer, null);
		return event;
	}
	
	public static BattleEvent switchWeaponEvent(Agent performer) {
		BattleEvent event = new BattleEvent(Type.SWITCH_WEAPON, performer, null);
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
	public Weapon getWeapon() { return this.wpn; }
	public Attack getAttack() { return this.atk; }
	public Spell getSpell() { return this.spell; }
	public DamageResult getDamageResult() { return this.dmgResult; }
	public boolean isHit() { return this.dmgResult.hit; }
	public boolean isCrit() { return this.dmgResult.crit; }
	public int getDamage() { return this.dmgResult.damage; }
	public int getDamageReduction() { return this.dmgResult.damageReduction; }
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
			returnStr += String.valueOf(dmgResult.hit) + " ";
			returnStr += String.valueOf(dmgResult.crit) + " ";
			returnStr += String.valueOf(dmgResult.damage)+ " ";
			returnStr += String.valueOf(dmgResult.damageReduction);
			break;
		default:
			break;
		}
		return returnStr;
	}
	
}
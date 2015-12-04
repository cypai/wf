package com.pipai.wf.battle.log;

import java.util.LinkedList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.TargetedAction;
import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.agent.Agent;
import com.pipai.wf.battle.damage.DamageResult;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.Weapon;

/*
 * For use in BattleLog and reporting back to a GUI on battle events/actions/outcomes
 */
public final class BattleEvent {

	public enum Type {
		MOVE, RANGED_WEAPON_ATTACK, ATTACK, READY, CAST_TARGET, OVERWATCH, OVERWATCH_ACTIVATION, RELOAD, SWITCH_WEAPON, TARGETED_ACTION, START_TURN;
	}

	private Team team;
	private Type type;
	private Agent performer, target;
	private LinkedList<GridPosition> path;
	private Weapon wpn;
	private Spell spell;
	private DamageResult dmgResult;
	private LinkedList<BattleEvent> chainedEvents;
	private GridPosition targetTile;
	private String actionName;
	private TargetedAction targetedAction;
	private TargetedWithAccuracyActionOWCapable owActivatedAction;
	private boolean quickenFlag;

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

	public static BattleEvent readySpellEvent(Agent performer, Spell spell, boolean quickened) {
		BattleEvent event = new BattleEvent(Type.READY, performer, null);
		event.spell = spell;
		event.quickenFlag = quickened;
		return event;
	}

	public static BattleEvent castTargetEvent(Agent performer, Agent target, Spell spell, DamageResult dmgResult) {
		BattleEvent event = new BattleEvent(Type.CAST_TARGET, performer, target);
		event.spell = spell;
		event.dmgResult = dmgResult;
		return event;
	}

	public static BattleEvent overwatchEvent(Agent performer, String actionName) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH, performer, null);
		event.actionName = actionName;
		return event;
	}

	public static BattleEvent overwatchActivationEvent(Agent performer, Agent target, TargetedWithAccuracyActionOWCapable action, GridPosition targetTile, DamageResult dmgResult) {
		BattleEvent event = new BattleEvent(Type.OVERWATCH_ACTIVATION, performer, target);
		event.owActivatedAction = action;
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

	public static BattleEvent targetedActionEvent(Agent performer, Agent target, TargetedAction action) {
		BattleEvent event = new BattleEvent(Type.TARGETED_ACTION, performer, target);
		event.targetedAction = action;
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
		chainedEvents = new LinkedList<BattleEvent>();
	}

	public Team getTeam() {
		return team;
	}

	public Type getType() {
		return type;
	}

	public Agent getPerformer() {
		return performer;
	}

	public Agent getTarget() {
		return target;
	}

	public LinkedList<GridPosition> getPath() {
		return path;
	}

	public Weapon getWeapon() {
		return wpn;
	}

	public String getActionName() {
		return actionName;
	}

	public String getPreparedOWName() {
		return actionName;
	}

	public TargetedWithAccuracyActionOWCapable getActivatedOWAction() {
		return owActivatedAction;
	}

	public TargetedAction getTargetedAction() {
		return targetedAction;
	}

	public Spell getSpell() {
		return spell;
	}

	public DamageResult getDamageResult() {
		return dmgResult;
	}

	public boolean isHit() {
		return dmgResult.hit;
	}

	public boolean isCrit() {
		return dmgResult.crit;
	}

	public int getDamage() {
		return dmgResult.damage;
	}

	public int getDamageReduction() {
		return dmgResult.damageReduction;
	}

	public GridPosition getTargetTile() {
		return targetTile;
	}

	public boolean getQuickened() {
		return quickenFlag;
	}

	public int getNumChainEvents() {
		return chainedEvents.size();
	}

	@SuppressWarnings("unchecked")
	public LinkedList<BattleEvent> getChainEvents() {
		return (LinkedList<BattleEvent>) chainedEvents.clone();
	}

	public void addChainEvent(BattleEvent event) {
		chainedEvents.add(event);
	}

}

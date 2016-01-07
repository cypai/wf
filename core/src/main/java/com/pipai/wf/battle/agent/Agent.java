package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.OverwatchableTargetedAction;
import com.pipai.wf.battle.effect.StatusEffect;
import com.pipai.wf.battle.effect.StatusEffectList;
import com.pipai.wf.battle.effect.SuppressedStatusEffect;
import com.pipai.wf.battle.inventory.AgentInventory;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.exception.NoRegisteredAgentException;
import com.pipai.wf.item.armor.Armor;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.misc.HasBasicStats;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.spell.Spell;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.unit.ability.component.SpellAbilityComponent;
import com.pipai.wf.unit.schema.UnitSchema;

// TODO: Make this a stupid data structure class
public class Agent implements HasName, HasBasicStats {

	private Team team;
	private BasicStats basicStats;
	private String name;
	private State state;
	private GridPosition position;
	private OverwatchableTargetedAction overwatchAction;
	private AbilityList abilities;
	private StatusEffectList statusEffects;

	private AgentInventory inventory;

	private int level;
	private int expGiven;
	private int exp;

	public Agent(String name, BasicStats basicStats) {
		this.basicStats = basicStats;
		inventory = new AgentInventory(3);
		statusEffects = new StatusEffectList();
		abilities = new AbilityList();
		abilities.registerToAgent(this);
	}

	public Agent(UnitSchema schema) {
		name = schema.getName();
		basicStats = schema.getBasicStats();
		abilities = schema.getAbilities().deepCopy();
		abilities.registerToAgent(this);
		level = schema.getLevel();
		expGiven = schema.getExpGiven();
		exp = schema.getExp();
		inventory = schema.getInventory().deepCopy();
		statusEffects = new StatusEffectList();
	}

	@Override
	public BasicStats getBasicStats() {
		return basicStats;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public AgentInventory getInventory() {
		return inventory;
	}

	public void setAP(int ap) {
		basicStats.setAP(ap);
	}

	public void useAP(int ap) {
		int temp = getAP() - ap;
		temp = temp < 0 ? 0 : temp;
		basicStats.setAP(temp);
	}

	public void setHP(int hp) {
		if (hp <= 0) {
			basicStats.setHP(0);
			state = State.KO;
		} else if (hp > basicStats.getMaxHP()) {
			basicStats.setMaxHP(basicStats.getMaxHP());
		} else {
			basicStats.setHP(hp);
		}
	}

	public void takeDamage(int amt) {
		int pierceDmg = inventory.isEquippingArmor() ? inventory.getEquippedArmor().takeDamage(amt) : amt;
		setHP(getHP() - pierceDmg);
	}

	public void heal(int amt) {
		setHP(getHP() + amt);
	}

	public void setMP(int mp) {
		basicStats.setMP(mp);
	}

	public void useMP(int mp) {
		basicStats.setMP(basicStats.getMP() - mp);
	}

	public int getEffectiveMobility() {
		return getMobility() + statusEffects.totalMobilityModifier();
	}

	public Armor getEquippedArmor() {
		return inventory.getEquippedArmor();
	}

	public boolean isKO() {
		return state == State.KO;
	}

	public boolean isOverwatching() {
		return state == State.OVERWATCH;
	}

	public AbilityList getAbilities() {
		return abilities;
	}

	public Ability getAbility(Class<? extends Ability> abilityClass) {
		for (Ability a : abilities) {
			if (abilityClass.isInstance(a)) {
				return a;
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	public ArrayList<Spell> getSpellList() {
		ArrayList<Spell> l = new ArrayList<>();
		for (Ability a : abilities) {
			if (a instanceof SpellAbilityComponent) {
				l.add(((SpellAbilityComponent) a).grantedSpell());
			}
		}
		return l;
	}

	public void inflictStatus(StatusEffect se) {
		statusEffects.add(se);
	}

	public StatusEffectList getStatusEffects() {
		return statusEffects;
	}

	public GridPosition getPosition() {
		return new GridPosition(position);
	}

	public void setPosition(GridPosition pos) {
		position = pos;
	}

	public float getDistanceFrom(Agent other) {
		return (float) getPosition().distance(other.getPosition());
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void onTurnBegin() {
		basicStats.setAP(basicStats.getMaxAP());
		if (!isKO()) {
			state = State.NEUTRAL;
		}
	}

	public void onRoundEnd() {
		try {
			abilities.onRoundEnd();
		} catch (NoRegisteredAgentException e) {
			throw new IllegalStateException(e);
		}
		statusEffects.onRoundEnd();
	}

	public void onAction(Action action) {
		statusEffects.onAction(action);
	}

	public void setOverwatch(OverwatchableTargetedAction owAction) {
		overwatchAction = owAction;
		state = State.OVERWATCH;
		setAP(0);
	}

	public OverwatchableTargetedAction getOverwatchAction() {
		return overwatchAction;
	}

	public void clearOverwatch() {
		overwatchAction = null;
		state = State.NEUTRAL;
	}

	public void suppressOther(Agent other) {
		state = State.SUPPRESSING;
		other.inflictStatus(new SuppressedStatusEffect(other));
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public void giveExp(int exp) {
		this.exp += exp;
	}

	public int getExpGiven() {
		return expGiven;
	}

}

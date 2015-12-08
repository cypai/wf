package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.OverwatchableTargetedAction;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.effect.StatusEffect;
import com.pipai.wf.battle.effect.StatusEffectList;
import com.pipai.wf.battle.effect.SuppressedStatusEffect;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.NoRegisteredAgentException;
import com.pipai.wf.misc.BasicStats;
import com.pipai.wf.misc.HasBasicStats;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;

// TODO: Make this a stupid data structure class
public class Agent implements HasName, HasBasicStats {

	public enum State {
		NEUTRAL, KO, OVERWATCH, SUPPRESSING
	};

	private Team team;
	private BasicStats basicStats;
	private String name;
	private State state;
	private ArrayList<Weapon> weapons;
	private int weaponIndex;
	private Armor armor;
	private GridPosition position;
	private OverwatchableTargetedAction overwatchAction;
	private AbilityList innateAbilities;
	private StatusEffectList statusEffects;

	private int expGiven;
	private int exp;

	public Agent(AgentState state) {
		team = state.getTeam();
		position = state.getPosition();
		basicStats = state.getBasicStats().clone();
		weapons = state.getWeapons();
		weaponIndex = 0;
		armor = state.getArmor();
		innateAbilities = state.getAbilities().clone();
		innateAbilities.registerToAgent(this);
		name = state.getName();
		statusEffects = new StatusEffectList();
		exp = state.getExp();
		expGiven = state.getExpGiven();
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
		int pierceDmg = armor.takeDamage(amt);
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

	public Weapon getCurrentWeapon() {
		if (weapons.size() == 0) {
			return null;
		}
		return weapons.get(weaponIndex);
	}

	public Armor getArmor() {
		return armor;
	}

	public boolean isKO() {
		return state == State.KO;
	}

	public boolean isOverwatching() {
		return state == State.OVERWATCH;
	}

	public AbilityList getAbilities() {
		AbilityList allAbilities = innateAbilities.clone();
		allAbilities.add(getWeaponGrantedAbilities());
		return allAbilities;
	}

	public AbilityList getInnateAbilities() {
		return innateAbilities;
	}

	public AbilityList getWeaponGrantedAbilities() {
		if (getCurrentWeapon() != null) {
			return getCurrentWeapon().getGrantedAbilities();
		}
		return new AbilityList();
	}

	public Ability getAbility(Class<? extends Ability> abilityClass) {
		for (Ability a : innateAbilities) {
			if (abilityClass.isInstance(a)) {
				return a;
			}
		}
		for (Ability a : getCurrentWeapon().getGrantedAbilities()) {
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

	@SuppressWarnings("unchecked")
	public ArrayList<Weapon> getWeapons() {
		return (ArrayList<Weapon>) weapons.clone();
	}

	public ArrayList<Spell> getSpellList() {
		ArrayList<Spell> l = new ArrayList<>();
		for (Ability a : innateAbilities) {
			if (a.grantsSpell()) {
				l.add(a.grantedSpell());
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

	public void onTurnBegin() {
		basicStats.setAP(basicStats.getMaxAP());
		if (!isKO()) {
			state = State.NEUTRAL;
		}
	}

	public void onTurnEnd() {
		decrementCooldowns();
	}

	public void onRoundEnd() {
		try {
			innateAbilities.onRoundEnd();
		} catch (NoRegisteredAgentException e) {
			throw new IllegalStateException(e);
		}
		statusEffects.onRoundEnd();
	}

	public void onAction(Action action) {
		statusEffects.onAction(action);
	}

	public void decrementCooldowns() {
		for (Ability a : getInnateAbilities()) {
			if (a.onCooldown()) {
				a.decrementCooldown();
			}
		}

		for (Ability a : getWeaponGrantedAbilities()) {
			if (a.onCooldown()) {
				a.decrementCooldown();
			}
		}
	}

	public void switchWeapon() {
		weaponIndex += 1;
		if (weaponIndex == weapons.size()) {
			weaponIndex = 0;
		}
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

package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.effect.StatusEffect;
import com.pipai.wf.battle.effect.StatusEffectList;
import com.pipai.wf.battle.effect.SuppressedStatusEffect;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.overwatch.OverwatchActivatedActionSchema;
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
	private BasicStats stats;
	private String name;
	private State state;
	private ArrayList<Weapon> weapons;
	private int weaponIndex;
	private Armor armor;
	private GridPosition position;
	private OverwatchActivatedActionSchema owAction;
	private AbilityList abilities;
	private StatusEffectList seList;

	public Agent(AgentState state) {
		team = state.getTeam();
		position = state.getPosition();
		stats = state.getBasicStats().clone();
		weapons = state.getWeapons();
		weaponIndex = 0;
		armor = state.getArmor();
		abilities = state.getAbilities().clone();
		abilities.registerToAgent(this);
		name = state.getName();
		seList = new StatusEffectList();
	}

	@Override
	public BasicStats getBasicStats() {
		return stats;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public void setAP(int ap) {
		stats.setAP(ap);
	}

	public void useAP(int ap) {
		int temp = getAP() - ap;
		temp = temp < 0 ? 0 : temp;
		stats.setAP(temp);
	}

	public void setHP(int hp) {
		if (hp <= 0) {
			stats.setHP(0);
			state = State.KO;
		} else if (hp > stats.getMaxHP()) {
			stats.setMaxHP(stats.getMaxHP());
		} else {
			stats.setHP(hp);
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
		stats.setMP(mp);
	}

	public void useMP(int mp) {
		stats.setMP(stats.getMP() - mp);
	}

	public int getEffectiveMobility() {
		return getMobility() + seList.totalMobilityModifier();
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
		AbilityList allAbilities = abilities.clone();
		allAbilities.add(getWeaponGrantedAbilities());
		return allAbilities;
	}

	public AbilityList getInnateAbilities() {
		return abilities;
	}

	public AbilityList getWeaponGrantedAbilities() {
		if (getCurrentWeapon() != null) {
			return getCurrentWeapon().getGrantedAbilities();
		}
		return new AbilityList();
	}

	public Ability getAbility(Class<? extends Ability> abilityClass) {
		for (Ability a : abilities) {
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
		for (Ability a : abilities) {
			if (a.grantsSpell()) {
				l.add(a.grantedSpell());
			}
		}
		return l;
	}

	public void inflictStatus(StatusEffect se) {
		seList.add(se);
	}

	public StatusEffectList getStatusEffects() {
		return seList;
	}

	public GridPosition getPosition() {
		return position;
	}

	public void setPosition(GridPosition pos) {
		position = pos;
	}

	public float getDistanceFrom(Agent other) {
		return (float) getPosition().distance(other.getPosition());
	}

	public void onTurnBegin() {
		stats.setAP(stats.getMaxAP());
		if (!isKO()) {
			state = State.NEUTRAL;
		}
	}

	public void onTurnEnd() {
		decrementCooldowns();
	}

	public void onRoundEnd() {
		try {
			abilities.onRoundEnd();
		} catch (NoRegisteredAgentException e) {
			throw new IllegalStateException(e);
		}
		seList.onRoundEnd();
	}

	public void onAction(Action action) {
		seList.onAction(action);
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

	public void setOverwatch(OverwatchActivatedActionSchema owAction) {
		this.owAction = owAction;
		state = State.OVERWATCH;
		setAP(0);
	}

	public OverwatchActivatedActionSchema getOverwatchAction() {
		return owAction;
	}

	public void clearOverwatch() {
		owAction = null;
		state = State.NEUTRAL;
	}

	public void suppressOther(Agent other) {
		state = State.SUPPRESSING;
		other.inflictStatus(new SuppressedStatusEffect(other));
	}

}

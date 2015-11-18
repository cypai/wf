package com.pipai.wf.battle.agent;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pipai.wf.battle.BattleConfiguration;
import com.pipai.wf.battle.Team;
import com.pipai.wf.battle.action.Action;
import com.pipai.wf.battle.action.TargetedWithAccuracyActionOWCapable;
import com.pipai.wf.battle.armor.Armor;
import com.pipai.wf.battle.effect.StatusEffect;
import com.pipai.wf.battle.effect.StatusEffectList;
import com.pipai.wf.battle.effect.SuppressedStatusEffect;
import com.pipai.wf.battle.log.BattleEvent;
import com.pipai.wf.battle.map.BattleMap;
import com.pipai.wf.battle.map.BattleMapCell;
import com.pipai.wf.battle.map.CoverType;
import com.pipai.wf.battle.map.Direction;
import com.pipai.wf.battle.map.DirectionalCoverSystem;
import com.pipai.wf.battle.map.GridPosition;
import com.pipai.wf.battle.misc.OverwatchContainer;
import com.pipai.wf.battle.spell.Spell;
import com.pipai.wf.battle.vision.VisionCalculator;
import com.pipai.wf.battle.weapon.Weapon;
import com.pipai.wf.exception.IllegalActionException;
import com.pipai.wf.exception.NoRegisteredAgentException;
import com.pipai.wf.misc.HasName;
import com.pipai.wf.unit.ability.Ability;
import com.pipai.wf.unit.ability.AbilityList;
import com.pipai.wf.util.UtilFunctions;

public class Agent implements HasName {

	private static final Logger logger = LoggerFactory.getLogger(Agent.class);

	public enum State {
		NEUTRAL, KO, OVERWATCH, SUPPRESSING
	};

	private Team team;
	private int maxHP, maxAP, maxMP, hp, ap, mp;
	private int mobility, aim, defense;
	private String name;
	private State state;
	private ArrayList<Weapon> weapons;
	private int weaponIndex;
	private Armor armor;
	private BattleMap map;
	private GridPosition position;
	private OverwatchContainer owContainer;
	private AbilityList abilities;
	private StatusEffectList seList;
	private BattleConfiguration config;

	public Agent(AgentState state, BattleMap map, BattleConfiguration config) {
		this.map = map;
		team = state.team;
		position = state.position;
		maxHP = state.maxHP;
		hp = state.hp;
		maxAP = state.maxAP;
		mp = state.mp;
		maxMP = state.maxMP;
		ap = state.ap;
		mobility = state.mobility;
		aim = state.aim;
		defense = state.defense;
		weapons = state.weapons;
		weaponIndex = 0;
		armor = state.armor;
		abilities = state.abilities.clone();
		abilities.registerToAgent(this);
		name = state.name;
		owContainer = new OverwatchContainer();
		seList = new StatusEffectList();
		this.config = config;
	}

	public BattleMap getBattleMap() {
		return map;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public int getAP() {
		return ap;
	}

	public void setAP(int ap) {
		this.ap = ap;
	}

	public void useAP(int ap) {
		this.ap -= ap;
		if (this.ap < 0) {
			this.ap = 0;
		}
	}

	public int getMaxAP() {
		return maxAP;
	}

	public int getHP() {
		return hp;
	}

	public void setHP(int hp) {
		this.hp = hp;
		if (this.hp <= 0) {
			this.hp = 0;
			state = State.KO;
			map.getCell(position).makeAgentInactive();
		} else {
			state = State.NEUTRAL;
		}
	}

	public void takeDamage(int amt) {
		int pierceDmg = armor.takeDamage(amt);
		setHP(getHP() - pierceDmg);
	}

	public void heal(int amt) {
		hp += amt;
		if (hp > maxHP) {
			hp = maxHP;
		}
	}

	public int getMaxHP() {
		return maxHP;
	}

	public int getMP() {
		return mp;
	}

	public void setMP(int mp) {
		this.mp = mp;
	}

	public void useMP(int mp) {
		this.mp -= mp;
	}

	public int getMaxMP() {
		return maxMP;
	}

	public int getBaseMobility() {
		return mobility;
	}

	public int getEffectiveMobility() {
		return mobility + seList.totalMobilityModifier();
	}

	public int getBaseAim() {
		return aim;
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
	public String name() {
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
				l.add(a.getGrantedSpell());
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
		map.getCell(position).removeAgent();
		map.getCell(pos).setAgent(this);
		position = pos;
	}

	public float getDistanceFrom(Agent other) {
		return (float) getPosition().distance(other.getPosition());
	}

	public ArrayList<GridPosition> getPeekingSquares() {
		ArrayList<GridPosition> l = new ArrayList<>();
		l.add(getPosition());
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		ArrayList<Direction> coverDirs = coverSystem.getCoverDirections(getPosition());
		for (Direction coverDir : coverDirs) {
			ArrayList<Direction> perpendicularDirs = Direction.getPerpendicular(coverDir);
			for (Direction perpendicular : perpendicularDirs) {
				BattleMapCell peekSquare = map.getCellInDirection(getPosition(), perpendicular);
				if (peekSquare != null) {
					GridPosition pos = peekSquare.getPosition();
					BattleMapCell peekCoverSquare = map.getCellInDirection(pos, coverDir);

					if (peekSquare.isEmpty() && !peekCoverSquare.hasTileSightBlocker() && !l.contains(pos)) {
						l.add(peekSquare.getPosition());
					}
				}
			}
		}
		return l;
	}

	public CoverType getCoverType() {
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		return coverSystem.getCover(getPosition());
	}

	public boolean isOpen() {
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		return coverSystem.isOpen(getPosition());
	}

	public boolean isFlanked() {
		for (Agent a : enemiesInRange()) {
			if (isFlankedBy(a)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFlankedBy(Agent other) {
		ArrayList<GridPosition> otherPosList = other.getPeekingSquares();
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		for (GridPosition otherPos : otherPosList) {
			if (coverSystem.isFlankedBy(getPosition(), otherPos)) {
				return true;
			}
		}
		return false;
	}

	public int getDefense(Agent attacker) {
		int lowest = Integer.MAX_VALUE;
		for (GridPosition pos : attacker.getPeekingSquares()) {
			int curr = getDefense(pos);
			lowest = (curr < lowest) ? curr : lowest;
		}
		return lowest;
	}

	protected int getDefense(GridPosition attackerPos) {
		DirectionalCoverSystem coverSystem = new DirectionalCoverSystem(map);
		int situationalDef = defense + coverSystem.getBestCoverAgainstAttack(getPosition(), attackerPos).getDefense();
		return situationalDef;
	}

	public boolean canSee(Agent other) {
		for (GridPosition peekSquare : getPeekingSquares()) {
			for (GridPosition otherPeekSquare : other.getPeekingSquares()) {
				if (UtilFunctions.gridPositionDistance(peekSquare, otherPeekSquare) < config.sightRange()) {
					if (VisionCalculator.lineOfSight(map, peekSquare, otherPeekSquare)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean inRange(Agent other) {
		return other.canSee(this);
	}

	public ArrayList<Agent> enemiesInRange() {
		ArrayList<Agent> l = new ArrayList<>();
		for (Agent a : map.getAgents()) {
			if (a.team != team && !a.isKO() && canSee(a)) {
				l.add(a);
			}
		}
		return l;
	}

	public ArrayList<Agent> targetableEnemies() {
		ArrayList<Agent> list = new ArrayList<>();
		if (getCurrentWeapon().currentAmmo() == 0) {
			return list;
		} else {
			return enemiesInRange();
		}
	}

	public void onTurnBegin() {
		ap = maxAP;
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
			if (a.isOnCooldown()) {
				a.decrementCooldown();
			}
		}

		for (Ability a : getWeaponGrantedAbilities()) {
			if (a.isOnCooldown()) {
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

	public void overwatch(Class<? extends TargetedWithAccuracyActionOWCapable> attack) {
		owContainer.prepareAction(attack);
		state = State.OVERWATCH;
		setAP(0);
	}

	public void activateOverwatch(Agent other, BattleEvent activationLogEvent, GridPosition activatedTile) {
		TargetedWithAccuracyActionOWCapable action = owContainer.generateAction(this, other);
		try {
			other.setPosition(activatedTile);
			action.performOnOverwatch(activationLogEvent, config);
			owContainer.clear();
			state = State.NEUTRAL;
			if (getCurrentWeapon().needsAmmunition()) {
				getCurrentWeapon().expendAmmo(1);
			}
		} catch (IllegalActionException e) {
			logger.error(e.getMessage());
		}
	}

	public void suppressOther(Agent other) {
		state = State.SUPPRESSING;
		other.inflictStatus(new SuppressedStatusEffect(other));
	}

}
